package com.facade.facadecore.core.tag.rest;

import com.facade.facadecore.core.tag.TagManager;
import com.facade.facadecore.core.tag.mapper.TagMapper;
import com.facade.facadecore.core.tag.model.TagBO;
import com.facade.facadecore.core.tag.model.TagDTO;
import com.facade.facadecore.core.tag.model.TagResponse;
import com.facade.facadecore.core.tag.model.TagResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.facade.facadecore.constant.RestEndpoint.REST_V1_TAGS;

/**
 * @author Puck Wang
 * @project Blog
 * @created 3/3/2024
 */

@RestController
@RequestMapping(REST_V1_TAGS)
@AllArgsConstructor
@Slf4j
public class TagControllerImpl implements TagController {

    private final TagManager tagManager;
    private final TagMapper tagMapper;

    @Override
    @GetMapping("/list")
    public ResponseEntity<TagResponses> list(@RequestParam(name = "relatedToFetch", required = false) List<String> relatedToFetch) {
        List<TagBO> tagBOS = tagManager.list();
        List<TagDTO> tagDTOS = tagBOS.stream().map(tagMapper::mapToDTO).toList();

        List<UUID> tagIds = tagBOS.stream().filter(TagBO::isPublished).map(TagBO::getId).toList();

        Map<String, Map<?, ?>> metaMap = (Map<String, Map<?, ?>>) fetchRelatedFields(tagIds, relatedToFetch);

        return ResponseEntity.ok(new TagResponses(tagDTOS, metaMap));
    }



    @Override
    @PostMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    public ResponseEntity<TagResponse> create(@RequestBody TagDTO tagDTO) {
        TagBO tag = tagMapper.mapToBO(tagDTO);
        TagBO createdTagBO = tagManager.create(tag);
        TagDTO createdTagDTO = tagMapper.mapToDTO(createdTagBO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTagDTO.getId())
                .toUri();
        return ResponseEntity.created(location).body(new TagResponse(createdTagDTO));
    }

    @Override
    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID id) {
        log.debug("Deleting tag with id:{}", id);

        tagManager.delete(id);

        log.debug("Deleted tag with id:{}", id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/batch")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@RequestBody List<UUID> ids) {
        log.debug("Deleting tags with ids:{}", ids);

        tagManager.batchDelete(ids);

        log.debug("Deleted tags with ids:{}", ids);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/admin/list")
    public ResponseEntity<TagResponses> adminList(@RequestParam(name = "relatedToFetch", required = false) List<String> relatedToFetch) {
        List<TagBO> tagBOS = tagManager.list();
        List<TagDTO> tagDTOS = tagBOS.stream().map(tagMapper::mapToDTO).toList();

        List<UUID> tagIds = tagBOS.stream().map(TagBO::getId).toList();

        Map<String, Map<?, ?>> metaMap = (Map<String, Map<?, ?>>) fetchRelatedFields(tagIds, relatedToFetch);

        return ResponseEntity.ok(new TagResponses(tagDTOS, metaMap));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<TagResponse> update(@PathVariable(name = "id")  UUID id, @RequestBody TagDTO tagDTO) {
        TagDTO update = tagMapper.mapToDTO(tagManager.update(tagMapper.mapToBO(tagDTO)));
        return ResponseEntity.ok(new TagResponse(update));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<TagResponse> get(@PathVariable(name = "id")UUID id) {
        TagDTO tagDTO = tagMapper.mapToDTO(tagManager.get(id));
        return ResponseEntity.ok(new TagResponse(tagDTO));
    }

    private Map<?, ?> fetchRelatedFields(List<UUID> tagIds, List<String> relatedToFetch) {
        if (CollectionUtils.isEmpty(relatedToFetch)) {
            return Map.of();
        }
        Map<String, Map<?, ?>> metaMap = new HashMap<>();
        for (String subject : relatedToFetch) {
            Map<UUID, ?> map = tagManager.fetchRelatedSubjects(subject, tagIds);
            metaMap.put(subject, map);
        }

        return metaMap;
    }
}
