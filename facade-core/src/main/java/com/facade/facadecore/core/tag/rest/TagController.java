package com.facade.facadecore.core.tag.rest;

import com.facade.facadecore.core.tag.model.TagDTO;
import com.facade.facadecore.core.tag.model.TagResponse;
import com.facade.facadecore.core.tag.model.TagResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

/**
 * @author Puck Wang
 * @project Blog
 * @created 3/3/2024
 */
public interface TagController {
    ResponseEntity<TagResponses> list(List<String> relatedToFetch);

    ResponseEntity<TagResponse> create(TagDTO tagDTO);

    ResponseEntity<Void> delete(UUID id);

    ResponseEntity<Void> delete(List<UUID> id);
    ResponseEntity<TagResponses> adminList(List<String> relatedToFetch);
    ResponseEntity<TagResponse> update(UUID id, TagDTO tagDTO);

    ResponseEntity<TagResponse> get(UUID id);
}
