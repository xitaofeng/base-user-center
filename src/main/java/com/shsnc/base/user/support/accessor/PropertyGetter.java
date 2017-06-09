package com.shsnc.base.user.support.accessor;

public interface PropertyGetter<K,T> {
    
    K getValue(T entity);
    
}
