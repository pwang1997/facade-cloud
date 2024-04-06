package com.facade.facadecore.core.metrics.mapper;

import com.facade.facadecore.base.mapper.BaseMapper;
import com.facade.facadecore.core.metrics.model.MetricsBO;
import com.facade.facadecore.core.metrics.model.MetricsDTO;
import org.mapstruct.Mapper;

/**
 * @author Puck Wang
 * @project Blog
 * @created 1/22/2024
 */

@Mapper(componentModel = "spring")
public interface MetricsMapper extends BaseMapper<MetricsBO, MetricsDTO> {
}
