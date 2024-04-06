package com.facade.facadecore.core.project.mapper;

import com.facade.facadecore.base.mapper.BaseMapper;
import com.facade.facadecore.core.project.model.ProjectBO;
import com.facade.facadecore.core.project.model.ProjectDTO;
import org.mapstruct.Mapper;

/**
 * @author Puck Wang
 * @project Blog
 * @created 3/11/2024
 */
@Mapper(componentModel = "spring")
public interface ProjectMapper extends BaseMapper<ProjectBO, ProjectDTO> {
}
