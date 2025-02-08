package com.github.mirror.cache.core.support.listener.slow;

import com.alibaba.fastjson.JSON;
import com.github.mirror.cache.api.ICacheSlowListener;
import com.github.mirror.cache.api.ICacheSlowListenerContext;
import com.github.mirror.cache.util.LogUtil;
import com.github.mirror.cache.util.LogUtil.SimpleLogger;

/**
 * 慢日志监听类
 * @author binbin.hou
 * @since 0.0.9
 */
public class CacheSlowListener implements ICacheSlowListener {

    private static final SimpleLogger log = LogUtil.getLogger(CacheSlowListener.class);

    @Override
    public void listen(ICacheSlowListenerContext context) {
        log.warn("[Slow] methodName: {}, params: {}, cost time: {} ms",
                context.methodName(), JSON.toJSON(context.params()), context.costTimeMills());
    }

    @Override
    public long slowerThanMills() {
        return 1000L;
    }

}
