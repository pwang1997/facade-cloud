package com.facade.facadecore.core.post.rest;

import com.facade.facadecore.core.post.PostManager;
import com.facade.facadecore.core.post.mapper.PostMapper;
import com.facade.facadecore.core.post.model.PostBO;
import com.facade.facadecore.core.post.model.PostDTO;
import com.facade.facadecore.core.post.model.PostResponse;
import com.facade.facadecore.core.post.model.PostResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.facade.facadecore.constant.ResponseMetaConstant.*;
import static com.facade.facadecore.constant.RestEndpoint.REST_V1_POSTS;

/**
 * @author Puck Wang
 * @project Blog
 * @created 1/22/2024
 */

@RestController("ArticleController")
@RequestMapping(REST_V1_POSTS)
@AllArgsConstructor
@Slf4j
public class PostControllerImpl implements PostController {

    private final PostManager postManager;
    private final PostMapper articleMapper;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> get(@PathVariable(name = "id") UUID id) {
        log.debug("Fetching article with id: {}", id);

        PostBO postBO = postManager.get(id);
        PostDTO postDTO = articleMapper.mapToDTO(postBO);

        log.debug("Fetched article: {}", postDTO);

        return ResponseEntity.ok(new PostResponse(postDTO, Map.of(CATEGORY_META, boListToMetaMap(List.of(postBO)))));
    }

    @Override
    @GetMapping("/list")
    public ResponseEntity<PostResponses> list(
            @RequestParam(name = "tagNames", required = false) List<String> tagNames,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
        List<PostBO> postBOList = postManager.list(true, tagNames);
        List<PostDTO> postDTOList = postBOList.stream().map(articleMapper::mapToDTO).toList();
        return ResponseEntity.ok(new PostResponses(postDTOList, Map.of(CATEGORY_META, boListToMetaMap(postBOList))));
    }

    @Override
    @GetMapping("/admin/list")
    public ResponseEntity<PostResponses> adminList(@RequestParam(name = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                                   @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
        List<PostBO> postBOList = postManager.list(false, List.of());
        List<PostDTO> postDTOList = postBOList.stream().map(articleMapper::mapToDTO).toList();
        return ResponseEntity.ok(new PostResponses(postDTOList, Map.of(CATEGORY_META, boListToMetaMap(postBOList))));
    }

    @Override
    @PostMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    public ResponseEntity<PostResponse> create(@RequestBody PostDTO article) {
        log.info("Creating article: {}", article);

        PostBO postBO = articleMapper.mapToBO(article);
        PostBO savedPostBO = postManager.create(postBO);
        PostDTO savedPostDTO = articleMapper.mapToDTO(savedPostBO);

        log.info("Created article: {}", savedPostDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPostDTO.getId())
                .toUri();
        return ResponseEntity.created(location).body(new PostResponse(savedPostDTO));
    }

    @Override
    @PutMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.PUT)
    public ResponseEntity<PostResponse> update(@PathVariable(name = "id") UUID id, @RequestBody PostDTO article) {
        log.debug("Updating article: {}", article);

        PostBO postBO = articleMapper.mapToBO(article);
        PostBO updatedPostBO = postManager.update(postBO);
        PostDTO updatedPostDTO = articleMapper.mapToDTO(updatedPostBO);

        log.debug("Updated article: {}", updatedPostDTO);

        return ResponseEntity.ok(new PostResponse(updatedPostDTO));
    }

    @Override
    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable(name = "id") UUID id) {
        log.debug("Deleting article with id:{}", id);

        postManager.delete(id);

        log.debug("Deleted article with id:{}", id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/batch")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@RequestBody List<UUID> ids) {
        log.debug("Deleting article with ids:{}", ids);

        postManager.batchDelete(ids);

        log.debug("Deleted article with ids:{}", ids);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/export")
    public ResponseEntity<PostResponses> export(@RequestParam(name = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                                @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
        List<PostBO> postBOList = postManager.list(false);
        List<PostDTO> postDTOList = postBOList.stream().map(articleMapper::mapToDTO).toList();
        return ResponseEntity.ok(new PostResponses(postDTOList, boListToMetaMap(postBOList)));
    }

    private Map<?, ?> boListToMetaMap(List<PostBO> list) {
        return list.stream().map((bo) -> Map.of(META_ID, bo.getId(), META_LIKES, bo.getMetrics().getLikes(),
                META_VIEWS, bo.getMetrics().getViews())).collect(Collectors.groupingBy((e) -> e.get(META_ID)));
    }
}
