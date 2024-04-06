package com.facade.facadecore.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Puck Wang
 * @project Blog
 * @created 3/21/2024
 */

@Getter
@Component
public class GsonUtils {

    private final Gson gson = new Gson();

    public String toJson(Object object) {
        return this.gson.toJson(object);
    }

    public <T> T fromJsonIfPresent(String json, Class<T> classOfT) {
        return StringUtils.isEmpty(json) ? null : this.gson.fromJson(json, classOfT);
    }

    public <T> T fromJson(JsonElement json, Class<T> classOfT) {
        return this.gson.fromJson(json, classOfT);
    }

    public JsonElement getAsJsonObject(Object jsonElement) {
        return this.gson.toJsonTree(jsonElement).getAsJsonObject();
    }

    public <T> List convertJsonTreeToList(List redisObjects, Class<T> clazz) {
        return CollectionUtils.isNotEmpty(redisObjects) ?
                redisObjects.stream().map(this::getAsJsonObject).map((ele) -> this.fromJson((JsonElement) ele, clazz)).toList() :
                List.of();
    }

}
