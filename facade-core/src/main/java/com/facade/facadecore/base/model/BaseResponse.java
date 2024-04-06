package com.facade.facadecore.base.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Puck Wang
 * @project Blog
 * @created 1/21/2024
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
    protected T data;
    protected Map<?, ?> meta;

    public BaseResponse(T data) {
        this.data = data;
        this.meta = Map.of();
    }
}
