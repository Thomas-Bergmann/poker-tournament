package de.hatoka.user.internal.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.hatoka.user.capi.business.UserRef;
import tests.de.hatoka.user.UserTestConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { UserTestConfiguration.class })
public class UserDaoTest
{
    private static final String LOCAL_REF = UserDaoTest.class.getSimpleName();
    private static final UserRef USER_REF = UserRef.valueOfLocal(LOCAL_REF);

    @Autowired
    private UserDao userDao;

    @Test
    public void testFindLocalRef()
    {
        UserPO userPO = new UserPO();
        userPO.setGlobalRef(USER_REF.getGlobalRef());
        userDao.save(userPO);
        Optional<UserPO> findUserOpt = userDao.findByGlobalRef(USER_REF.getGlobalRef());
        assertTrue(findUserOpt.isPresent());
        assertEquals(userPO, findUserOpt.get());
        userDao.delete(userPO);
    }
}
