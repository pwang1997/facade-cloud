package com.facade.facadecore.core.tag;

import com.facade.facadecore.base.AsyncSQLExecutor;
import com.facade.facadecore.constant.Actions;
import com.facade.facadecore.core.metrics.SystemMetricsManager;
import com.facade.facadecore.core.post.dao.PostDao;
import com.facade.facadecore.core.tag.dao.TagDao;
import com.facade.facadecore.core.tag.model.TagBO;
import com.facade.facadecore.core.tag.model.TagUsageDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.facade.facadecore.constant.Subjects.*;

/**
 * @author Puck Wang
 * @project Blog
 * @created 1/24/2024
 */

@Service
@Slf4j
public class TagManager implements AsyncSQLExecutor {

    private final TagDao tagDao;
    private final PostDao postDao;
    private final SystemMetricsManager systemMetricsManager;

    public TagManager(TagDao tagDao, PostDao postDao, @Lazy SystemMetricsManager systemMetricsManager) {
        this.tagDao = tagDao;
        this.postDao = postDao;
        this.systemMetricsManager = systemMetricsManager;
    }

    public List<TagBO> list() {
        return (List<TagBO>) tagDao.findAll();
    }

    public Map<Long, ?> fetchRelatedSubjects(String relatedSubject, List<Long> tagIds) {
        Map<Long, Long> countMap = new HashMap<>();
        if (relatedSubject.equals(SUBJECT_POST)) {
            for (Long tagId : tagIds) {
                countMap.put(tagId, postDao.countRelatedPostByTagId(tagId));
            }
        }
        return countMap;
    }

    @Transactional
    public TagBO create(TagBO tagBO) {
        TagBO byName = tagDao.findByName(tagBO.getName());
        if (ObjectUtils.isEmpty(byName)) {
            return tagDao.save(tagBO);
        }
        systemMetricsManager.invalidateSystemMetricsKey();
        return byName;
    }

    @Transactional
    public List<TagBO> create(List<TagBO> tagBOs) {
        systemMetricsManager.invalidateSystemMetricsKey();
        return (List<TagBO>) tagDao.saveAll(tagBOs);
    }

    public TagBO get(Long id) {
        return tagDao.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public TagBO update(TagBO tagBO) {
        return tagDao.save(tagBO);
    }

    @Transactional
    public void delete(Long id) {
        tagDao.deleteById(id);
        systemMetricsManager.invalidateSystemMetricsKey();
    }

    @Transactional
    public void batchDelete(List<Long> ids) {
        tagDao.deleteAllById(ids);
        systemMetricsManager.invalidateSystemMetricsKey();
    }


    public List<TagUsageDTO> getPostTagUsage() {
        log.debug("getPostTagUsage...");
        return tagDao.countPostTagUsage();
    }

    public List<TagUsageDTO> getProjectTagUsage() {
        log.debug("getProjectTagUsage...");
        return tagDao.countProjectTagUsage();
    }

    @Override
    @Async("asyncSQLExecutor")
    public CompletableFuture<?> doAction(String action) {
        log.info("TagManager---doAction: {}", action);
        if (Actions.COUNT_UNIQUE.equals(action)) {
            List<TagBO> list = this.list();
            return CompletableFuture.completedFuture(Map.of("name", SUBJECT_TAG, "count", list.size()));
        } else if (Actions.COUNT_USAGE.equals(action)) {
            List<TagUsageDTO> projectTagUsageList = getProjectTagUsage();
            List<TagUsageDTO> postTagUsageList = getPostTagUsage();

            List<Map<String, ?>> projectTagList = new ArrayList<>();
            for (TagUsageDTO usageDTO : projectTagUsageList) {
                projectTagList.add(Map.of("name", usageDTO.getTagName(), "count", usageDTO.getOccurrence()));
            }

            List<Map<String, ?>> postTagList = new ArrayList<>();
            for (TagUsageDTO usageDTO : postTagUsageList) {
                postTagList.add(Map.of("name", usageDTO.getTagName(), "count", usageDTO.getOccurrence()));
            }

            return CompletableFuture.completedFuture(Map.of(SUBJECT_POST, postTagList, SUBJECT_PROJECT, projectTagList));
        }
        return CompletableFuture.completedFuture("No valid action found!");
    }
}
