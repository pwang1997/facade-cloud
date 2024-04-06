package com.facade.facadecore.base;

/**
 * @author Puck Wang
 * @project Blog
 * @created 4/1/2024
 */
public interface RedisAction<T> {
    String getRedisKeyById(T id);

    String getRedisKeyByPublished(boolean publishedOnly);

    void InvalidateRedisListKey();
}
