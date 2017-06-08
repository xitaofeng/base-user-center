package com.shsnc.base.user.support;

import com.shsnc.base.util.config.BizException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * Created by houguangqiang on 2017/6/8.
 */
public abstract class Assert {
    public Assert() {
    }

    public static void isTrue(boolean expression, String message) throws BizException {
        if(!expression) {
            throw new BizException(message);
        }
    }

    public static void isNull(Object object, String message) throws BizException {
        if(object != null) {
            throw new BizException(message);
        }
    }

    public static void notNull(Object object, String message) throws BizException {
        if(object == null) {
            throw new BizException(message);
        }
    }

    public static void hasLength(String text, String message) throws BizException {
        if(!StringUtils.hasLength(text)) {
            throw new BizException(message);
        }
    }

    public static void hasText(String text, String message) throws BizException {
        if(!StringUtils.hasText(text)) {
            throw new BizException(message);
        }
    }

    public static void doesNotContain(String textToSearch, String substring, String message) throws BizException {
        if(StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch.contains(substring)) {
            throw new BizException(message);
        }
    }

    public static void notEmpty(Object[] array, String message) throws BizException {
        if(ObjectUtils.isEmpty(array)) {
            throw new BizException(message);
        }
    }

    public static void noNullElements(Object[] array, String message) throws BizException {
        if(array != null) {
            Object[] var2 = array;
            int var3 = array.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Object element = var2[var4];
                if(element == null) {
                    throw new BizException(message);
                }
            }
        }

    }

    public static void notEmpty(Collection<?> collection, String message) throws BizException {
        if(CollectionUtils.isEmpty(collection)) {
            throw new BizException(message);
        }
    }

    public static void notEmpty(Map<?, ?> map, String message) throws BizException {
        if(CollectionUtils.isEmpty(map)) {
            throw new BizException(message);
        }
    }

    public static void state(boolean expression, String message) throws BizException {
        if(!expression) {
            throw new BizException(message);
        }
    }

}
