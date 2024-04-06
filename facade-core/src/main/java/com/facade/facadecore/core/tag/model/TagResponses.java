package com.facade.facadecore.core.tag.model;

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
public class TagResponses extends BaseResponses<TagDTO> {

    public TagResponses(List<TagDTO> tagDTOS, Map<String, Map<?, ?>> metaMap) {
        super(tagDTOS, metaMap);
    }

    public TagResponses(List<TagDTO> tagDTOS) {
        super(tagDTOS);
    }
}
