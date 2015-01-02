package de.hatoka.common.capi.dao;

public interface Dao<T>
{
    /**
     * Creates an element, but doesn't add this element
     *
     * @return
     */
    public T create();

    /**
     * Resolves an account via identifier
     *
     * @param id
     * @return
     */
    public T getById(String id);

    /**
     * @param element
     *            (mandatory)
     * @return the element by primary key
     */
    public String getId(T element);

    /**
     *
     * @param element
     * @throws IllegalStateException
     *             if element has no value for primary key
     */
    public void insert(T element);

    /**
     * Removes the element via primary key
     *
     * @param id
     */
    public void remove(String id);

    /**
     * Removes the element
     *
     * @param element
     */
    public void remove(T element);
}