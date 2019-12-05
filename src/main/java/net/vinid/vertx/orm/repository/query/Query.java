package net.vinid.vertx.orm.repository.query;

import io.vertx.core.json.JsonArray;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface Query<E> {

    String getConditionSql();

    JsonArray getConditionParams();

    @SuppressWarnings({"unchecked"})
    default Query<E> orderBy(Order<E>... orders) {
        return orderBy(Arrays.asList(orders));
    }

    default Query<E> orderBy(Order<E> order) {
        return orderBy(Collections.singletonList(order));
    }

    Query<E> orderBy(List<Order<E>> orders);

    List<Order<E>> orderBy();

    Query<E> limit(int limit);

    int limit();

    Query<E> offset(long offset);

    long offset();
}