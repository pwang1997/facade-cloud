package com.facade.facadecore.core.metrics.model;

import com.facade.facadecore.base.model.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * @author Puck Wang
 * @project Blog
 * @created 3/25/2024
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SystemMetricsResponse extends BaseResponse {
    public SystemMetricsResponse(Object data, Map meta) {
        super(data, meta);
    }

    public SystemMetricsResponse(Object data) {
        super(data);
    }
}
