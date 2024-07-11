package ar.com.poker.engine.states;

import ar.com.poker.api.game.BetCommand;
import ar.com.poker.api.game.TexasHoldEmUtil;
import ar.com.poker.engine.model.ModelContext;
import ar.com.poker.engine.model.PlayerEntity;
import ar.com.util.statemachine.IStateTrigger;

import java.util.EnumMap;
import java.util.Map;

public class BetRoundTrigger implements IStateTrigger<ModelContext> {

    @FunctionalInterface
    private interface BetChecker {
        boolean check(ModelContext context, PlayerEntity playerEntity, BetCommand betCommand);
    }

    private static  Map<TexasHoldEmUtil.BetCommandType, BetChecker> buildBetCommandChecker(){
        Map<TexasHoldEmUtil.BetCommandType, BetChecker> result = new EnumMap<>(TexasHoldEmUtil.BetCommandType.class);
        result.put(TexasHoldEmUtil.BetCommandType.FOLD, (m, p, b) -> true);
        result.put(TexasHoldEmUtil.BetCommandType.TIMEOUT, (m,p,b) -> false);
        result.put(TexasHoldEmUtil.BetCommandType.ERROR, (m, p,b) -> false);
        result.put(TexasHoldEmUtil.BetCommandType.RAISE, (m,p,b) -> b.getChips() > (m.getHighBet() - p.getBet()) && b.getChips() < p.getChips());
        result.put(TexasHoldEmUtil.BetCommandType.ALL_IN, (m,p,b) -> {
            b.setChips(p.getChips());
            return p.getChips() > 0;
        });

        result.put(TexasHoldEmUtil.BetCommandType.CALL, (c, p, b) -> {
            b.setChips(c.getHighBet() - p.getBet());
            return c.getHighBet() > c.getSettings().getBigBlind();
        });

        result.put(TexasHoldEmUtil.BetCommandType.CHECK, (c, p, b) -> {
            b.setChips(c.getHighBet() - p.getBet());
            return b.getChips() == 0 || c.getHighBet() == c.getSettings().getBigBlind();
        });
        return result;
    }
    @Override
    public boolean execute(ModelContext context) {
        return false;
    }
}
