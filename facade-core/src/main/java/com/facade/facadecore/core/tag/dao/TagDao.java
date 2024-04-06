package com.facade.facadecore.core.tag.dao;

import com.facade.facadecore.core.tag.model.TagBO;
import com.facade.facadecore.core.tag.model.TagUsageDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Puck Wang
 * @project Blog
 * @created 1/24/2024
 */
public interface TagDao extends CrudRepository<TagBO, Long> {

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM tags WHERE name = ?1"
    )
    TagBO findByName(String name);


    @Query(
            nativeQuery = true,
            value = "SELECT t.name as tagName, COUNT(*) as occurrence FROM tags t JOIN post_tag_assn pta ON pta.tag_id = t.id GROUP BY t.name"
    )
    List<TagUsageDTO> countPostTagUsage();


    @Query(
            nativeQuery = true,
            value = "SELECT t.name as tagName, COUNT(*) as occurrence FROM tags t JOIN project_tag_assn pta ON pta.tag_id = t.id GROUP BY t.name"
    )
    List<TagUsageDTO> countProjectTagUsage();
}
