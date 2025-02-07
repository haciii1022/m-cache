package com.github.mirror.cache.core.support.listener.remove;

import com.github.mirror.cache.api.ICacheRemoveListener;
import com.github.mirror.cache.api.ICacheRemoveListenerContext;
import com.github.mirror.cache.util.LogUtil;
import com.github.mirror.cache.util.LogUtil.SimpleLogger;

/**
 * 默认的删除监听类
 * @author binbin.hou
 * @since 0.0.6
 */
public class CacheRemoveListener<K,V> implements ICacheRemoveListener<K,V> {

    private static final SimpleLogger log = LogUtil.getLogger(CacheRemoveListener.class);

    @Override
    public void listen(ICacheRemoveListenerContext<K, V> context) {
        log.debug("Remove key: {}, value: {}, type: {}",
                context.key(), context.value(), context.type());
    }

}
