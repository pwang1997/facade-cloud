package com.facade.facadecore.core.post.model;

import com.facade.facadecore.base.model.BaseResponses;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * @author Puck Wang
 * @project Blog
 * @created 1/21/2024
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PostResponses extends BaseResponses<PostDTO> {
    public PostResponses(List<PostDTO> dtos, Map<?, ?> meta) {
        super(dtos, meta);
    }
}
