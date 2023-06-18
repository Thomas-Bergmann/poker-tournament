package de.hatoka.group.internal.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import tests.de.hatoka.group.GroupTestConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { GroupTestConfiguration.class })
public class GroupDaoTest
{
    private static final String GROUP_REF = "group:testCrud";

    @Autowired
    private GroupDao dao;

    @Test
    public void testFindByOwnerRefAndName()
    {
        GroupPO groupPO = new GroupPO();
        groupPO.setGlobalGroupRef(GROUP_REF);
        groupPO.setName("testCrud");
        dao.save(groupPO);
        Optional<GroupPO> storedGroupPO = dao.findByGlobalGroupRef(GROUP_REF);
        assertEquals(groupPO, storedGroupPO.get(), "loaded group");
        dao.delete(groupPO);
    }
}
