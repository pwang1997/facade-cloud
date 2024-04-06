package com.facade.facadecore.core.project.model;

import com.facade.facadecore.base.model.BaseBO;
import com.facade.facadecore.core.metrics.model.MetricsBO;
import com.facade.facadecore.core.tag.model.TagBO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Puck Wang
 * @project Blog
 * @created 3/11/2024
 */

@Entity(name = "projects")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProjectBO extends BaseBO {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title", columnDefinition = "text")
    private String title;

    @Column(name = "summary", columnDefinition = "text")
    private String summary;

    @Column(name = "demo_url", columnDefinition = "text")
    private String demoUrl;

    @Column(name = "sourceCode_url", columnDefinition = "text")
    private String sourceCodeUrl;

    @Column(name = "snapshot_image_url", columnDefinition = "text")
    private String snapshotImageUrl;

    @Column(name = "published", columnDefinition = "boolean default false")
    private boolean published;

    @Column(name = "pinned", columnDefinition = "boolean default false")
    private boolean pinned;

    @ManyToMany
    @JoinTable(name = "project_tag_assn", joinColumns = {@JoinColumn(name = "project_id")}, inverseJoinColumns = {@JoinColumn(name = "tag_id")},
            foreignKey = @ForeignKey(name = "FK_PROJECT_ID"), inverseForeignKey = @ForeignKey(name = "FK_TAG_ID"),
            indexes = {
                    @Index(name = "idx_project_tag_tagId", columnList = "tag_id")
            })
    private List<TagBO> tags;

    @OneToOne
    @JoinColumn(name = "project_metrics_id", referencedColumnName = "id")
    private MetricsBO metrics;

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        Class<?> oEffectiveClass = object instanceof HibernateProxy ? ((HibernateProxy) object).getHibernateLazyInitializer().getPersistentClass() : object.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ProjectBO projectBO = (ProjectBO) object;
        return getId() != null && Objects.equals(getId(), projectBO.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
