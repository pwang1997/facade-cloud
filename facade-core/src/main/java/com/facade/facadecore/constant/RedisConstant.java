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

  public final static String REDIS_KEY_VIEWS = "views";
  public final static String REDIS_KEY_ID = "id";
  public final static String REDIS_KEY_VIEW_ACCESS = "publishedOnly";

  public final static String REDIS_KEY_PROJECT_VIEWS = REDIS_KEY_PROJECT + "-" + REDIS_KEY_VIEWS;
  public final static String REDIS_KEY_PROJECT_ID = REDIS_KEY_PROJECT + "-" + REDIS_KEY_ID + "-";
  public final static String REDIS_KEY_PROJECT_VIEW_ACCESS =
      REDIS_KEY_PROJECT + "-" + REDIS_KEY_VIEW_ACCESS + "-";

  public final static String REDIS_KEY_POST_VIEWS = REDIS_KEY_POST + "-" + REDIS_KEY_VIEWS;
  public final static String REDIS_KEY_POST_ID = REDIS_KEY_POST + "-" + REDIS_KEY_ID + "-";

  public final static String REDIS_KEY_POST_VIEW_ACCESS =
      REDIS_KEY_POST + "-" + REDIS_KEY_VIEW_ACCESS + "-";

}
