package de.hatoka.user.capi.business;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UserRefTest
{

    private static final String ABC = "abc";
    private static final String USER_REF_ABC = "user:abc";

    @Test
    public void test()
    {
        assertEquals(ABC, UserRef.valueOfGlobal(USER_REF_ABC).getLocalRef());
        assertEquals(USER_REF_ABC, UserRef.valueOfGlobal(USER_REF_ABC).toString());
    }
}
