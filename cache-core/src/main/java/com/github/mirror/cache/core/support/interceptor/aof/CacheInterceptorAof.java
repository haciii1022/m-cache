package com.github.mirror.cache.core.support.interceptor.aof;

import com.alibaba.fastjson.JSON;
import com.github.mirror.cache.api.ICache;
import com.github.mirror.cache.api.ICacheInterceptor;
import com.github.mirror.cache.api.ICacheInterceptorContext;
import com.github.mirror.cache.api.ICachePersist;
import com.github.mirror.cache.core.model.PersistAofEntry;
import com.github.mirror.cache.core.support.persist.CachePersistAof;
import com.github.mirror.cache.util.LogUtil;
import com.github.mirror.cache.util.LogUtil.SimpleLogger;

/**
 * 顺序追加模式
 *
 * AOF 持久化到文件，暂时不考虑 buffer 等特性。
 * @author binbin.hou
 * @since 0.0.10
 */
public class CacheInterceptorAof<K,V> implements ICacheInterceptor<K, V> {

    private static final SimpleLogger log = LogUtil.getLogger(CacheInterceptorAof.class);

    @Override
    public void before(ICacheInterceptorContext<K,V> context) {
    }

    @Override
    public void after(ICacheInterceptorContext<K,V> context) {
        // 持久化类
        ICache<K,V> cache = context.cache();
        ICachePersist<K,V> persist = cache.persist();

        if(persist instanceof CachePersistAof) {
            CachePersistAof<K,V> cachePersistAof = (CachePersistAof<K,V>) persist;

            String methodName = context.method().getName();
            PersistAofEntry aofEntry = PersistAofEntry.newInstance();
            aofEntry.setMethodName(methodName);
            aofEntry.setParams(context.params());

            String json = JSON.toJSONString(aofEntry);

            // 直接持久化
            log.debug("AOF 开始追加文件内容：{}", json);
            cachePersistAof.append(json);
            log.debug("AOF 完成追加文件内容：{}", json);
        }
    }

}
