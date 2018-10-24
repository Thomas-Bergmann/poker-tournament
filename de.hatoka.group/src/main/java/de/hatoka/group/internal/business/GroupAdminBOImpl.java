package de.hatoka.group.internal.business;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.hatoka.group.capi.business.GroupAdminBO;
import de.hatoka.group.capi.business.GroupBO;
import de.hatoka.group.internal.persistence.GroupAdminDao;
import de.hatoka.group.internal.persistence.GroupAdminPO;
import de.hatoka.user.capi.business.UserBO;
import de.hatoka.user.capi.business.UserBORepository;
import de.hatoka.user.capi.business.UserRef;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GroupAdminBOImpl implements GroupAdminBO
{
    @Autowired
    private GroupAdminDao adminDao;
    @Autowired
    private UserBORepository userBORepository;

    private final GroupBO groupBO;
    private final GroupAdminPO adminPO;

    public GroupAdminBOImpl(GroupBO groupBO, GroupAdminPO adminPO)
    {
        this.groupBO = groupBO;
        this.adminPO = adminPO;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((adminPO == null) ? 0 : adminPO.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GroupAdminBOImpl other = (GroupAdminBOImpl)obj;
        if (adminPO == null)
        {
            if (other.adminPO != null)
                return false;
        }
        else if (!adminPO.equals(other.adminPO))
            return false;
        return true;
    }

    @Override
    @Transactional
    public void remove()
    {
        if (groupBO.getMembers().size() < 2)
        {
            throw new IllegalStateException("Can't delete last admin");
        }
        adminDao.delete(adminPO);
    }

    @Override
    public UserBO getUser()
    {
        return userBORepository.findUser(UserRef.valueOfGlobal(adminPO.getUserRef())).get();
    }

    @Override
    public GroupBO getGroup()
    {
        return groupBO;
    }
}
