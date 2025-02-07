package com.github.mirror.cache.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mirror
 * @date 2025/2/7
 */
public class LogUtil {

    private LogUtil() {
        // 禁止外部实例化
    }

    // 用于存储不同类的日志实例
    private static final Map<Class<?>, SimpleLogger> loggers = new HashMap<>();

    // 获取日志实例
    public static SimpleLogger getLogger(Class<?> clazz) {
        return loggers.computeIfAbsent(clazz, SimpleLogger::new);
    }

    // 简单的日志类
    public static class SimpleLogger {
        private final Class<?> clazz;

        public SimpleLogger(Class<?> clazz) {
            this.clazz = clazz;
        }

        public void info(String message, Object... args) {
            System.out.println("[INFO] " + clazz.getSimpleName() + ": " + format(message, args));
        }

        public void error(String message, Object... args) {
            System.err.println("[ERROR] " + clazz.getSimpleName() + ": " + format(message, args));
        }

        public void debug(String message, Object... args) {
            System.out.println("[DEBUG] " + clazz.getSimpleName() + ": " + format(message, args));
        }

        public void warn(String message, Object... args) {
            System.out.println("[WARN] " + clazz.getSimpleName() + ": " + format(message, args));
        }

        private String format(String message, Object... args) {
            if (args == null) {
                return message;
            }
            for (Object arg : args) {
                message = message.replaceFirst("\\{}", arg == null ? "null" : arg.toString());
            }
            return message;
        }
    }
}
