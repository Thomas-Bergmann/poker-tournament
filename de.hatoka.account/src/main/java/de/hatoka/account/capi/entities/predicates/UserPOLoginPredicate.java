package de.hatoka.account.capi.entities.predicates;

import java.util.function.Predicate;

import de.hatoka.account.capi.entities.UserPO;

public class UserPOLoginPredicate implements Predicate<UserPO>
{
    private final String login;

    public UserPOLoginPredicate(String login)
    {
        this.login = login;
    }

    @Override
    public boolean test(UserPO t)
    {
        return login.equals(t.getLogin());
    }

}