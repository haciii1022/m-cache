package com.github.mirror.cache.core.support.map;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author binbin.hou
 * @since 0.0.3
 */
public final class Maps {

    private Maps(){}

    /**
     * hashMap 实现策略
     * @param <K> key
     * @param <V> value
     * @return map 实现
     * @since 0.0.3
     */
    public static <K,V> Map<K,V> hashMap() {
        return new HashMap<>();
    }
    public static <K,V> Map<K,V> concurrentHashMap() {
        return new ConcurrentHashMap<>();
    }

}
