package com.github.longdt.vertxorm.repository.query;

public class LessThan<O, A extends Comparable<A>> extends SingleQuery<O> {

    public LessThan(String fieldName, A value) {
        super(fieldName, "`" + fieldName + "` < ?", value);
    }
}
