/*
 * Copyright (C) Intershop Communications AG - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * The content is proprietary and confidential.
 * Intershop Communication AG, Intershop Tower, 07740 Jena, Germany, 2018-04-05
 */
package de.hatoka.cashgame.internal.remote.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import de.hatoka.cashgame.capi.business.CashGameRef;
import de.hatoka.cashgame.internal.remote.model.CashGameDataRO;
import de.hatoka.cashgame.internal.remote.model.CashGameRO;
import de.hatoka.common.capi.math.Money;
import de.hatoka.user.capi.business.UserRef;
import tests.de.hatoka.cashgame.CashGameTestApplication;
import tests.de.hatoka.cashgame.CashGameTestConfiguration;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { CashGameTestApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { CashGameTestConfiguration.class })
@ActiveProfiles("test")
public class CashGameControllerTest
{
    private static final Money EURO_5 = Money.valueOf("5 EUR");
    private static final UserRef USER_REF = UserRef.valueOfLocal("owner");
    private static final CashGameRef CASHGAME_REF1 = CashGameRef.valueOf(USER_REF, "test-one");
    private static final CashGameRef CASHGAME_REF2 = CashGameRef.valueOf(USER_REF, "test-two");

    @Autowired
    private TestRestTemplate restTemplate;

//    @InjectMocks
//    @Autowired
//    private CashGameController underTest;
//
    @BeforeEach
    public void setup()
    {
        // mockMvc = MockMvcBuilders.standaloneSetup(underTest).build();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createCashGameAndDelete()
    {
        CashGameDataRO data = new CashGameDataRO();
        data.setBuyIn(EURO_5);
        data.setDate(new Date());
        putCashGame(CASHGAME_REF1, data);

        CashGameRO ro = getCashGame(CASHGAME_REF1);
        assertNotNull(ro, "cash game created and found");
        assertNotNull(ro.getData(), "cash game contains data");
        deleteCashGame(CASHGAME_REF1);
        assertEquals(EURO_5, ro.getData().getBuyIn());
    }

    @Test
    public void testGetCashGames()
    {
        CashGameDataRO data = new CashGameDataRO();
        data.setBuyIn(EURO_5);
        data.setDate(new Date());
        putCashGame(CASHGAME_REF1, data);
        data.setDate(new Date());
        putCashGame(CASHGAME_REF2, data);

        List<CashGameRO> games = getCashGames(USER_REF);
        assertEquals(2, games.size());
        deleteCashGame(CASHGAME_REF1);
        deleteCashGame(CASHGAME_REF2);
    }

    private List<CashGameRO> getCashGames(UserRef ref)
    {
        Map<String, String> urlParams = Collections.emptyMap();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(CashGameController.PATH_ROOT)
                .queryParam(CashGameController.QUERY_VAR_USEREF, ref.getGlobalRef());

        // System.out.println(builder.buildAndExpand(urlParams).toUri());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<CashGameRO[]> response = restTemplate.exchange(builder.buildAndExpand(urlParams).toUri() , HttpMethod.GET, httpEntity, CashGameRO[].class);
        return Arrays.asList(response.getBody());
    }

    private CashGameRO getCashGame(CashGameRef ref)
    {
        return this.restTemplate.getForObject(CashGameController.PATH_CASHGAME, CashGameRO.class, ref);
    }

    private void putCashGame(CashGameRef ref, CashGameDataRO data)
    {
        this.restTemplate.put(CashGameController.PATH_CASHGAME, data, ref);
    }

    private void deleteCashGame(CashGameRef ref)
    {
        this.restTemplate.delete(CashGameController.PATH_CASHGAME, ref);
    }
}
