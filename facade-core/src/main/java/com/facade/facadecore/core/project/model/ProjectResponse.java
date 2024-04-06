package com.facade.facadecore.core.project.model;

import com.facade.facadecore.base.model.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * @author Puck Wang
 * @project Blog
 * @created 3/11/2024
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectResponse extends BaseResponse<ProjectDTO> {
    public ProjectResponse(ProjectDTO dto) {
        super(dto);
    }

    public ProjectResponse(ProjectDTO dto, Map<?, ?> meta) {
        super(dto, meta);
    }
}
