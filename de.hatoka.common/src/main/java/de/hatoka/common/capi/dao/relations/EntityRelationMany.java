package de.hatoka.common.capi.dao.relations;

public interface EntityRelationMany<M>
{
    void add(M object);
    void remove(M object);
}
