package com.facade.facadecore.core.project.rest;

import com.facade.facadecore.core.project.ProjectManager;
import com.facade.facadecore.core.project.mapper.ProjectMapper;
import com.facade.facadecore.core.project.model.ProjectBO;
import com.facade.facadecore.core.project.model.ProjectDTO;
import com.facade.facadecore.core.project.model.ProjectResponse;
import com.facade.facadecore.core.project.model.ProjectResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static com.facade.facadecore.constant.RestEndpoint.REST_V1_PROJECTS;

/**
 * @author Puck Wang
 * @project Blog
 * @created 3/11/2024
 */

@RestController("ProjectController")
@RequestMapping(REST_V1_PROJECTS)
@AllArgsConstructor
@Slf4j
public class ProjectControllerImpl implements ProjectController {

    private final ProjectMapper projectMapper;
    private final ProjectManager projectManager;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> get(@PathVariable(name = "id") UUID id) {

        ProjectBO projectBO = projectManager.get(id);
        ProjectDTO projectDTO = projectMapper.mapToDTO(projectBO);

        return ResponseEntity.ok(new ProjectResponse(projectDTO));
    }

    @Override
    @GetMapping("/list")
    public ResponseEntity<ProjectResponses> list(
            @RequestParam(name = "tagNames", required = false) List<String> tagNames,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
        List<ProjectBO> list = projectManager.list(true, tagNames);
        List<ProjectDTO> projectDTOS = list.stream().map(projectMapper::mapToDTO).toList();
        return ResponseEntity.ok(new ProjectResponses(projectDTOS));
    }

    @Override
    @GetMapping("/admin/list")
    public ResponseEntity<ProjectResponses> adminList(@RequestParam(name = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                                      @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
        List<ProjectBO> list = projectManager.list(false, List.of());
        List<ProjectDTO> projectDTOS = list.stream().map(projectMapper::mapToDTO).toList();
        return ResponseEntity.ok(new ProjectResponses(projectDTOS));
    }

    @Override
    @PostMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    public ResponseEntity<ProjectResponse> create(@RequestBody ProjectDTO projectDTO) {

        ProjectBO projectBO = projectMapper.mapToBO(projectDTO);
        ProjectBO createdProject = projectManager.create(projectBO);
        ProjectDTO createdProjectDTO = projectMapper.mapToDTO(createdProject);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdProjectDTO.getId())
                .toUri();
        return ResponseEntity.created(location).body(new ProjectResponse(createdProjectDTO));
    }

    @Override
    @PutMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.PUT)
    public ResponseEntity<ProjectResponse> update(@PathVariable(name = "id") UUID id, @RequestBody ProjectDTO projectDTO) {
        ProjectBO projectBO = projectMapper.mapToBO(projectDTO);
        ProjectBO updatedProjectBO = projectManager.update(projectBO);
        ProjectDTO updatedProjectDTO = projectMapper.mapToDTO(updatedProjectBO);
        return ResponseEntity.ok(new ProjectResponse(updatedProjectDTO));
    }

    @Override
    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        projectManager.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/batch")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@RequestBody List<UUID> ids) {
        log.debug("Deleting project with ids:{}", ids);

        projectManager.batchDelete(ids);

        log.debug("Deleted project with ids:{}", ids);
        return ResponseEntity.noContent().build();
    }
}
