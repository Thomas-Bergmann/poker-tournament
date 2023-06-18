/*
 * Copyright (C) Intershop Communications AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * The content is proprietary and confidential.
 * Intershop Communication AG, Intershop Tower, 07740 Jena, Germany, 2018-04-05
 */
package de.hatoka.player.internal.remote.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.hatoka.player.capi.business.PlayerRef;
import de.hatoka.player.capi.remote.PlayerDataRO;
import de.hatoka.player.capi.remote.PlayerRO;
import de.hatoka.player.internal.remote.PlayerController;
import de.hatoka.user.capi.business.UserRef;
import tests.de.hatoka.player.PlayerTestApplication;
import tests.de.hatoka.player.PlayerTestConfiguration;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { PlayerTestApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { PlayerTestConfiguration.class })
@ActiveProfiles("test")
public class PlayerControllerTest
{
    private static final UserRef USER_REF = UserRef.valueOfLocal("owner");
    private static final PlayerRef PLAYER_REF1 = PlayerRef.valueOf(USER_REF, "test-one");
    private static final PlayerRef PLAYER_REF2 = PlayerRef.valueOf(USER_REF, "test-two");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createPlayerAndDelete()
    {
        PlayerDataRO data = new PlayerDataRO();
        data.setName("player-one");
        data.seteMail("player-1@hatoka.de");
        putPlayer(PLAYER_REF1, data);

        PlayerRO ro = getPlayer(PLAYER_REF1);
        assertNotNull(ro, "cash game created and found");
        assertNotNull(ro.getData(), "cash game contains data");
        deletePlayer(PLAYER_REF1);
        assertEquals("player-one", ro.getData().getName());
    }

    @Test
    public void testGetPlayers()
    {
        PlayerDataRO data = new PlayerDataRO();
        data.setName("player-one");
        data.seteMail("player-1@hatoka.de");
        putPlayer(PLAYER_REF1, data);
        data.setName("player-two");
        data.seteMail("player-2@hatoka.de");
        putPlayer(PLAYER_REF2, data);

        List<PlayerRO> players = getPlayers(USER_REF);
        assertEquals(2, players.size());
        deletePlayer(PLAYER_REF1);
        deletePlayer(PLAYER_REF2);
    }

    private List<PlayerRO> getPlayers(UserRef ref)
    {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put(PlayerController.PATH_VAR_USER, ref.getGlobalRef());

        return Arrays.asList(this.restTemplate.getForObject(PlayerController.PATH_ROOT, PlayerRO[].class, urlParams));
    }

    private Map<String, String> createURIParameter(PlayerRef ref)
    {
        Map<String, String> urlParams = new HashMap<>();
        urlParams.put(PlayerController.PATH_VAR_USER, ref.getUserRef().getGlobalRef());
        urlParams.put(PlayerController.PATH_VAR_PLAYER, ref.getLocalRef());
        return urlParams;
    }

    private PlayerRO getPlayer(PlayerRef ref)
    {
        return this.restTemplate.getForObject(PlayerController.PATH_PLAYER, PlayerRO.class, createURIParameter(ref));
    }

    private void putPlayer(PlayerRef ref, PlayerDataRO data)
    {
        // this.restTemplate.put(PlayerController.PATH_PLAYER, data, createURIParameter(ref));
        ResponseEntity<Void> response = this.restTemplate.exchange(PlayerController.PATH_PLAYER, HttpMethod.PUT, new HttpEntity<PlayerDataRO>(data), Void.class, createURIParameter(ref));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    private void deletePlayer(PlayerRef ref)
    {
        this.restTemplate.delete(PlayerController.PATH_PLAYER, createURIParameter(ref));
    }
}
