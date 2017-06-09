package com.shsnc.base.user.support.helper;

import com.shsnc.base.user.support.accessor.PropertyGetter;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * Created by houguangqiang on 2017/6/8.
 */
public class BeanHelper {

    public static  <T, E> List<T> extractToList(Collection<E> collection, PropertyGetter<T, E> getter){
        List<T> list = new ArrayList<T>();
        if(collection != null){
            for(E item : collection){
                T value = getter.getValue(item);
                if(value != null){
                    list.add(value);
                }
            }
        }
        return list;
    }

    public static  <T, E> Set<T> extractToSet(Collection<E> collection, PropertyGetter<T, E> getter){
        Set<T> set = new HashSet<T>();
        if(collection != null){
            for(E item : collection){
                T value = getter.getValue(item);
                if(value != null){
                    set.add(value);
                }
            }
        }
        return set;
    }

    public static  <T, E> Map<T, E> extractToMap(Collection<E> collection, PropertyGetter<T, E> getter){
        Map<T, E> map = new HashMap<T, E>();
        if(collection != null){
            for(E item : collection){
                T value = getter.getValue(item);
                if(value != null){
                    map.put(value, item);
                }
            }
        }
        return map;
    }


    /**
     * 用source填充target为null的属性
     * @param source
     * @param target
     */
    public static <T> void populateNullProperties(T source, T target){
        BeanWrapper targetBeanWarpper = new BeanWrapperImpl(target);
        BeanWrapper sourceBeanWarpper = new BeanWrapperImpl(source);

        PropertyDescriptor[] pds = targetBeanWarpper.getPropertyDescriptors();
        for(PropertyDescriptor pd : pds){
            String property = pd.getName();
            if(sourceBeanWarpper.isReadableProperty(property) && targetBeanWarpper.isWritableProperty(property)){
                Object value = targetBeanWarpper.getPropertyValue(property);
                if(value == null){
                    value = sourceBeanWarpper.getPropertyValue(property);
                    targetBeanWarpper.setPropertyValue(property, value);
                }
            }
        }
    }
}
