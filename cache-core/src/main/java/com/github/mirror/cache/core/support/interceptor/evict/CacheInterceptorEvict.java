package com.github.mirror.cache.core.support.interceptor.evict;

import com.github.mirror.cache.api.ICacheEvict;
import com.github.mirror.cache.api.ICacheInterceptor;
import com.github.mirror.cache.api.ICacheInterceptorContext;
import com.github.mirror.cache.util.LogUtil;
import com.github.mirror.cache.util.LogUtil.SimpleLogger;

import java.lang.reflect.Method;

/**
 * 驱除策略拦截器
 *
 * @author binbin.hou
 * @since 0.0.11
 */
public class CacheInterceptorEvict<K,V> implements ICacheInterceptor<K, V> {

    private static final SimpleLogger log = LogUtil.getLogger(CacheInterceptorEvict.class);

    @Override
    public void before(ICacheInterceptorContext<K,V> context) {
    }

    @Override
    @SuppressWarnings("all")
    public void after(ICacheInterceptorContext<K,V> context) {
        ICacheEvict<K,V> evict = context.cache().evict();

        Method method = context.method();
        final K key = (K) context.params()[0];
        if("remove".equals(method.getName())) {
            evict.removeKey(key);
        } else {
            evict.updateKey(key);
        }
    }

}
