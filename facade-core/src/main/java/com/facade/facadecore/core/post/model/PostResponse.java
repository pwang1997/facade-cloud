package com.facade.facadecore.core.post.model;

import com.facade.facadecore.base.model.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * @author Puck Wang
 * @project Blog
 * @created 1/21/2024
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PostResponse extends BaseResponse<PostDTO> {
    public PostResponse(PostDTO dto) {
        super(dto);
    }

    public PostResponse(PostDTO dto, Map<?, ?> meta) {
        super(dto, meta);
    }

}
