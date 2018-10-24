package de.hatoka.user.internal.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.hatoka.common.capi.exceptions.DuplicateObjectException;
import de.hatoka.user.capi.business.UserBO;
import de.hatoka.user.capi.business.UserBORepository;
import de.hatoka.user.capi.business.UserRef;
import tests.de.hatoka.user.UserTestConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { UserTestConfiguration.class })
public class UserBORepositoryImplTest
{
    private static final UserRef USER_LOGIN_TEST = UserRef.valueOfLocal("test_user");

    @Autowired
    private UserBORepository userRepo;

    @AfterEach @BeforeEach
    public void destroyCreatedObjects()
    {
        if (userRepo != null)
        {
            userRepo.clear();
        }
    }

    @Test
    public void testCreateUser()
    {
        Optional<UserBO> notFoundUserOpt = userRepo.findUser(USER_LOGIN_TEST);
        assertFalse(notFoundUserOpt.isPresent(), "user not found");
        UserBO user = userRepo.createUser(USER_LOGIN_TEST);
        assertNotNull(user, "user not created");
        Optional<UserBO> storedUserOpt = userRepo.findUser(USER_LOGIN_TEST);
        assertTrue(storedUserOpt.isPresent(), "user found");
        assertEquals(user, storedUserOpt.get(), "user not equal");
    }

    @Test
    public void testExistingUser()
    {
        userRepo.createUser(USER_LOGIN_TEST);
        assertThrows(DuplicateObjectException.class, () -> {
            userRepo.createUser(USER_LOGIN_TEST);
        });
    }

}
