package com.github.mirror.cache.core.support.load;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.github.mirror.cache.api.ICache;
import com.github.mirror.cache.api.ICacheLoad;
import com.github.mirror.cache.core.model.PersistRdbEntry;
import com.github.mirror.cache.util.LogUtil;
import com.github.mirror.cache.util.LogUtil.SimpleLogger;

import java.io.File;
import java.util.List;

/**
 * 加载策略-文件路径
 *
 * @author binbin.hou
 * @since 0.0.8
 */
public class CacheLoadDbJson<K, V> implements ICacheLoad<K, V> {

    private static final SimpleLogger log = LogUtil.getLogger(CacheLoadDbJson.class);

    /**
     * 文件路径
     *
     * @since 0.0.8
     */
    private final String dbPath;

    public CacheLoadDbJson(String dbPath) {
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
        log.info("[load] 开始处理 path: {}", dbPath);
        if (CollectionUtil.isEmpty(lines)) {
            log.info("[load] path: {} 文件内容为空，直接返回", dbPath);
            return;
        }

        for (String line : lines) {
            if (StrUtil.isEmpty(line)) {
                continue;
            }

            // 执行
            // 简单的类型还行，复杂的这种反序列化会失败
            PersistRdbEntry<K, V> entry = JSON.parseObject(line, PersistRdbEntry.class);

            K key = entry.getKey();
            V value = entry.getValue();
            Long expire = entry.getExpire();

            cache.put(key, value);
            if (ObjectUtil.isNotNull(expire)) {
                cache.expireAt(key, expire);
            }
        }
        //nothing...
    }
}
