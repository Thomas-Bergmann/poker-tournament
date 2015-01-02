package de.hatoka.address.internal.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import de.hatoka.address.capi.doa.AddressDao;
import de.hatoka.address.capi.entities.AddressPO;
import de.hatoka.common.capi.dao.GenericDao;
import de.hatoka.common.capi.dao.UUIDGenerator;
import de.hatoka.common.capi.exceptions.MandatoryParameterException;

public class AddressDaoImpl extends GenericDao<AddressPO> implements AddressDao
{
    @Inject
    private UUIDGenerator uuidGenerator;

    public AddressDaoImpl()
    {
        super(AddressPO.class);
    }

    @Override
    public AddressPO clone(AddressPO addressPO)
    {
        AddressPO result = addressPO.clone();
        result.setId(uuidGenerator.generate());
        result.setPredecessorRef(addressPO.getId());
        insert(result);
        return result;
    }

    @Override
    public AddressPO createAndInsert()
    {
        AddressPO result = create();
        result.setId(uuidGenerator.generate());
        insert(result);
        return result;
    }

    @Override
    public String getId(AddressPO element)
    {
        if (element == null)
        {
            throw new MandatoryParameterException("element");
        }
        return element.getId();
    }

    @Override
    public AddressPO getPredecessor(AddressPO addressPO)
    {
        String ref = addressPO.getPredecessorRef();
        if (ref == null)
        {
            return null;
        }
        return getById(ref);
    }

    @Override
    public Collection<AddressPO> getSuccessors(AddressPO addressPO)
    {
        String id = addressPO.getId();
        if (id == null)
        {
            return Collections.emptyList();
        }
        List<AddressPO> result = new ArrayList<>();
        for (AddressPO entry : values())
        {
            if (id.equals(entry.getPredecessorRef()))
            {
                result.add(entry);
            }
        }
        return result;
    }
}
