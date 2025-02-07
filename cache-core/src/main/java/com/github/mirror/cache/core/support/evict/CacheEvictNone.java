package com.github.mirror.cache.core.support.evict;

import com.github.mirror.cache.api.ICacheEntry;
import com.github.mirror.cache.api.ICacheEvictContext;

/**
 * 丢弃策略
 * @author binbin.hou
 * @since 0.0.2
 */
public class CacheEvictNone<K,V> extends AbstractCacheEvict<K,V> {

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        return null;
    }

}
