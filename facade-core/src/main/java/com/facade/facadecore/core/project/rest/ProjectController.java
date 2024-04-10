package com.facade.facadecore.core.project.rest;

import com.facade.facadecore.core.project.model.ProjectDTO;
import com.facade.facadecore.core.project.model.ProjectResponse;
import com.facade.facadecore.core.project.model.ProjectResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

/**
 * @author Puck Wang
 * @project Blog
 * @created 3/11/2024
 */
public interface ProjectController {
    ResponseEntity<ProjectResponse> get(UUID id);

    ResponseEntity<ProjectResponses> list(List<String> tagNames, Integer pageNumber, Integer pageSize);

    ResponseEntity<ProjectResponses> adminList(Integer pageNumber, Integer pageSize);

    ResponseEntity<ProjectResponse> create(ProjectDTO projectDTO);

    ResponseEntity<ProjectResponse> update(UUID id, ProjectDTO projectDTO);

    ResponseEntity<Void> delete(UUID id);

    ResponseEntity<Void> delete(List<UUID> id);
}
