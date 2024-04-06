package com.facade.facadecore.base;

import java.util.concurrent.CompletableFuture;

/**
 * @author Puck Wang
 * @project Blog
 * @created 3/25/2024
 */
public interface AsyncSQLExecutor {

    CompletableFuture<?> doAction(String action);
}
