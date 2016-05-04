package de.hatoka.group.internal.business;

import de.hatoka.group.capi.business.MemberBO;
import de.hatoka.group.capi.dao.MemberDao;
import de.hatoka.group.capi.entities.MemberPO;

public class MemberBOImpl implements MemberBO
{
    private MemberPO memberPO;
    private MemberDao memberDao;

    public MemberBOImpl(MemberPO memberPO, MemberDao memberDao)
    {
        this.memberPO = memberPO;
        this.memberDao = memberDao;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((memberPO == null) ? 0 : memberPO.hashCode());
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
        MemberBOImpl other = (MemberBOImpl)obj;
        if (memberPO == null)
        {
            if (other.memberPO != null)
                return false;
        }
        else if (!memberPO.equals(other.memberPO))
            return false;
        return true;
    }

    @Override
    public String getUserRef()
    {
        return memberPO.getUserRef();
    }

    @Override
    public String getName()
    {
        return memberPO.getName();
    }

    @Override
    public void remove()
    {
        memberDao.remove(memberPO);
        memberPO = null;
    }
}
