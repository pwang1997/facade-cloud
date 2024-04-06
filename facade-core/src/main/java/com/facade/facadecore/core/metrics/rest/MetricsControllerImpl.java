package com.facade.facadecore.core.metrics.rest;

import com.facade.facadecore.core.metrics.SystemMetricsManager;
import com.facade.facadecore.core.metrics.model.SystemMetricsResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.facade.facadecore.constant.RestEndpoint.REST_V1_METRICS;

/**
 * @author Puck Wang
 * @project Blog
 * @created 3/25/2024
 */


@RestController
@RequestMapping(REST_V1_METRICS)
@AllArgsConstructor
@Slf4j
public class MetricsControllerImpl implements MetricsController {
    private final SystemMetricsManager systemMetricsManager;

    @Override
    @GetMapping
    public ResponseEntity<?> list(@RequestParam(name = "relatedToFetch") List<String> relatedToFetch) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (String taskStr : relatedToFetch) {
            String[] arr = taskStr.split(":");
            if (arr.length != 2) return ResponseEntity.badRequest().body("Unacceptable format");

            String target = arr[0], action = arr[1];
            futures.add(systemMetricsManager.doTargetAction(target, action));
        }

        CompletableFuture<?>[] futuresArray = futures.toArray(new CompletableFuture<?>[0]);

        CompletableFuture<List<?>> listFuture = CompletableFuture.allOf(futuresArray)
                .thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));

        final List<?> results = listFuture.join();

        Map<String, Object> resultMap = new HashMap<>();
        for (int i = 0; i < relatedToFetch.size(); i++) {
            Object result = results.get(i);
            String taskStr = relatedToFetch.get(i);
            resultMap.put(taskStr, result);
            systemMetricsManager.cacheTargetAction(taskStr, result);
        }

        return ResponseEntity.ok(new SystemMetricsResponse(resultMap));
    }
}
