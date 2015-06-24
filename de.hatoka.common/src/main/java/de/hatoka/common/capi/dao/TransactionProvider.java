package de.hatoka.common.capi.dao;

import java.util.concurrent.Callable;

import javax.persistence.EntityTransaction;

public interface TransactionProvider
{
    EntityTransaction get();

    default void runInTransaction(Runnable runnable)
    {
        EntityTransaction transaction = get();
        try
        {
            transaction.begin();
            runnable.run();
            transaction.commit();
        }
        finally
        {
            if (transaction.isActive())
            {
                transaction.rollback();
            }
        }
    }

    default <T> T runInTransaction(Callable<T> callable) throws Exception
    {
        T result = null;
        EntityTransaction transaction = get();
        try
        {
            transaction.begin();
            result = callable.call();
            transaction.commit();
        }
        finally
        {
            if (transaction.isActive())
            {
                transaction.rollback();
            }
        }
        return result;
    }
}
