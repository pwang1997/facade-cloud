package com.facade.facadecore.core.post.rest;

import com.facade.facadecore.core.post.model.PostDTO;
import com.facade.facadecore.core.post.model.PostResponse;
import com.facade.facadecore.core.post.model.PostResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

/**
 * @author Puck Wang
 * @project Blog
 * @created 1/22/2024
 */
public interface PostController {

    ResponseEntity<PostResponse> get(UUID id);

    ResponseEntity<PostResponses> list(List<String> tagNames, Integer pageNumber, Integer pageSize);

    ResponseEntity<PostResponses> adminList(Integer pageNumber, Integer pageSize);

    ResponseEntity<PostResponse> create(PostDTO article);

    ResponseEntity<PostResponse> update(PostDTO article);

    ResponseEntity<Void> delete(UUID id);

    ResponseEntity<Void> delete(List<UUID> ids);

    ResponseEntity<PostResponses> export(Integer pageNumber, Integer pageSize);

}
