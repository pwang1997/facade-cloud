package com.facade.facadecore.core.tag.model;

import com.facade.facadecore.base.model.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Puck Wang
 * @project Blog
 * @created 1/21/2024
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TagResponse extends BaseResponse<TagDTO> {
    public TagResponse(TagDTO tagDTO) {
        super(tagDTO);
    }
}
