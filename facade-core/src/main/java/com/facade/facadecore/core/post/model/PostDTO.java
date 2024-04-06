package com.facade.facadecore.core.post.model;

import com.facade.facadecore.base.model.BaseDTO;
import com.facade.facadecore.core.metrics.model.MetricsDTO;
import com.facade.facadecore.core.tag.model.TagDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

/**
 * @author Puck Wang
 * @project Blog
 * @created 1/21/2024
 */

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@SuperBuilder
@JsonInclude
public class PostDTO extends BaseDTO {
    private UUID id;
    private String summary;
    @NotNull
    private String content;
    @NotNull
    private String title;
    private boolean published;
    private boolean pinned;
    @Builder.Default
    private List<TagDTO> tags = List.of();
    private MetricsDTO metrics;
}
