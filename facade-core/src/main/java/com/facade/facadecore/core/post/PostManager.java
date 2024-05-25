package com.facade.facadecore.core.post;

import static com.facade.facadecore.constant.Actions.COUNT_UNIQUE;
import static com.facade.facadecore.constant.RedisConstant.REDIS_KEY_POST;
import static com.facade.facadecore.constant.Subjects.SUBJECT_PROJECT;

import com.facade.facadecore.base.AsyncSQLExecutor;
import com.facade.facadecore.base.RedisAction;
import com.facade.facadecore.core.metrics.MetricsManager;
import com.facade.facadecore.core.metrics.SystemMetricsManager;
import com.facade.facadecore.core.metrics.model.MetricsBO;
import com.facade.facadecore.core.post.dao.PostDao;
import com.facade.facadecore.core.post.model.PostBO;
import com.facade.facadecore.core.tag.TagManager;
import com.facade.facadecore.core.tag.model.TagBO;
import com.facade.facadecore.redis.RedisManager;
import com.facade.facadecore.utils.BOUtils;
import com.facade.facadecore.utils.GsonUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author Puck Wang
 * @project Blog
 * @created 1/22/2024
 */

@Slf4j
@Service
@Transactional
public class PostManager implements AsyncSQLExecutor, RedisAction<UUID> {

  private final PostDao postDao;
  private final MetricsManager metricsManager;
  private final TagManager tagManager;
  private final BOUtils boUtils;
  private final RedisManager redisManager;
  private final GsonUtils gsonUtils;
  private final SystemMetricsManager systemMetricsManager;

  public PostManager(PostDao postDao, MetricsManager metricsManager, TagManager tagManager,
      BOUtils boUtils, RedisManager redisManager, GsonUtils gsonUtils,
      @Lazy SystemMetricsManager systemMetricsManager) {
    this.postDao = postDao;
    this.metricsManager = metricsManager;
    this.tagManager = tagManager;
    this.boUtils = boUtils;
    this.redisManager = redisManager;
    this.gsonUtils = gsonUtils;
    this.systemMetricsManager = systemMetricsManager;
  }

  public PostBO create(PostBO postBO) {
    if (CollectionUtils.isNotEmpty(postBO.getTags())) {
      tagManager.create(postBO.getTags());
    }
    boUtils.setDirtyFields(postBO);
    postDao.save(postBO);
    MetricsBO postMetaBO = metricsManager.create();
    postBO.setMetrics(postMetaBO);
    PostBO save = postDao.save(postBO);

    redisManager.set(getRedisKeyById(save.getId()), gsonUtils.toJson(save));
    InvalidateRedisListKey();
    return save;
  }

  public PostBO get(UUID uuid) {
    String key = getRedisKeyById(uuid);
    String redisObject = (String) redisManager.get(key);
    PostBO postBO = gsonUtils.fromJsonIfPresent(redisObject, PostBO.class);
    if (ObjectUtils.isEmpty(postBO)) {
      postBO = postDao.findById(uuid).orElseThrow(EntityNotFoundException::new);
      redisManager.set(key, gsonUtils.toJson(postBO));
    }
    updateViews(String.valueOf(uuid), postBO.getMetrics().getViews());

    Long dirtyViews = (Long) redisManager.hGet("POST-VIEWS", uuid.toString());
    postBO.getMetrics().setViews(dirtyViews);
    return postBO;
  }

  public List<PostBO> list(boolean publishedOnly) {
    return publishedOnly ? postDao.findAllPublished() : (List<PostBO>) postDao.findAll();
  }

  public List<PostBO> list(boolean publishedOnly, List<String> tagNames) {
    String key = getRedisKeyByPublished(publishedOnly);
    String redisObject = (String) redisManager.get(key);
    List redisObjects = gsonUtils.fromJsonIfPresent(redisObject, List.class);
    List<PostBO> postBOs;
    if (CollectionUtils.isEmpty(redisObjects)) {
      postBOs = list(publishedOnly);
      redisManager.set(key, gsonUtils.toJson(postBOs));
    } else {
      postBOs = gsonUtils.convertJsonTreeToList(redisObjects, PostBO.class);
    }

    if (CollectionUtils.isNotEmpty(tagNames)) {
      postBOs = postBOs
          .stream()
          .filter((postBO -> CollectionUtils.containsAny(tagNames,
              postBO.getTags().stream().map(TagBO::getName).toList())))
          .toList();
    }
    return postBOs;
  }

  public PostBO update(PostBO postBO) {
    String redisKey = getRedisKeyById(postBO.getId());
    PostBO updated = postDao.save(postBO);

    redisManager.set(redisKey, gsonUtils.toJson(updated));
    InvalidateRedisListKey();

    return updated;
  }

  public void delete(UUID uuid) {
    String redisKey = getRedisKeyById(uuid);
    postDao.deleteById(uuid);
    redisManager.del(redisKey);
    InvalidateRedisListKey();
  }

  public void batchDelete(List<UUID> ids) {
    postDao.deleteAllById(ids);

    InvalidateRedisListKey();
    List<String> redisKeys = ids.stream().map(this::getRedisKeyById).toList();
    redisManager.del(redisKeys);
  }

  @Override
  public String getRedisKeyById(UUID id) {
    return REDIS_KEY_POST + "-id-" + id;
  }

  @Override
  public String getRedisKeyByPublished(boolean publishedOnly) {
    return REDIS_KEY_POST + "-publishedOnly-" + publishedOnly;
  }

  @Override
  public void InvalidateRedisListKey() {
    redisManager.del(getRedisKeyByPublished(true));
    redisManager.del(getRedisKeyByPublished(false));
    systemMetricsManager.invalidateSystemMetricsKey();
  }

  @Override
  @Async("asyncSQLExecutor")
  public CompletableFuture<?> doAction(String action) {
    log.info("PostManager---doAction: [{}]", action);
    if (COUNT_UNIQUE.equals(action)) {
      List<PostBO> list = this.list(true);
      return CompletableFuture.completedFuture(
          Map.of("name", SUBJECT_PROJECT, "count", list.size()));
    }
    return CompletableFuture.completedFuture("No valid action found!");
  }

  @Async("asyncSQLExecutor")
  public void updateViews(String id, long currentViews) {
    String redisKey = "POST-VIEWS";
    if (ObjectUtils.isEmpty(redisManager.hGet(redisKey, id))) {
      redisManager.hSet(redisKey, id, currentViews);
    } else {
      Long views = (Long) redisManager.hGet(redisKey, id);
      redisManager.hSet(redisKey, id, views + 1);
    }
  }
}
