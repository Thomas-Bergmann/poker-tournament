package de.hatoka.address.internal.dao;

import java.util.Collection;

import javax.inject.Inject;

import de.hatoka.address.capi.doa.AddressDao;
import de.hatoka.address.capi.entities.AddressPO;
import de.hatoka.common.capi.dao.GenericJPADao;
import de.hatoka.common.capi.dao.UUIDGenerator;

public class AddressDaoJpa extends GenericJPADao<AddressPO> implements AddressDao
{
    @Inject
    private UUIDGenerator uuidGenerator;

    public AddressDaoJpa()
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
    public AddressPO createAndInsert(String ownerID)
    {
        AddressPO result = create();
        result.setId(uuidGenerator.generate());
        result.setOwnerID(ownerID);
        insert(result);
        return result;
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
        return createNamedQuery("AddressPO.findByPredecessorRef").setParameter("predecessorRef", addressPO.getId()).getResultList();
    }
}
