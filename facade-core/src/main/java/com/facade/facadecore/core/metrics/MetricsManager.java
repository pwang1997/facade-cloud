package com.facade.facadecore.core.metrics;

import com.facade.facadecore.base.AsyncSQLExecutor;
import com.facade.facadecore.constant.Actions;
import com.facade.facadecore.core.metrics.dao.MetricsDao;
import com.facade.facadecore.core.metrics.model.MetricsBO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.facade.facadecore.constant.Subjects.SUBJECT_PROJECT;

/**
 * @author Puck Wang
 * @project Blog
 * @created 1/22/2024
 */

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class MetricsManager implements AsyncSQLExecutor {
    private final MetricsDao metricsDao;

    public MetricsBO create(MetricsBO postMetaBO) {
        return metricsDao.save(postMetaBO);
    }

    public MetricsBO create() {
        return metricsDao.save(MetricsBO.builder().likes(0L).views(0L).build());
    }

    public MetricsBO get(Long id) {
        return metricsDao.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public MetricsBO update(MetricsBO postMetaBO) {
        return metricsDao.save(postMetaBO);
    }

    @Override
    public CompletableFuture<?> doAction(String action) {
        log.info("PostManager---doAction: {}", action);
        if (Actions.COUNT_UNIQUE.equals(action)) {
            Long count = metricsDao.sumViews();
            return CompletableFuture.completedFuture(Map.of("name", SUBJECT_PROJECT, "count", count));
        }
        return CompletableFuture.completedFuture("No valid action found!");
    }
}
