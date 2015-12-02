package de.hatoka.account.internal.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import de.hatoka.account.capi.business.AccountBusinessFactory;
import de.hatoka.account.capi.entities.UserPO;
import de.hatoka.address.capi.business.AddressBO;
import de.hatoka.address.capi.business.AddressBORepository;
import de.hatoka.address.capi.doa.AddressDao;
import de.hatoka.address.capi.entities.AddressPO;
import de.hatoka.common.capi.exceptions.IllegalObjectAccessException;

public class UserAddressBORespositoryImpl implements AddressBORepository
{
    private final UserPO userPO;
    private final AddressDao addressDao;
    private final AccountBusinessFactory businessFactory;

    public UserAddressBORespositoryImpl(UserPO userPO, AddressDao addressDao, AccountBusinessFactory businessFactory)
    {
        this.userPO = userPO;
        this.addressDao = addressDao;
        this.businessFactory = businessFactory;
    }

    @Override
    public void clean()
    {
        userPO.setAddressRefs(Collections.emptySet());
    }

    @Override
    public void cloned(AddressBO newAddressBO, String oldAddressRef)
    {
        // nothing to do; the repository will not react on cloning addresses
        Set<String> refs = userPO.getAddressRefs();
        if (refs.contains(oldAddressRef))
        {
            refs.remove(oldAddressRef);
            refs.add(newAddressBO.getID());
        }
    }

    @Override
    public boolean contains(AddressBO addressBO)
    {
        return contains(addressBO.getID());
    }

    private boolean contains(String addressRef)
    {
        return userPO.getAddressRefs().contains(addressRef);
    }

    @Override
    public AddressBO createAddressBO()
    {
        AddressPO addressPO = addressDao.createAndInsert(userPO.getId());
        userPO.getAddressRefs().add(addressPO.getId());
        return newBO(addressPO);
    }

    @Override
    public Collection<AddressBO> getAll()
    {
        Collection<String> addressRefs = userPO.getAddressRefs();
        List<AddressBO> result = new ArrayList<>(addressRefs.size());
        for (String addressRef : addressRefs)
        {
            result.add(getByID(addressRef));
        }
        return result;
    }

    @Override
    public AddressBO getByID(String addressRef)
    {
        AddressPO addressPO = addressDao.getById(addressRef);
        if (addressPO == null)
        {
            return null;
        }
        if (!contains(addressRef))
        {
            throw new IllegalObjectAccessException("Address is not assigned to user.");
        }
        return newBO(addressPO);
    }

    private AddressBO newBO(AddressPO addressPO)
    {
        return businessFactory.getAddressBO(addressPO, this);
    }

    @Override
    public void removedAddressRef(String oldAddressRef)
    {
        if (!contains(oldAddressRef))
        {
            throw new IllegalObjectAccessException("Can't delete address from foreign repository.");
        }
        userPO.getAddressRefs().remove(oldAddressRef);
    }

}
