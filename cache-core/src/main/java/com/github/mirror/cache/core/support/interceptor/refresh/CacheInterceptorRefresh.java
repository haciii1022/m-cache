package com.github.mirror.cache.core.support.interceptor.refresh;

import com.github.mirror.cache.api.ICache;
import com.github.mirror.cache.api.ICacheInterceptor;
import com.github.mirror.cache.api.ICacheInterceptorContext;
import com.github.mirror.cache.util.LogUtil;
import com.github.mirror.cache.util.LogUtil.SimpleLogger;

/**
 * 刷新
 *
 * @author binbin.hou
 * @since 0.0.5
 */
public class CacheInterceptorRefresh<K,V> implements ICacheInterceptor<K, V> {

    private static final SimpleLogger log = LogUtil.getLogger(CacheInterceptorRefresh.class);

    @Override
    public void before(ICacheInterceptorContext<K,V> context) {
        log.debug("Refresh start");
        final ICache<K,V> cache = context.cache();
        cache.expire().refreshExpire(cache.keySet());
    }

    @Override
    public void after(ICacheInterceptorContext<K,V> context) {
    }

}
