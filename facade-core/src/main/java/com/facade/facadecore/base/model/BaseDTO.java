package com.facade.facadecore.base.model;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * @author Puck Wang
 * @project Blog
 * @created 1/21/2024
 */

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseDTO {
    private Timestamp createdAt;
    @Builder.Default
    private Timestamp updatedAt = Timestamp.from(Instant.now());
}
