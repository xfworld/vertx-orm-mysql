package com.github.longdt.vertxorm.repository.query;

public class NotEqual<E> extends SingleQuery<E> {

    public NotEqual(String fieldName, Object value) {
        super(fieldName, "`" + fieldName + "`!=?", value);
    }
}
