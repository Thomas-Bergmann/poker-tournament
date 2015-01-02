package de.hatoka.address.capi.doa;

import java.util.Collection;

import de.hatoka.address.capi.entities.AddressPO;
import de.hatoka.common.capi.dao.Dao;

public interface AddressDao extends Dao<AddressPO>
{
    /**
     * Creates a copy of an existing address
     * @param addressPO
     * @return
     */
    AddressPO clone(AddressPO addressPO);

    /**
     * Creates a new address
     * @return
     */
    AddressPO createAndInsert();

    /**
     * @param addressPO
     * @return the base of the given address
     */
    AddressPO getPredecessor(AddressPO addressPO);

    /**
     *
     * @param addressPO
     * @return all successors of an address
     */
    Collection<AddressPO> getSuccessors(AddressPO addressPO);

}