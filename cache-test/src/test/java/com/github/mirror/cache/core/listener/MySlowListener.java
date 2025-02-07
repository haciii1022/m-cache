package com.github.mirror.cache.core.listener;

import com.github.mirror.cache.api.ICacheSlowListener;
import com.github.mirror.cache.api.ICacheSlowListenerContext;

/**
 * @author binbin.hou
 * @since 0.0.9
 */
public class MySlowListener implements ICacheSlowListener {

    @Override
    public void listen(ICacheSlowListenerContext context) {
        System.out.println("【慢日志】name: " + context.methodName());
    }

    @Override
    public long slowerThanMills() {
        return 0;
    }

}
