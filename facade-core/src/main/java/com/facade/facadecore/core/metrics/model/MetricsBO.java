package com.facade.facadecore.core.metrics.model;

import com.facade.facadecore.base.model.BaseBO;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author Puck Wang
 * @project Blog
 * @created 3/23/2024
 */

@EqualsAndHashCode(callSuper = true)
@Entity(name = "metrics")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MetricsBO extends BaseBO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "views", columnDefinition = "bigint default 0", nullable = false)
    private Long views;

    @Column(name = "likes", columnDefinition = "bigint default 0", nullable = false)
    private Long likes;

    public MetricsBO incrementViews() {
        this.incrementViews(1L);
        return this;
    }

    public MetricsBO incrementViews(Long increment) {
        this.setViews(this.getViews() + increment);
        return this;
    }

    public MetricsBO incrementLikes(Long increment) {
        this.setLikes(this.getLikes() + increment);
        return this;
    }

    public MetricsBO incrementLikes() {
        return incrementLikes(1L);
    }
}
