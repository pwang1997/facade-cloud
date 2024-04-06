package com.facade.facadecore.core.project.model;

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
 * @created 3/11/2024
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@SuperBuilder
@JsonInclude
public class ProjectDTO extends BaseDTO {
    private UUID id;
    @NotNull
    private String title;
    private String summary;
    private String demoUrl;
    private String sourceCodeUrl;
    private String snapshotImageUrl;
    private boolean published;
    private boolean pinned;
    @Builder.Default
    private List<TagDTO> tags = List.of();
    private MetricsDTO metrics;
}
