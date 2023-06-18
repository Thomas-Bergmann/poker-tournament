package de.hatoka.player.internal.business;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.hatoka.player.capi.business.PlayerBO;
import de.hatoka.player.capi.business.PlayerRef;
import de.hatoka.player.internal.persistence.PlayerDao;
import de.hatoka.player.internal.persistence.PlayerPO;
import de.hatoka.user.capi.business.UserRef;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PlayerBOImpl implements PlayerBO
{
    private PlayerPO playerPO;
    @Autowired
    private PlayerDao playerDao;

    public PlayerBOImpl(PlayerPO playerPO)
    {
        this.playerPO = playerPO;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((playerPO == null) ? 0 : playerPO.hashCode());
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
        PlayerBOImpl other = (PlayerBOImpl)obj;
        if (playerPO == null)
        {
            if (other.playerPO != null)
                return false;
        }
        else if (!playerPO.equals(other.playerPO))
            return false;
        return true;
    }

    @Override
    public String getName()
    {
        return playerPO.getName();
    }

    @Override
    public void remove()
    {
        playerDao.delete(playerPO);
        playerPO = null;
    }

    @Override
    public String getEMail()
    {
        return playerPO.getEMail();
    }

    @Override
    public void setEMail(String eMail)
    {
        playerPO.setEMail(eMail);
        savePO();
    }

    private void savePO()
    {
        playerPO = playerDao.save(playerPO);
    }

    @Override
    public PlayerRef getRef()
    {
        return PlayerRef.valueOf(UserRef.valueOfGlobal(playerPO.getContextRef()), playerPO.getPlayerRef());
    }

    @Override
    public void setName(String name)
    {
        playerPO.setName(name);
        savePO();
    }

    @Override
    public Date getFirstDate()
    {
        return playerPO.getFirstDate();
    }
}
