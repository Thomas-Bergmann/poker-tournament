package de.hatoka.common.capi.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.hatoka.common.capi.exceptions.DuplicateObjectException;
import de.hatoka.common.capi.exceptions.MandatoryParameterException;

public abstract class GenericDao<T> implements Dao<T>
{
    private final Map<String, T> data = new HashMap<>();
    private final Class<T> entityClass;

    public GenericDao(Class<T> entityClass)
    {
        this.entityClass = entityClass;
    }

    /**
     * Finds any object with given predicate
     *
     * @param predicate
     * @return matched object (null if no element is present)
     */
    protected T any(Predicate<T> predicate)
    {
        Optional<T> any = values().stream().filter(predicate).findAny();
        if (any.isPresent())
        {
            return any.get();
        }
        return null;
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

    protected Set<Entry<String, T>> entrySet()
    {
        return data.entrySet();
    }

    /**
     * Filters objects at repository
     *
     * @param predicate
     * @return collection of matching objects
     */
    protected Collection<T> filter(Predicate<T> predicate)
    {
        return values().stream().filter(predicate).collect(Collectors.toList());
    }

    @Override
    public T getById(String id)
    {
        if (id == null)
        {
            throw new MandatoryParameterException("id");
        }
        return data.get(id);
    }

    private String getType()
    {
        return getClass().getSimpleName();
    }

    @Override
    public void insert(T element)
    {
        String key = getId(element);
        if (key == null)
        {
            throw new IllegalStateException(getType() + " has no value for primary key.");
        }
        T existing = data.get(key);
        if (existing != null)
        {
            throw new DuplicateObjectException(getType() + " already exists");
        }
        data.put(key, element);
        return;
    }

    @Override
    public void remove(String key)
    {
        data.remove(key);
    }

    @Override
    public void remove(T element)
    {
        String key = getId(element);
        remove(key);
    }

    protected Collection<T> values()
    {
        return data.values();
    }
}
