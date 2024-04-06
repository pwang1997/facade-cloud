package com.facade.facadecore.core.metrics.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

/**
 * @author Puck Wang
 * @project Blog
 * @created 3/25/2024
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@SuperBuilder
@JsonInclude
public class SystemMetricsDTO {
    private Map<String, ?> fieldsToFetch;
}
