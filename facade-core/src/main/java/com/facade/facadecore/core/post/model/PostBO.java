package com.facade.facadecore.core.post.model;

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
 * @created 1/21/2024
 */

@Entity(name = "posts")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PostBO extends BaseBO {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "content", columnDefinition = "text")
    private String content;

    @Column(name = "summary", columnDefinition = "text")
    private String summary;

    @Column(name = "title", columnDefinition = "varchar(255)", nullable = false)
    private String title;

    @Column(name = "published", columnDefinition = "boolean default false")
    private boolean published;

    @Column(name = "pinned", columnDefinition = "boolean default false")
    private boolean pinned;

    @ManyToMany
    @JoinTable(name = "post_tag_assn", joinColumns = {@JoinColumn(name = "post_id")}, inverseJoinColumns = {@JoinColumn(name = "tag_id")},
            foreignKey = @ForeignKey(name = "FK_POST_ID"), inverseForeignKey = @ForeignKey(name = "FK_TAG_ID"),
            indexes = {
                    @Index(name = "idx_post_tag_tagId", columnList = "tag_id")
            })
    private List<TagBO> tags;

    @OneToOne
    @JoinColumn(name = "post_metrics_id", referencedColumnName = "id")
    private MetricsBO metrics;

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        Class<?> oEffectiveClass = object instanceof HibernateProxy ? ((HibernateProxy) object).getHibernateLazyInitializer().getPersistentClass() : object.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        PostBO postBO = (PostBO) object;
        return getId() != null && Objects.equals(getId(), postBO.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
