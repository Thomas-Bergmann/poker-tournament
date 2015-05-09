package de.hatoka.common.capi.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class GenericJPADao<T extends IdentifiableEntity> implements Dao<T>
{
    @Inject
    private EntityManagerProvider entityManagerProvider;

    private final Class<T> entityClass;

    public GenericJPADao(Class<T> entityClass)
    {
        this.entityClass = entityClass;
    }

    @Override
    public T create()
    {
        try
        {
            return entityClass.newInstance();
        }
        catch(InstantiationException | IllegalAccessException e)
        {
            throw new RuntimeException("Can't instantiate class without parameter: " + entityClass.getCanonicalName(),
                            e);
        }
    }

    protected TypedQuery<T> createNamedQuery(String queryName)
    {
        return getManager().createNamedQuery(queryName, entityClass);
    }

    @Override
    public T getById(String id)
    {
        return getManager().find(entityClass, id);
    }

    @Override
    public String getId(T element)
    {
        return element.getId();
    }

    private EntityManager getManager()
    {
        return entityManagerProvider.get();
    }

    @Override
    public void insert(T element)
    {
        getManager().persist(element);
    }

    @Override
    public void remove(String id)
    {
        T element = getById(id);
        if (element != null)
        {
            remove(element);
        }
    }

    @Override
    public void remove(T element)
    {
        getManager().remove(element);
    }

    protected T getOptionalResult(TypedQuery<T> query)
    {
        List<T> list = query.getResultList();
        return list.isEmpty() ? null : list.iterator().next();
    }

}
