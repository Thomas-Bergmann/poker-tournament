package de.hatoka.group.internal.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.hatoka.group.capi.business.GroupAdminBO;
import de.hatoka.group.capi.business.GroupBO;
import de.hatoka.group.capi.business.GroupBORepository;
import de.hatoka.group.capi.business.GroupMemberBO;
import de.hatoka.group.capi.business.GroupRef;
import de.hatoka.player.capi.business.PlayerBO;
import de.hatoka.player.capi.business.PlayerBORepository;
import de.hatoka.player.capi.business.PlayerRef;
import de.hatoka.user.capi.business.UserBO;
import de.hatoka.user.capi.business.UserBORepository;
import de.hatoka.user.capi.business.UserRef;
import tests.de.hatoka.group.GroupTestConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { GroupTestConfiguration.class })
public class GroupBOImplTest
{
    private static final UserRef OWNER_REF = UserRef.valueOfLocal(GroupBOImplTest.class.getSimpleName() + "_OWNER");
    private static final PlayerRef OWNER_PLAYER_REF = PlayerRef.valueOf(OWNER_REF, "owner");
    private static final PlayerRef OTHER_PLAYER_REF = PlayerRef.valueOf(OWNER_REF, "player-one");
    private static final GroupRef OWNER_GROUP1_REF = GroupRef.valueOfLocal("group-0");
    private static final GroupRef OWNER_GROUP2_REF = GroupRef.valueOfLocal("group-1");

    @Autowired
    private GroupBORepository groupRepo;
    @Autowired
    private UserBORepository userRepo;
    @Autowired
    private PlayerBORepository playerRepo;

    @AfterEach @BeforeEach
    public void destroyCreatedObjects()
    {
        if (groupRepo != null)
        {
            groupRepo.clear();
        }
        if (playerRepo != null)
        {
            playerRepo.clear();
        }
        if (userRepo != null)
        {
            userRepo.clear();
        }
    }

    @Test
    public void testStaticContent()
    {
        GroupBO underTest = groupRepo.createGroup(OWNER_GROUP1_REF, "testGroup");
        assertEquals("testGroup", underTest.getName());
    }

    @Test
    public void testCreateMember()
    {
        GroupBO underTest = groupRepo.createGroup(OWNER_GROUP1_REF, "testGroup");
        UserBO user = userRepo.createUser(OWNER_REF);
        underTest.createAdmin(user);
        Collection<GroupAdminBO> admins = underTest.getAdmins();
        assertEquals(1, admins.size());

        // add new member
        PlayerBO owner = playerRepo.createPlayer(OWNER_PLAYER_REF, "owner");
        PlayerBO player = playerRepo.createPlayer(OTHER_PLAYER_REF, "player-one");
        underTest.createMember(owner);
        GroupMemberBO member = underTest.createMember(player);
        assertTrue(member.getPlayer().isPresent());
        assertEquals("player-one", member.getPlayer().get().getName());
        Collection<GroupMemberBO> members = underTest.getMembers();
        assertEquals(2, members.size());
        assertTrue(members.contains(member));
    }

    @Test
    public void testGroupIdentity()
    {
        GroupBO firstGroup = groupRepo.createGroup(OWNER_GROUP1_REF, "testGroup1");
        GroupBO secondGroup = groupRepo.createGroup(OWNER_GROUP2_REF, "testGroup2");
        assertNotEquals(firstGroup, secondGroup);
        UserBO user = userRepo.createUser(OWNER_REF);
        firstGroup.createAdmin(user);
        secondGroup.createAdmin(user);
        List<GroupBO> groups = groupRepo.getGroups(OWNER_REF);
        assertEquals(2, groups.size());
    }

    @Test
    public void testUniqueGroupRef()
    {
        groupRepo.createGroup(OWNER_GROUP1_REF, "testGroup1");
        assertThrows(DataIntegrityViolationException.class, () -> {
            groupRepo.createGroup(OWNER_GROUP1_REF, "testGroup3");
        });
    }
}
