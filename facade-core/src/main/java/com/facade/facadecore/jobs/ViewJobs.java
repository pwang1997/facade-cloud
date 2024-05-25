package com.facade.facadecore.jobs;

import com.facade.facadecore.core.metrics.MetricsManager;
import com.facade.facadecore.core.metrics.model.MetricsBO;
import com.facade.facadecore.core.post.PostManager;
import com.facade.facadecore.core.project.ProjectManager;
import com.facade.facadecore.redis.RedisManager;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Puck Wang
 * @project facade-cloud
 * @created 5/24/2024
 */

@Component
@Slf4j
public class ViewJobs {

  private final ProjectManager projectManager;
  private final PostManager postManager;
  private final MetricsManager metricsManager;
  private final RedisManager redisManager;

  public ViewJobs(ProjectManager projectManager, PostManager postManager,
      MetricsManager metricsManager,
      RedisManager redisManager) {
    this.projectManager = projectManager;
    this.postManager = postManager;
    this.metricsManager = metricsManager;
    this.redisManager = redisManager;
  }

  @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
  @Async("asyncJobExecutor")
  public void updateProjectViews() {
    log.info("Starting scheduled job[updateProjectViews]");
    Map<Object, Object> projectViewMap = redisManager.hGetAll("PROJECT-VIEWS");

    BiConsumer<Object, Object> updateViewAction = (projectId, viewVal) -> {
      Long id = projectManager.get(UUID.fromString((String) projectId)).getMetrics().getId();
      MetricsBO metricsBO = metricsManager.get(id);
      metricsBO.setViews((Long) viewVal);
      metricsManager.update(metricsBO);
    };
    projectViewMap.forEach(updateViewAction);
    log.info("Finished scheduled job[updateProjectViews]");
  }


  @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
  @Async("asyncJobExecutor")
  public void updatePostViews() {
    log.info("Starting scheduled job[updatePostViews]");

    Map<Object, Object> projectViewMap = redisManager.hGetAll("POST-VIEWS");

    BiConsumer<Object, Object> updateViewAction = (projectId, viewVal) -> {
      Long id = postManager.get(UUID.fromString((String) projectId)).getMetrics().getId();
      MetricsBO metricsBO = metricsManager.get(id);
      metricsBO.setViews((Long) viewVal);
      metricsManager.update(metricsBO);
    };
    projectViewMap.forEach(updateViewAction);
    log.info("Finished scheduled job[updatePostViews]");
  }
}
