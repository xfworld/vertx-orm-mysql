package net.vinid.vertx.orm.repository.impl;

import io.vertx.core.json.JsonArray;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RowMapperImpl<ID, E> implements RowMapper<ID, E> {
    private String tableName;
    private Supplier<E> supplier;
    private boolean pkAutoGen;
    private Function<E, ID> pkGetter;
    private FieldMapping<E, ?> pkConverter;
    private Function<ID, ?> pkGetConverter;
    private List<String> columnNames;
    private List<String> columnNamesPlusPK;
    private List<FieldMapping<E, ?>> fieldMappings;
    private List<FieldMapping<E, ?>> mappingsPlusPK;

    public RowMapperImpl(BuilderImpl<ID, E> builder) {
        this.tableName = builder.getTableName();
        this.supplier = builder.getSupplier();
        this.pkAutoGen = builder.isPkAutoGen();
        this.pkGetter = builder.getPkGetter();
        this.pkGetConverter = builder.getPkGetConverter();
        this.pkConverter = builder.getPkConverter();
        String pkName = builder.getPkName();
        columnNames = builder.getMappings().keySet().stream().filter(c -> !pkName.equals(c)).collect(Collectors.toList());

        columnNamesPlusPK = new ArrayList<>(columnNames.size() + 1);
        columnNamesPlusPK.addAll(columnNames);
        columnNamesPlusPK.add(pkName);

        fieldMappings = builder.getMappings().values().stream()
                .filter(m -> !pkName.equals(m.getFieldName())).collect(Collectors.toList());
        mappingsPlusPK = new ArrayList<>(fieldMappings.size() + 1);
        mappingsPlusPK.addAll(fieldMappings);
        mappingsPlusPK.add(pkConverter);
    }

    public String tableName() {
        return tableName;
    }

    public E newInstance() {
        return supplier.get();
    }

    public String pkName() {
        return pkConverter.getFieldName();
    }

    public boolean isPkAutoGen() {
        return pkAutoGen;
    }

    public List<String> getColumnNames() {
        return getColumnNames(true);
    }

    public List<String> getColumnNames(boolean includePK) {
        return includePK ? columnNamesPlusPK : columnNames;
    }

    public List<FieldMapping<E, ?>> getFieldMappings() {
        return getMappings(true);
    }

    public List<FieldMapping<E, ?>> getMappings(boolean includePK) {
        return includePK ? mappingsPlusPK : fieldMappings;
    }

    public ID getId(E entity) {
        return pkGetter.apply(entity);
    }

    public Object id2DbValue(ID id) {
        return pkGetConverter.apply(id);
    }

    public void setId(E entity, Object id) {
        pkConverter.set(entity, id);
    }

    public E map(JsonArray rs) {
        E entity = supplier.get();
        int i = 0;
        for (FieldMapping<E, ?> m : getFieldMappings()) {
            m.set(entity, rs.getValue(i));
            ++i;
        }
        return entity;
    }

    public JsonArray toJsonArray(E entity) {
        return toJsonArray(entity,true);
    }

    public JsonArray toJsonArray(E entity, boolean includePK) {
        return getMappings(includePK).stream().map(m -> m.get(entity))
                .collect(JsonArray::new, (objects, o) -> {
                    if (o == null) {
                        objects.addNull();
                    } else {
                        objects.add(o);
                    }
                }, JsonArray::addAll);
    }

}
