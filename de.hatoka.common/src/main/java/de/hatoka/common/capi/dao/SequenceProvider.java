package de.hatoka.common.capi.dao;

public interface SequenceProvider
{
    /**
     * Creates new sequence for a given name
     *
     * @param name
     *            of sequence
     * @return next number of sequence
     */
    public UUIDGenerator create(String name);

    /**
     * Create a new sequence for a given name and last number, so the sequence can
     * start with the next number
     *
     * @param name
     * @param number
     */
    public UUIDGenerator create(String name, long number);
}