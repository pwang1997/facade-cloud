package com.facade.facadecore.core.post.mapper;

import com.facade.facadecore.base.mapper.BaseMapper;
import com.facade.facadecore.core.post.model.PostBO;
import com.facade.facadecore.core.post.model.PostDTO;
import org.mapstruct.Mapper;

/**
 * @author Puck Wang
 * @project Blog
 * @created 1/22/2024
 */

@Mapper(componentModel = "spring")
public abstract class PostMapper implements BaseMapper<PostBO, PostDTO> {
}
