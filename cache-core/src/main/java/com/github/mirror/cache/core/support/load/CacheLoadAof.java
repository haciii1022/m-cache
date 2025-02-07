package com.github.mirror.cache.core.support.load;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.github.mirror.cache.annotation.CacheInterceptor;
import com.github.mirror.cache.api.ICache;
import com.github.mirror.cache.api.ICacheLoad;
import com.github.mirror.cache.core.core.Cache;
import com.github.mirror.cache.core.model.PersistAofEntry;
import com.github.mirror.cache.util.LogUtil;
import com.github.mirror.cache.util.LogUtil.SimpleLogger;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 加载策略-AOF文件模式
 * @author binbin.hou
 * @since 0.0.10
 */
public class CacheLoadAof<K,V> implements ICacheLoad<K,V> {

    private static final SimpleLogger log = LogUtil.getLogger(CacheLoadAof.class);

    /**
     * 方法缓存
     *
     * 暂时比较简单，直接通过方法判断即可，不必引入参数类型增加复杂度。
     * @since 0.0.10
     */
    private static final Map<String, Method> METHOD_MAP = new HashMap<>();

    static {
        Method[] methods = Cache.class.getMethods();

        for(Method method : methods){
            CacheInterceptor cacheInterceptor = method.getAnnotation(CacheInterceptor.class);

            if(cacheInterceptor != null) {
                // 暂时
                if(cacheInterceptor.aof()) {
                    String methodName = method.getName();

                    METHOD_MAP.put(methodName, method);
                }
            }
        }

    }

    /**
     * 文件路径
     * @since 0.0.8
     */
    private final String dbPath;

    public CacheLoadAof(String dbPath) {
        this.dbPath = dbPath;
    }

    @Override
    public void load(ICache<K, V> cache) {
        File file = new File(System.getProperty("user.dir"), dbPath);
        if (!FileUtil.exist(file)) {
            log.info("[load] path: {} 文件不存在，直接返回", dbPath);
            return;
        }
        List<String> lines = FileUtil.readUtf8Lines(file);
//        List<String> lines = FileUtil.readAllLines(dbPath);
        log.info("[load] 开始处理 path: {}", dbPath);
        if(CollectionUtil.isEmpty(lines)) {
            log.info("[load] path: {} 文件内容为空，直接返回", dbPath);
            return;
        }

        for(String line : lines) {
            if(StrUtil.isEmpty(line)) {
                continue;
            }

            // 执行
            // 简单的类型还行，复杂的这种反序列化会失败
            PersistAofEntry entry = JSON.parseObject(line, PersistAofEntry.class);

            final String methodName = entry.getMethodName();
            final Object[] objects = entry.getParams();

            final Method method = METHOD_MAP.get(methodName);
            // 反射调用
            ReflectUtil.invoke(cache, method, objects);
        }
    }

}
