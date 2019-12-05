package net.vinid.vertx.orm.repository.query;

public class Has<E> extends SingleQuery<E> {

    public Has(String fieldName) {
        super(fieldName, "`" + fieldName + "` is not null", QueryFactory.EMPTY_PARAMS);
    }
}
