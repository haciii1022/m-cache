package com.github.mirror.cache.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    // 定义时间格式
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

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
            log("INFO", message, args);
        }

        public void debug(String message, Object... args) {
            log("DEBUG", message, args);
        }

        public void warn(String message, Object... args) {
            log("WARN", message, args);
        }

        public void error(String message, Object... args) {
            String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
            System.err.println("[" + timestamp + "][ERROR] " + clazz.getSimpleName() + ": " + format(message, args));
        }

        private void log(String level, String message, Object... args) {
            String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
            System.out.println("[" + timestamp + "][" + level + "] " + clazz.getSimpleName() + ": " + format(message, args));
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
