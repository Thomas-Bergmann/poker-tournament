package de.hatoka.account.capi.business;

import java.util.List;

/**
 * Repository which gives access to accounts.
 */
public interface AccountBORepository
{
    /**
     * Creates an account for given email
     *
     * @param email
     * @return
     */
    AccountBO createAccountBO();

    /**
     * Resolves an account via identifier
     *
     * @param id
     * @return account (null - if account not exist)
     */
    AccountBO getAccountBOById(String id);

    /**
     * @return the accounts owned by user
     */
    List<AccountBO> getAccountBOs();
}
