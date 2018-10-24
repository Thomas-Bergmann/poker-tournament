package de.hatoka.user.internal.business;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.hatoka.common.capi.exceptions.DuplicateObjectException;
import de.hatoka.user.capi.business.UserBO;
import de.hatoka.user.capi.business.UserBORepository;
import de.hatoka.user.capi.business.UserRef;
import de.hatoka.user.internal.persistence.UserDao;
import de.hatoka.user.internal.persistence.UserPO;

@Component
public class UserBORepositoryImpl implements UserBORepository
{
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserBOFactory factory;

    @Override
    public UserBO createUser(UserRef globalRef)
    {
        Optional<UserPO> userOpt = userDao.findByGlobalRef(globalRef.getGlobalRef());
        if (userOpt.isPresent())
        {
            throw new DuplicateObjectException("User", "externalRef", globalRef.getGlobalRef());
        }
        UserPO userPO = new UserPO();
        userPO.setGlobalRef(globalRef.getGlobalRef());
        userDao.save(userPO);
        return factory.get(userPO);
    }

    @Override
    public Optional<UserBO> findUser(UserRef externalRef)
    {
        return userDao.findByGlobalRef(externalRef.getGlobalRef()).map(factory::get);
    }

    @Override
    public List<UserBO> getAllUsers()
    {
        return userDao.findAll().stream().map(factory::get).collect(Collectors.toList());
    }
}
