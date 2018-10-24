package de.hatoka.group.internal.remote;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import de.hatoka.group.capi.business.GroupBO;
import de.hatoka.group.capi.remote.GroupDataRO;
import de.hatoka.group.capi.remote.GroupInfoRO;
import de.hatoka.group.capi.remote.GroupRO;

@Component
public class GroupBO2RO
{
    public GroupRO apply(GroupBO group)
    {
        GroupDataRO data = new GroupDataRO();
        data.setName(group.getName());
        data.setAdmins(group.getAdmins().stream().map(a -> a.getUser().getRef().getGlobalRef()).collect(Collectors.toList()));
        data.setMembers(group.getMembers().stream().map(m -> m.getPlayer().get().getRef().getGlobalRef()).collect(Collectors.toList()));
        GroupInfoRO info = new GroupInfoRO();
        info.setCountMember(group.getMembers().size());
        info.setCountAdmin(group.getAdmins().size());
        GroupRO result = new GroupRO();
        result.setRefGlobal(group.getRef().getGlobalRef());
        result.setRefLocal(group.getRef().getLocalRef());
        result.setData(data);
        result.setInfo(info);
        return result;
    }

    public List<GroupRO> apply(List<GroupBO> groups)
    {
        return groups.stream().map(this::apply).collect(Collectors.toList());
    }
}
