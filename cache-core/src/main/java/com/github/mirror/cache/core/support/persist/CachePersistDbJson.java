package com.github.mirror.cache.core.support.persist;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.github.mirror.cache.api.ICache;
import com.github.mirror.cache.core.model.PersistRdbEntry;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 缓存持久化-db-基于 JSON
 * @author binbin.hou
 * @since 0.0.8
 */
public class CachePersistDbJson<K,V> extends CachePersistAdaptor<K,V> {

    /**
     * 数据库路径
     * @since 0.0.8
     */
    private final String dbPath;

    public CachePersistDbJson(String dbPath) {
        this.dbPath = dbPath;
    }

    /**
     * 持久化
     * key长度 key+value
     * 第一个空格，获取 key 的长度，然后截取
     * @param cache 缓存
     */
    @Override
    public void persist(ICache<K, V> cache) {
        Set<Map.Entry<K,V>> entrySet = cache.entrySet();
        File file = new File(System.getProperty("user.dir"), dbPath);
        // 创建文件
        FileUtil.touch(file);
        // 清空文件
        FileUtil.writeUtf8Lines(Collections.emptyList(),file);
//        FileUtil.truncate(dbPath);

        for(Map.Entry<K,V> entry : entrySet) {
            K key = entry.getKey();
            Long expireTime = cache.expire().expireTime(key);
            PersistRdbEntry<K,V> persistRdbEntry = new PersistRdbEntry<>();
            persistRdbEntry.setKey(key);
            persistRdbEntry.setValue(entry.getValue());
            persistRdbEntry.setExpire(expireTime);

            String line = JSON.toJSONString(persistRdbEntry);
//            com.github.houbb.heaven.util.io.FileUtil.writeString(dbPath,line, StandardOpenOption.APPEND);
            FileUtil.appendUtf8String(line + System.lineSeparator(),file);
        }
    }

    @Override
    public long delay() {
        return 3;
    }

    @Override
    public long period() {
        return 3;
    }

    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.MINUTES;
    }

}
