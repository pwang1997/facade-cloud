package com.facade.facadecore.core.metrics.rest;

import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author Puck Wang
 * @project Blog
 * @created 3/25/2024
 */
public interface MetricsController {

    ResponseEntity<?> list(List<String> relatedToFetch);
}
