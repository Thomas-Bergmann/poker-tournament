package de.hatoka.player.internal.remote;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.hatoka.common.capi.rest.RestControllerErrorSupport;
import de.hatoka.player.capi.business.PlayerBO;
import de.hatoka.player.capi.business.PlayerBORepository;
import de.hatoka.player.capi.business.PlayerRef;
import de.hatoka.player.capi.remote.PlayerDataRO;
import de.hatoka.player.capi.remote.PlayerRO;
import de.hatoka.user.capi.business.UserRef;

@RestController
@RequestMapping(value = PlayerController.PATH_ROOT, produces = { APPLICATION_JSON_VALUE })
public class PlayerController
{
    public static final String PATH_VAR_USER = "userRef";
    public static final String PATH_VAR_PLAYER = "playerRef";
    public static final String PATH_ROOT = "/users/{"+PATH_VAR_USER+"}/players";
    private static final String SUB_PATH_PLAYER = "/{"+PATH_VAR_PLAYER+"}";
    public static final String PATH_PLAYER = PATH_ROOT + SUB_PATH_PLAYER;

    @Autowired
    private PlayerBORepository playerRepository;
    @Autowired
    private PlayerBO2RO playerBO2RO;

    @Autowired
    private RestControllerErrorSupport errorSupport;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<PlayerRO> getPlayers(@PathVariable(PATH_VAR_USER) String userLocalRef)
    {
        UserRef userRef = UserRef.valueOfLocal(userLocalRef);
        List<PlayerBO> players = playerRepository.getPlayers(userRef);
        return playerBO2RO.apply(players);
    }

    @PutMapping(value = SUB_PATH_PLAYER, consumes = { APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.CREATED)
    public void createPlayer(@PathVariable(PATH_VAR_USER) String userLocalRef, @PathVariable(PATH_VAR_PLAYER) String playerLocalRef, @RequestBody PlayerDataRO input)
    {
        PlayerRef playerRef = PlayerRef.valueOf(UserRef.valueOfLocal(userLocalRef), playerLocalRef);
        Optional<PlayerBO> playerOpt = playerRepository.findPlayer(playerRef);
        if (!playerOpt.isPresent())
        {
            PlayerBO player = playerRepository.createPlayer(playerRef, input.getName());
            player.setEMail(input.geteMail());
        }
    }

    @PatchMapping(value = SUB_PATH_PLAYER, consumes = { APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updatePlayer(@PathVariable(PATH_VAR_USER) String userLocalRef, @PathVariable(PATH_VAR_PLAYER) String playerLocalRef, @RequestBody PlayerDataRO input)
    {
        PlayerRef playerRef = PlayerRef.valueOf(UserRef.valueOfLocal(userLocalRef), playerLocalRef);
        Optional<PlayerBO> playerOpt = playerRepository.findPlayer(playerRef);
        if (!playerOpt.isPresent())
        {
            errorSupport.throwNotFoundException("notfound.player", playerRef.toString());
        }
        PlayerBO player = playerOpt.get();
        player.setEMail(input.geteMail());
        player.setName(input.getName());
    }

    @GetMapping(SUB_PATH_PLAYER)
    @ResponseStatus(HttpStatus.OK)
    public PlayerRO getPlayer(@PathVariable(PATH_VAR_USER) String userLocalRef, @PathVariable(PATH_VAR_PLAYER) String playerLocalRef)
    {
        PlayerRef playerRef = PlayerRef.valueOf(UserRef.valueOfLocal(userLocalRef), playerLocalRef);
        Optional<PlayerBO> playerOpt = playerRepository.findPlayer(playerRef);
        if (!playerOpt.isPresent())
        {
            errorSupport.throwNotFoundException("notfound.player", playerRef.toString());
        }
        return playerBO2RO.apply(playerOpt.get());
    }

    @DeleteMapping(SUB_PATH_PLAYER)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deletePlayer(@PathVariable(PATH_VAR_USER) String userLocalRef, @PathVariable(PATH_VAR_PLAYER) String playerLocalRef)
    {
        PlayerRef playerRef = PlayerRef.valueOf(UserRef.valueOfLocal(userLocalRef), playerLocalRef);
        Optional<PlayerBO> playerOpt = playerRepository.findPlayer(playerRef);
        if (playerOpt.isPresent())
        {
            playerOpt.get().remove();
        }
    }
}
