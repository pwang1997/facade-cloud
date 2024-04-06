package com.facade.facadecore.core.project.model;

import com.facade.facadecore.base.model.BaseResponses;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * @author Puck Wang
 * @project Blog
 * @created 3/11/2024
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectResponses extends BaseResponses<ProjectDTO> {
    public ProjectResponses(List<ProjectDTO> dtos) {
        super(dtos);
    }

    public ProjectResponses(List<ProjectDTO> dtos, Map<?, ?> meta) {
        super(dtos, meta);
    }
}
