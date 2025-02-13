package com.github.mirror.cache.core.support.evict;

import com.github.mirror.cache.api.ICache;
import com.github.mirror.cache.api.ICacheEntry;
import com.github.mirror.cache.api.ICacheEvictContext;
import com.github.mirror.cache.core.model.CacheEntry;
import com.github.mirror.cache.core.support.struct.lru.ILruMap;
import com.github.mirror.cache.core.support.struct.lru.impl.LruMapDoubleList;
import com.github.mirror.cache.util.LogUtil;
import com.github.mirror.cache.util.LogUtil.SimpleLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * 淘汰策略-LRU-K
 *
 * 实现方式：使用 K 层 LRU 结构
 *  * @author mirror
 *  * @since 0.1.0
 */
public class CacheEvictLruK<K, V> extends AbstractCacheEvict<K, V> {

    private static final SimpleLogger log = LogUtil.getLogger(CacheEvictLruK.class);

    /**
     * LRU 队列列表，每层代表一次访问
     */
    private final List<ILruMap<K, V>> lruLevels;

    /**
     * LRU-K 参数，表示 K 次访问后才晋升
     */
    private final int k;

    public CacheEvictLruK(int k) {
        this.k = Math.max(2, k); // K 至少为 2
        this.lruLevels = new ArrayList<>(k);
        for (int i = 0; i < k; i++) {
            this.lruLevels.add(new LruMapDoubleList<>());
        }
    }

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K, V> result = null;
        final ICache<K, V> cache = context.cache();
        
        if (cache.size() >= context.size()) {
            ICacheEntry<K, V> evictEntry = null;
            
            // 先从最低层 LRU 淘汰
            for (ILruMap<K, V> lru : lruLevels) {
                if (!lru.isEmpty()) {
                    evictEntry = lru.removeEldest();
                    log.debug("从 LRU 层级中淘汰数据：{}", evictEntry);
                    break;
                }
            }

            if (evictEntry != null) {
                final K evictKey = evictEntry.key();
                V evictValue = cache.remove(evictKey);
                result = new CacheEntry<>(evictKey, evictValue);
            }
        }

        return result;
    }

    @Override
    public void updateKey(final K key) {
        for (int i = 0; i < k; i++) {
            ILruMap<K, V> lru = lruLevels.get(i);
            if (lru.contains(key)) {
                // 从当前 LRU 层级中移除
                lru.removeKey(key);
                
                // 如果还有更高一级的 LRU，则晋升
                if (i + 1 < k) {
                    lruLevels.get(i + 1).updateKey(key);
                    log.debug("key: {} 晋升到 LRU 层级 {}", key, i + 1);
                } else {
                    // 最高层级，保持不变
                    lru.updateKey(key);
                }
                return;
            }
        }

        // 如果不在任何 LRU 队列中，加入第 0 层 LRU
        lruLevels.get(0).updateKey(key);
        log.debug("key: {} 第一次访问，加入 LRU 层级 0", key);
    }

    @Override
    public void removeKey(final K key) {
        for (ILruMap<K, V> lru : lruLevels) {
            if (lru.contains(key)) {
                lru.removeKey(key);
                log.debug("key: {} 从 LRU 层级中移除", key);
                return;
            }
        }
    }
}
