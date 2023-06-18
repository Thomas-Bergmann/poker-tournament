package de.hatoka.cashgame.internal.remote.controller;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.hatoka.cashgame.capi.business.CashGameBO;
import de.hatoka.cashgame.capi.business.CashGameBORepository;
import de.hatoka.cashgame.capi.business.CashGameRef;
import de.hatoka.cashgame.internal.remote.mapper.CashGameBO2RO;
import de.hatoka.cashgame.internal.remote.model.CashGameDataRO;
import de.hatoka.cashgame.internal.remote.model.CashGameRO;
import de.hatoka.common.capi.rest.RestControllerErrorSupport;
import de.hatoka.user.capi.business.UserRef;

@RestController
@RequestMapping(value = CashGameController.PATH_ROOT, produces = { APPLICATION_JSON_VALUE })
public class CashGameController
{
    public static final String PATH_ROOT = "/cashgames";
    private static final String PATH_VAR_CASHGAME = "cashGameRef";
    private static final String GAME_SUB_PATH = "/{"+PATH_VAR_CASHGAME+"}";
    public static final String PATH_CASHGAME = "/cashgames" + GAME_SUB_PATH;
    public static final String QUERY_VAR_USEREF= "userRef";

    @Autowired
    private CashGameBORepository cashGameRepository;
    @Autowired
    private CashGameBO2RO cashGameBO2RO;

    @Autowired
    private RestControllerErrorSupport errorSupport;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<CashGameRO> getCashGames(@RequestParam(QUERY_VAR_USEREF) String userRefString)
    {
        UserRef userRef = UserRef.valueOfGlobal(userRefString);
        List<CashGameBO> cashGames = cashGameRepository.getCashGames(userRef);
        return cashGameBO2RO.apply(cashGames);
    }

    @PutMapping(value = GAME_SUB_PATH, consumes = { APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.CREATED)
    public void createCashGame(@PathVariable(PATH_VAR_CASHGAME) String cashGameRefString, @RequestBody CashGameDataRO input)
    {
        CashGameRef cashGameRef = CashGameRef.valueOf(cashGameRefString);
        Optional<CashGameBO> CashGameOpt = cashGameRepository.findCashGame(cashGameRef);
        if (!CashGameOpt.isPresent())
        {
            CashGameBO cashGame = cashGameRepository.createCashGame(cashGameRef.getUserRef(), cashGameRef.getLocalRef(), input.getDate());
            cashGame.setBuyIn(input.getBuyIn());
        }
    }

    @GetMapping(GAME_SUB_PATH)
    @ResponseStatus(HttpStatus.OK)
    public CashGameRO getCashGame(@PathVariable(PATH_VAR_CASHGAME) String cashGameRefString)
    {
        CashGameRef cashGameRef = CashGameRef.valueOf(cashGameRefString);
        Optional<CashGameBO> cashGameOpt = cashGameRepository.findCashGame(cashGameRef);
        if (!cashGameOpt.isPresent())
        {
            errorSupport.throwNotFoundException("notfound.CashGame", cashGameRef.toString());
        }
        return cashGameBO2RO.apply(cashGameOpt.get());
    }

    @DeleteMapping(GAME_SUB_PATH)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteCashGame(@PathVariable(PATH_VAR_CASHGAME) String cashGameRefString)
    {
        CashGameRef cashGameRef = CashGameRef.valueOf(cashGameRefString);
        Optional<CashGameBO> cashGameOpt = cashGameRepository.findCashGame(cashGameRef);
        if (cashGameOpt.isPresent())
        {
            cashGameOpt.get().remove();
        }
    }
}
