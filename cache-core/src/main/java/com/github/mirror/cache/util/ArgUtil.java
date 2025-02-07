
package com.github.mirror.cache.util;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

public final class ArgUtil {
    private ArgUtil() {
    }

    public static void notNull(Object object, String name) {
        if (null == object) {
            throw new IllegalArgumentException(name + " can not be null!");
        }
    }

    public static void notNull(Object object, String name, String errMsg) {
        if (null == object) {
            String errorInfo = String.format("%s %s", name, errMsg);
            throw new IllegalArgumentException(errorInfo);
        }
    }

    public static void notEmpty(String string, String name) {
        if (StrUtil.isEmpty(string)) {
            throw new IllegalArgumentException(name + " can not be null!");
        }
    }

    public static void equals(Object except, Object real, String msg) {
        if (ObjectUtil.notEqual(except, real)) {
            String errorMsg = buildErrorMsg(except, real, msg);
            throw new IllegalArgumentException(errorMsg);
        }
    }

    public static boolean isEqualsLen(String string, int len) {
        if (StrUtil.isEmpty(string)) {
            return 0 == len;
        } else {
            return string.length() == len;
        }
    }

    public static boolean isNotEqualsLen(String string, int len) {
        return !isEqualsLen(string, len);
    }

    public static boolean isFitMaxLen(String string, int maxLen) {
        if (StrUtil.isEmpty(string)) {
            return 0 <= maxLen;
        } else {
            return string.length() <= maxLen;
        }
    }

    public static boolean isNotFitMaxLen(String string, int maxLen) {
        return !isFitMaxLen(string, maxLen);
    }

    public static boolean isFitMinLen(String string, int minLen) {
        if (StrUtil.isEmpty(string)) {
            return 0 >= minLen;
        } else {
            return string.length() >= minLen;
        }
    }

    public static boolean isNotFitMinLen(String string, int minLen) {
        return !isFitMinLen(string, minLen);
    }

    public static Boolean isNumber(String number) {
        if (ObjectUtil.isNotNull(number)) {
            try {
                new BigDecimal(number);
                return true;
            } catch (Exception var2) {
                return false;
            }
        } else {
            return true;
        }
    }

    public static Boolean isNotNumber(String number) {
        return !isNumber(number);
    }

    public static Boolean isMatchesRegex(String string, String regex) {
        return null != string ? string.matches(regex) : true;
    }

    public static Boolean isNotMatchesRegex(String string, String regex) {
        return !isMatchesRegex(string, regex);
    }

    private static String buildErrorMsg(Object except, Object real, String msg) {
        String resultMsg = msg;
        if (StrUtil.isEmpty(resultMsg)) {
            resultMsg = "与期望值不符合!";
        }

        return String.format("Except:<%s>, Real:<%s>, Msg:<%s>", except, real, resultMsg);
    }

    public static void positive(int number, String paramName) {
        if (number <= 0) {
            throw new IllegalArgumentException(paramName + " must be > 0!");
        }
    }

    public static void notNegative(int number, String paramName) {
        if (number < 0) {
            throw new IllegalArgumentException(paramName + " must be >= 0!");
        }
    }

    public static void positive(long number, String paramName) {
        if (number <= 0L) {
            throw new IllegalArgumentException(paramName + " must be > 0!");
        }
    }

    public static void notNegative(long number, String paramName) {
        if (number < 0L) {
            throw new IllegalArgumentException(paramName + " must be >= 0!");
        }
    }

    public static void positive(double number, String paramName) {
        if (number < 0.0) {
            throw new IllegalArgumentException(paramName + " must be > 0!");
        }
    }

    public static void notNegative(double number, String paramName) {
        if (number < 0.0) {
            throw new IllegalArgumentException(paramName + " must be >= 0!");
        }
    }

    public static void assertTrue(boolean condition, String name) {
        if (!condition) {
            throw new IllegalArgumentException(name + " excepted true but is false!");
        }
    }

    public static void assertFalse(boolean condition, String name) {
        if (condition) {
            throw new IllegalArgumentException(name + " excepted false but is true!");
        }
    }

    public static void notEmpty(Object[] array, String name) {
        if (ArrayUtil.isEmpty(array)) {
            throw new IllegalArgumentException(name + " excepted is not empty!");
        } else {
            Object[] var2 = array;
            int var3 = array.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Object object = var2[var4];
                notNull(object, name + " element ");
            }

        }
    }

    public static void notEmpty(Collection<?> collection, String name) {
        if (CollectionUtil.isEmpty(collection)) {
            throw new IllegalArgumentException(name + " excepted is not empty!");
        } else {
            Iterator var2 = collection.iterator();

            while(var2.hasNext()) {
                Object object = var2.next();
                notNull(object, name + " element ");
            }

        }
    }

    /** @deprecated */
    @Deprecated
    public static void gt(long actual, long expected) {
        gt("", actual, expected);
    }

    public static void gt(String paramName, long actual, long expected) {
        if (actual <= expected) {
            throw new IllegalArgumentException("[" + paramName + "] actual is <" + actual + ">, expected is gt " + expected);
        }
    }

    public static void gte(String paramName, long actual, long expected) {
        if (actual < expected) {
            throw new IllegalArgumentException("[" + paramName + "] actual is <" + actual + ">, expected is gte " + expected);
        }
    }

    public static void lt(String paramName, long actual, long expected) {
        if (actual >= expected) {
            throw new IllegalArgumentException("[" + paramName + "] actual is <" + actual + ">, expected is lt " + expected);
        }
    }

    public static void lte(String paramName, long actual, long expected) {
        if (actual > expected) {
            throw new IllegalArgumentException("[" + paramName + "] actual is <" + actual + ">, expected is lte " + expected);
        }
    }
}
