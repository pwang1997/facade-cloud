package com.facade.facadecore.base.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Puck Wang
 * @project Blog
 * @created 1/21/2024
 */


@Getter
@Setter
@MappedSuperclass
public abstract class BaseBO implements Serializable {

    //    @CreatedDate
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    //    @LastModifiedDate
//    @CreatedDate
//    @CreationTimestamp
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;
}
