package de.hatoka.group.internal.remote;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.hatoka.common.capi.rest.RestControllerErrorSupport;
import de.hatoka.group.capi.business.GroupBO;
import de.hatoka.group.capi.business.GroupBORepository;
import de.hatoka.group.capi.business.GroupRef;
import de.hatoka.group.capi.remote.GroupDataRO;
import de.hatoka.group.capi.remote.GroupRO;
import de.hatoka.player.capi.business.PlayerBORepository;
import de.hatoka.player.capi.business.PlayerRef;
import de.hatoka.user.capi.business.UserBORepository;
import de.hatoka.user.capi.business.UserRef;

@RestController
@RequestMapping(value = GroupController.PATH_ROOT, produces = { APPLICATION_JSON_VALUE })
public class GroupController
{
    public static final String PATH_VAR_GROUP = "groupRef";
    public static final String PATH_VAR_USER = "userRef";
    public static final String PATH_VAR_PLAYER = "playerRef";
    public static final String PATH_ROOT = "/groups";
    private static final String SUB_PATH_GROUP   = "/{groupRef}";
    // private static final String SUB_PATH_MEMBERS = "/{groupRef}/members";
    // private static final String SUB_PATH_MEMBER  = "/{groupRef}/members/{playerRef}";
    // private static final String SUB_PATH_ADMINS = "/{groupRef}/admins";
    // private static final String SUB_PATH_ADMIN  = "/{groupRef}/admins/{userRef}";

    @Autowired
    private GroupBORepository groupRepository;
    @Autowired
    private GroupBO2RO groupBO2RO;
    @Autowired
    private UserBORepository userBORepository;
    @Autowired
    private PlayerBORepository playerBORepository;

    @Autowired
    private RestControllerErrorSupport errorSupport;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<GroupRO> getGroups(@PathVariable(PATH_VAR_USER) String userGlobalRef)
    {
        UserRef userRef = UserRef.valueOfGlobal(userGlobalRef);
        List<GroupBO> groups = groupRepository.getGroups(userRef);
        return groupBO2RO.apply(groups);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(SUB_PATH_GROUP)
    public GroupRO getGroup(@PathVariable(PATH_VAR_GROUP) String groupLocalRef)
    {
        GroupRef groupRef = GroupRef.valueOfLocal(groupLocalRef);
        Optional<GroupRO> groupOpt = groupRepository.findGroup(groupRef).map(groupBO2RO::apply);
        if (!groupOpt.isPresent())
        {
            errorSupport.throwNotFoundException("notfound.group", groupRef.toString());
        }
        return groupOpt.get();
    }

    @PutMapping(value = SUB_PATH_GROUP, consumes = { APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.CREATED)
    public void createGroup(@PathVariable(PATH_VAR_GROUP) String groupLocalRef, @RequestBody GroupDataRO input)
    {
        GroupRef groupRef = GroupRef.valueOfLocal(groupLocalRef);
        Optional<GroupBO> groupOpt = groupRepository.findGroup(groupRef);
        if (!groupOpt.isPresent())
        {
            GroupBO group = groupRepository.createGroup(groupRef, input.getName());
            input.getAdmins().stream()
                .map(UserRef::valueOfGlobal)
                .map(userBORepository::findUser)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(group::createAdmin);
            input.getMembers().stream()
                .map(PlayerRef::valueOf)
                .map(playerBORepository::findPlayer)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(group::createMember);
        }
    }
}
