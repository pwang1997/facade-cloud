package com.facade.facadecore.core.tag.model;

import com.facade.facadecore.base.model.BaseBO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

/**
 * @author Puck Wang
 * @project Blog
 * @created 1/24/2024
 */
@Entity(name = "tags")
@Table(name = "tags",
        indexes = {
                @Index(name = "idx_tag_name", columnList = "name")
        })
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TagBO extends BaseBO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        Class<?> oEffectiveClass = object instanceof HibernateProxy ? ((HibernateProxy) object).getHibernateLazyInitializer().getPersistentClass() : object.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        TagBO tagBO = (TagBO) object;
        return getId() != null && Objects.equals(getId(), tagBO.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
