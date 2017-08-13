package ar.com.poker.engine.states;

import ar.com.poker.api.game.TexasHoldEmUtil.GameState;
import ar.com.poker.engine.model.ModelContext;
import ar.com.poker.engine.model.ModelUtil;
import ar.com.util.statemachine.IState;
import static ar.com.poker.api.game.TexasHoldEmUtil.PlayerState.READY;

public class CheckState implements IState<ModelContext>{
	public static final String NAME = "Next";
	private static final GameState[] GAME_STATE = GameState.values();
	private static final int[] OBTAIN_CARDS = {3, 1, 1, 0, 0};

	@Override
	public String getName() {
		return NAME;
	}
	
	private int indexByGameState(GameState gameState){
		int i = 0;
		while(i < GAME_STATE.length && GAME_STATE[i] != gameState){
			i++;
		}
		return i;
	}
	

	@Override
	public boolean execute(ModelContext context) {
		int indexGameState = indexByGameState(context.getGameState());
		if(OBTAIN_CARDS[indexGameState] > 0){
			context.addCommunityCards(OBTAIN_CARDS[indexGameState]);
		}
		context.setGameState(GAME_STATE[indexGameState+1]);
        //context.setLastActivePlayers(context.getActivePlayers());
        context.setBets(0);
        context.getPlayers().stream().filter(p -> p.isActive()).forEach(p -> p.setState(READY));
        context.setPlayerTurn(ModelUtil.nextPlayer(context, context.getDealer()));
        context.setLastBetCommand(null);
        context.setLastPlayerBet(null);
		return true;
	}
	

}
