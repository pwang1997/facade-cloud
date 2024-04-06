package com.facade.facadecore.base.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Puck Wang
 * @project Blog
 * @created 1/21/2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponses<T> {
    protected List<T> data;
    protected Map<?, ?> meta;

    public BaseResponses(List<T> data) {
        this.data = data;
        this.meta = Map.of();
    }

}
