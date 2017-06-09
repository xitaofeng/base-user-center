package com.shsnc.base.user.support.accessor;

public interface PropertySetter<K,T> {

    void setValue(T entity, K value);
}
