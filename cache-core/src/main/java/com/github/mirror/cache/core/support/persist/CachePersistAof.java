package com.github.mirror.cache.core.support.persist;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.github.mirror.cache.api.ICache;
import com.github.mirror.cache.util.LogUtil;
import com.github.mirror.cache.util.LogUtil.SimpleLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 缓存持久化-AOF 持久化模式
 *
 * @author binbin.hou
 * @since 0.0.10
 */
public class CachePersistAof<K, V> extends CachePersistAdaptor<K, V> {

    private static final SimpleLogger log = LogUtil.getLogger(CachePersistAof.class);

    /**
     * 缓存列表
     *
     * @since 0.0.10
     */
    private final List<String> bufferList = new ArrayList<>();

    /**
     * 数据持久化路径
     *
     * @since 0.0.10
     */
    private final String dbPath;

    public CachePersistAof(String dbPath) {
        this.dbPath = dbPath;
    }

    /**
     * 持久化
     * key长度 key+value
     * 第一个空格，获取 key 的长度，然后截取
     *
     * @param cache 缓存
     */
    @Override
    public void persist(ICache<K, V> cache) {
        log.info("开始 AOF 持久化到文件 {}", bufferList);
        File file = new File(System.getProperty("user.dir"), dbPath);
        // 1. 创建文件
        FileUtil.touch(file);
//        if(!FileUtil.exists(dbPath)) {
//            FileUtil.createFile(dbPath);
//        }
        // 2. 持久化追加到文件中
        FileUtil.appendUtf8Lines(bufferList, file);

        // 3. 清空 buffer 列表
        bufferList.clear();
        log.info("完成 AOF 持久化到文件 {}", bufferList);
    }

    @Override
    public long delay() {
        return 1;
    }

    @Override
    public long period() {
        return 1;
    }

    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }

    /**
     * 添加文件内容到 buffer 列表中
     *
     * @param json json 信息
     * @since 0.0.10
     */
    public void append(final String json) {
        if(StrUtil.isNotEmpty(json)) {
            bufferList.add(json);
        }
    }

}
