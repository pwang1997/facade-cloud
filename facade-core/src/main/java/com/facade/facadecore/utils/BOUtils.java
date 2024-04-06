package com.facade.facadecore.utils;

import com.facade.facadecore.base.model.BaseBO;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * @author Puck Wang
 * @project Blog
 * @created 3/23/2024
 */

@Component
public class BOUtils {

    public void setDirtyFields(BaseBO bo) {
        if (bo.getCreatedAt() == null) {
            bo.setCreatedAt(Timestamp.from(Instant.now()));
        }
        
        bo.setUpdatedAt(Timestamp.from(Instant.now()));
    }
}
