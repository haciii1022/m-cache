package com.github.mirror.cache.core.support.evict;

import com.github.mirror.cache.api.ICache;
import com.github.mirror.cache.api.ICacheEntry;
import com.github.mirror.cache.api.ICacheEvictContext;
import com.github.mirror.cache.core.model.CacheEntry;
import com.github.mirror.cache.util.LogUtil;
import com.github.mirror.cache.util.LogUtil.SimpleLogger;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Iterator;

/**
 * 淘汰策略 - SC (Second-Chance) 二次机会算法
 *
 * @author 
 * @since 0.1.0
 */
public class CacheEvictSC<K, V> extends AbstractCacheEvict<K, V> {

    private static final SimpleLogger log = LogUtil.getLogger(CacheEvictSC.class);

    /**
     * 使用 LinkedHashMap 维护访问顺序
     * Boolean 表示是否给予二次机会
     */
    private final Map<K, Boolean> cacheMap;

    public CacheEvictSC() {
        this.cacheMap = new LinkedHashMap<>();
    }

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K, V> result = null;
        final ICache<K, V> cache = context.cache();

        // 超过限制时，进行淘汰
        if (cache.size() >= context.size()) {
            Iterator<Map.Entry<K, Boolean>> iterator = cacheMap.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<K, Boolean> entry = iterator.next();
                K evictKey = entry.getKey();
                Boolean hasSecondChance = entry.getValue();

                if (hasSecondChance) {
                    // 给予二次机会，重置访问标记
                    cacheMap.put(evictKey, false);
                } else {
                    // 直接淘汰
                    V evictValue = cache.remove(evictKey);
                    iterator.remove(); // 从缓存 Map 中移除
                    log.debug("基于 SC（二次机会）算法淘汰 key：{}, value: {}", evictKey, evictValue);
                    result = new CacheEntry<>(evictKey, evictValue);
                    break;
                }
            }
        }

        return result;
    }

    /**
     * 访问 key 时，设置其访问标记为 true
     */
    @Override
    public void updateKey(final K key) {
        cacheMap.put(key, true);
    }

    /**
     * 移除 key
     */
    @Override
    public void removeKey(final K key) {
        cacheMap.remove(key);
    }
}
