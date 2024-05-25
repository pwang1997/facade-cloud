package com.facade.facadecore.core.post.dao;

import com.facade.facadecore.core.post.model.PostBO;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Puck Wang
 * @project Blog
 * @created 1/21/2024
 */

@Repository
public interface PostDao extends CrudRepository<PostBO, UUID> {

  @Query(value = "SELECT COUNT(post_id) FROM  post_tag_assn WHERE tag_id = ?1", nativeQuery = true)
  Long countRelatedPostByTagId(UUID tagId);

  @Query(value = "SELECT * FROM posts WHERE published = true", nativeQuery = true)
  List<PostBO> findAllPublished();

  @Query(
      value = "SELECT * FROM posts WHERE content LIKE %:query% OR title LIKE %:query%",
      nativeQuery = true
  )
  List<PostBO> search(@Param(value = "query") String searchQuery);
}
