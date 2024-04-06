package com.facade.facadecore.core.tag.mapper;

import com.facade.facadecore.base.mapper.BaseMapper;
import com.facade.facadecore.core.tag.model.TagBO;
import com.facade.facadecore.core.tag.model.TagDTO;
import org.mapstruct.Mapper;

/**
 * @author Puck Wang
 * @project Blog
 * @created 1/24/2024
 */

@Mapper(componentModel = "spring")
public interface TagMapper extends BaseMapper<TagBO, TagDTO> {
}
