package de.hatoka.group.internal.persistence;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import tests.de.hatoka.group.GroupTestConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { GroupTestConfiguration.class })
public class MemberDaoTest
{
    private static final String PLAYER_REF = MemberDaoTest.class.getSimpleName() + "OWNER";
    private static final String GROUP_REF = MemberDaoTest.class.getSimpleName() + "GROUP";

    @Autowired
    private GroupMemberDao dao;

    @Test
    public void testFindByGroupRef()
    {
        GroupMemberPO member = new GroupMemberPO();
        member.setGroupRef(GROUP_REF);
        member.setPlayerRef(PLAYER_REF);

        dao.save(member);
        List<GroupMemberPO> members = dao.findByGroupRef(GROUP_REF);
        assertTrue(members.contains(member), "contains member");
        List<GroupMemberPO> otherMembers = dao.findByGroupRef(GROUP_REF + "non_existing");
        assertFalse(otherMembers.contains(member), "doesn't contain member of other group");
        dao.delete(member);
    }
}
