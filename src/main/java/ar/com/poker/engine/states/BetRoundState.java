package ar.com.poker.engine.states;
import java.util.EnumMap;
import java.util.Map;

import ar.com.poker.api.game.BetCommand;
import ar.com.poker.api.game.TexasHoldEmUtil;
import ar.com.poker.api.game.TexasHoldEmUtil.BetCommandType;
import ar.com.poker.api.game.TexasHoldEmUtil.PlayerState;
import ar.com.poker.engine.model.ModelContext;
import ar.com.poker.engine.model.ModelUtil;
import ar.com.poker.engine.model.PlayerEntity;
import ar.com.util.statemachine.IState;



public class BetRoundState implements IState<ModelContext> { 
	
	@FunctionalInterface
	private static interface BetChecker {
		public boolean check(ModelContext m, PlayerEntity player, BetCommand bet);
	}
	
    public static final String NAME = "BetRound";
    private static final Map<BetCommandType, BetChecker> CHECKERS = buildBetCommandChecker();
    		
    		
	@Override
	public String getName() {
	    return NAME;
	}
	
private static Map<BetCommandType, BetChecker> buildBetCommandChecker() {
	Map<BetCommandType, BetChecker> result = new EnumMap<>(BetCommandType.class);
		result.put(BetCommandType.FOLD, (m, p, b) -> true);
		result.put(BetCommandType.TIMEOUT, (m, p, b) -> false); 
		result.put(BetCommandType.ERROR, (m, p, b) -> false);
	    result.put(BetCommandType.RAISE, (m, p, b) ->
		         b.getChips() > (m.getHighBet() - p.getBet()) &&
		         b.getChips() < p.getChips());
	    result.put(BetCommandType.ALL_IN, (m, p, b) -> {
	    		b.setChips(p.getChips());
        return p.getChips() > 0;
	    });
	    
	    result.put(BetCommandType.CALL, (c, p, b) -> {
	    	b.setChips(c.getHighBet() - p.getBet());
        return c.getHighBet() > c.getSettings().getBigBind();
	    });
	    
	    result.put(BetCommandType.CHECK, (c, p, b) -> {
	    	b.setChips(c.getHighBet() - p.getBet());
        return b.getChips() == 0 || c.getHighBet()==c.getSettings().getBigBind();
	    });
	    
    return result;
}



public boolean execute(ModelContext model) {
boolean result = false;
int playerTurn = model.getPlayerTurn(); PlayerEntity player = model.getPlayer(playerTurn); BetCommand command = player.getBetCommand();
        if (command != null) {
            BetCommand resultCommand = command;
            player.setBetCommand(null);
            long betChips = 0;
            BetCommandType commandType = command.getType();
            if (CHECKERS.get(commandType).check(model, player, command)) {
                betChips = command.getChips();
                player.setState(TexasHoldEmUtil.convert(command.getType()));
            } else {
                commandType = BetCommandType.FOLD;
                player.setState(PlayerState.FOLD);
                if (command.getType() == BetCommandType.TIMEOUT) {
                    resultCommand = new BetCommand(BetCommandType.TIMEOUT);
                } else {
                    resultCommand = new BetCommand(BetCommandType.ERROR);
                }
                ModelUtil.incrementErrors(player, model.getSettings());
            }
            ModelUtil.playerBet(model, player, commandType, betChips);
            model.lastResultCommand(player, resultCommand);
            model.setPlayerTurn(ModelUtil.nextPlayer(model, playerTurn));
            result = true;
        }
        return result;
    }
}