package com.facade.facadecore.core.project.dao;

import com.facade.facadecore.core.project.model.ProjectBO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author Puck Wang
 * @project Blog
 * @created 3/11/2024
 */
public interface ProjectDao extends CrudRepository<ProjectBO, UUID> {

    @Query(value = "SELECT * FROM projects WHERE published = true", nativeQuery = true)
    List<ProjectBO> findAllPublished();
}
