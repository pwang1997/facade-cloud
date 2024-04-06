package com.facade.facadecore.constant;

/**
 * @author Puck Wang
 * @project Blog
 * @created 3/20/2024
 */
public class RedisConstant {
    public final static Long DEFAULT_TTL = 3600L;

    public final static String REDIS_KEY_POST = "posts";
    public final static String REDIS_KEY_TAG = "tags";
    public final static String REDIS_KEY_PROJECT = "projects";
    public final static String REDIS_KEY_METRICS = "metrics";
}
