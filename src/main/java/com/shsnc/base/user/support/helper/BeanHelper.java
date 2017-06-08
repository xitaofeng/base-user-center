package com.shsnc.base.user.support.helper;

import com.shsnc.base.user.support.accessor.PropertyGetter;

import java.util.*;

/**
 * Created by houguangqiang on 2017/6/8.
 */
public class BeanHelper {

    public <T, E> List<T> extractToList(Collection<E> collection, PropertyGetter<T, E> getter){
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

    public <T, E> Set<T> extractToSet(Collection<E> collection, PropertyGetter<T, E> getter){
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

    public <T, E> Map<T, E> extractToMap(Collection<E> collection, PropertyGetter<T, E> getter){
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
}
