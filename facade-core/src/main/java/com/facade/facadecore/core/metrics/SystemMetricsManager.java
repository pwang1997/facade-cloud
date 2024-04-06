package com.facade.facadecore.core.metrics;

import com.facade.facadecore.core.post.PostManager;
import com.facade.facadecore.core.project.ProjectManager;
import com.facade.facadecore.core.tag.TagManager;
import com.facade.facadecore.redis.RedisManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.facade.facadecore.constant.RedisConstant.REDIS_KEY_METRICS;
import static com.facade.facadecore.constant.Subjects.*;

/**
 * @author Puck Wang
 * @project Blog
 * @created 3/25/2024
 */

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class SystemMetricsManager {
    private final TagManager tagManager;
    private final PostManager postManager;
    private final ProjectManager projectManager;
    private final MetricsManager metricsManager;
    private final RedisManager redisManager;

    @Async("asyncSQLExecutor")
    public CompletableFuture<?> doTargetAction(String target, String action) {
        String key = REDIS_KEY_METRICS + "-targetAction";
        Map<String, String> targetActionMap = (Map) redisManager.hGet(key, target + ":" + action);

        if (MapUtils.isNotEmpty(targetActionMap)) {
            return CompletableFuture.completedFuture(targetActionMap);
        }
        return switch (target) {
            case SUBJECT_POST -> postManager.doAction(action);
            case SUBJECT_TAG -> tagManager.doAction(action);
            case SUBJECT_PROJECT -> projectManager.doAction(action);
            case SUBJECT_VIEW -> metricsManager.doAction(action);
            default -> throw new IllegalStateException("Unexpected value: " + target);
        };
    }

    public void cacheTargetAction(String hashKey, Object hashVal) {
        String key = REDIS_KEY_METRICS + "-targetAction";
        Map<String, String> targetActionMap = (Map) redisManager.hGet(key, hashKey);
        if (MapUtils.isEmpty(targetActionMap)) {
            redisManager.hSet(key, hashKey, hashVal);
        }
    }

    public void invalidateSystemMetricsKey() {
        redisManager.del(REDIS_KEY_METRICS + "-targetAction");
    }
}
