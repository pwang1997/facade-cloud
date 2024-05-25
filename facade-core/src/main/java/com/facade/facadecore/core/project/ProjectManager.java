package com.facade.facadecore.core.project;

import static com.facade.facadecore.constant.Actions.COUNT_UNIQUE;
import static com.facade.facadecore.constant.RedisConstant.REDIS_KEY_PROJECT;
import static com.facade.facadecore.constant.Subjects.SUBJECT_PROJECT;

import com.facade.facadecore.base.AsyncSQLExecutor;
import com.facade.facadecore.base.RedisAction;
import com.facade.facadecore.core.metrics.MetricsManager;
import com.facade.facadecore.core.metrics.SystemMetricsManager;
import com.facade.facadecore.core.metrics.model.MetricsBO;
import com.facade.facadecore.core.project.dao.ProjectDao;
import com.facade.facadecore.core.project.model.ProjectBO;
import com.facade.facadecore.core.tag.TagManager;
import com.facade.facadecore.core.tag.model.TagBO;
import com.facade.facadecore.redis.RedisManager;
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
 * @created 3/11/2024
 */

@Slf4j
@Service
@Transactional
public class ProjectManager implements AsyncSQLExecutor, RedisAction<UUID> {

  private final ProjectDao projectDao;
  private final TagManager tagManager;
  private final MetricsManager metricsManager;
  private final RedisManager redisManager;
  private final GsonUtils gsonUtils;
  private final SystemMetricsManager systemMetricsManager;

  public ProjectManager(ProjectDao projectDao, TagManager tagManager, MetricsManager metricsManager,
      RedisManager redisManager, GsonUtils gsonUtils,
      @Lazy SystemMetricsManager systemMetricsManager) {
    this.projectDao = projectDao;
    this.tagManager = tagManager;
    this.metricsManager = metricsManager;
    this.redisManager = redisManager;
    this.gsonUtils = gsonUtils;
    this.systemMetricsManager = systemMetricsManager;
  }

  public ProjectBO create(ProjectBO projectBO) {
    if (CollectionUtils.isNotEmpty(projectBO.getTags())) {
      tagManager.create(projectBO.getTags());
    }
    MetricsBO metricsBO = metricsManager.create();
    projectBO.setMetrics(metricsBO);
    ProjectBO save = projectDao.save(projectBO);

    redisManager.set(getRedisKeyById(save.getId()), gsonUtils.toJson(save));
    InvalidateRedisListKey();
    return save;
  }

  public ProjectBO get(UUID id) {
    String key = getRedisKeyById(id);
    String redisObject = (String) redisManager.get(key);
    ProjectBO projectBO = gsonUtils.fromJsonIfPresent(redisObject, ProjectBO.class);
    if (ObjectUtils.isEmpty(projectBO)) {
      projectBO = projectDao.findById(id).orElseThrow(EntityNotFoundException::new);
      redisManager.set(key, gsonUtils.toJson(projectBO));
    }
    updateViews(id.toString(), projectBO.getMetrics().getViews());

    Long dirtyViews = (Long) redisManager.hGet("PROJECT-VIEWS", id.toString());
    projectBO.getMetrics().setViews(dirtyViews);
    return projectBO;
  }

  public List<ProjectBO> list(boolean publishedOnly) {
    return publishedOnly ? projectDao.findAllPublished() : (List<ProjectBO>) projectDao.findAll();
  }

  public List<ProjectBO> list(boolean publishedOnly, List<String> tagNames) {
    String key = getRedisKeyByPublished(publishedOnly);
    String redisObject = (String) redisManager.get(key);
    List redisObjects = gsonUtils.fromJsonIfPresent(redisObject, List.class);
    List<ProjectBO> projectBOs;
    if (CollectionUtils.isEmpty(redisObjects)) {
      projectBOs = list(publishedOnly);
      redisManager.set(key, gsonUtils.toJson(projectBOs));
    } else {
      projectBOs = gsonUtils.convertJsonTreeToList(redisObjects, ProjectBO.class);
    }

    if (CollectionUtils.isNotEmpty(tagNames)) {
      projectBOs = projectBOs
          .stream()
          .filter((postBO -> CollectionUtils.containsAny(tagNames,
              postBO.getTags().stream().map(TagBO::getName).toList())))
          .toList();
    }
    return projectBOs;
  }

  public ProjectBO update(ProjectBO projectBO) {
    String redisKey = getRedisKeyById(projectBO.getId());
    ProjectBO updated = projectDao.save(projectBO);

    redisManager.set(redisKey, gsonUtils.toJson(updated));
    InvalidateRedisListKey();
    return updated;
  }

  public void delete(UUID uuid) {
    String redisKey = getRedisKeyById(uuid);
    redisManager.del(redisKey);
    projectDao.deleteById(uuid);
    InvalidateRedisListKey();
  }

  public void batchDelete(List<UUID> ids) {
    projectDao.deleteAllById(ids);
    InvalidateRedisListKey();
    List<String> redisKeys = ids.stream().map(this::getRedisKeyById).toList();
    redisManager.del(redisKeys);
  }

  @Async("asyncSQLExecutor")
  public CompletableFuture<?> doAction(String action) {
    log.info("ProjectManager---doAction: {}", action);
    if (COUNT_UNIQUE.equals(action)) {
      List<ProjectBO> list = this.list(true);
      return CompletableFuture.completedFuture(
          Map.of("name", SUBJECT_PROJECT, "count", list.size()));
    }
    return CompletableFuture.completedFuture("No valid action found!");
  }

  @Async("asyncSQLExecutor")
  public void updateViews(String id, long currentViews) {
    String key = "PROJECT-VIEWS";
    if (ObjectUtils.isEmpty(redisManager.hGet(key, id))) {
      redisManager.hSet(key, id, currentViews);
    } else {
      Long views = (Long) redisManager.hGet(key, id);
      redisManager.hIncr(key, id, views + 1);
    }
  }

  @Override
  public String getRedisKeyById(UUID id) {
    return REDIS_KEY_PROJECT + "-id-" + id;
  }

  @Override
  public String getRedisKeyByPublished(boolean publishedOnly) {
    return REDIS_KEY_PROJECT + "-publishedOnly-" + publishedOnly;
  }

  @Override
  public void InvalidateRedisListKey() {
    redisManager.del(getRedisKeyByPublished(true));
    redisManager.del(getRedisKeyByPublished(false));
    systemMetricsManager.invalidateSystemMetricsKey();
  }
}
