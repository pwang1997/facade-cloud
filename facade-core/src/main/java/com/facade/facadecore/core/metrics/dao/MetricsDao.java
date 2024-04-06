package com.facade.facadecore.core.metrics.dao;

import com.facade.facadecore.core.metrics.model.MetricsBO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Puck Wang
 * @project Blog
 * @created 3/23/2024
 */

@Repository
public interface MetricsDao extends CrudRepository<MetricsBO, Long> {

    @Query(
            nativeQuery = true,
            value = "SELECT SUM(views) FROM metrics"
    )
    Long sumViews();
}
