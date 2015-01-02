package de.hatoka.common.capi.dao;

import javax.persistence.EntityTransaction;

public interface TransactionProvider
{
    EntityTransaction get();
}
