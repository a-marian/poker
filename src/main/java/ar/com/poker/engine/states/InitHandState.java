package ar.com.poker.engine.states;

import java.util.List;

import ar.com.poker.api.core.Deck;
import ar.com.poker.api.game.Settings;
import ar.com.poker.api.game.TexasHoldEmUtil.BetCommandType;
import ar.com.poker.api.game.TexasHoldEmUtil.GameState;
import ar.com.poker.api.game.TexasHoldEmUtil.PlayerState;
import ar.com.poker.engine.model.ModelContext;
import ar.com.poker.engine.model.ModelUtil;
import ar.com.poker.engine.model.PlayerEntity;
import ar.com.util.statemachine.IState;
import static ar.com.poker.api.game.TexasHoldEmUtil.MIN_PLAYERS;

public class InitHandState  implements IState<ModelContext>{
	public static final String NAME = "InitHand";
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean execute(ModelContext context) {
		// TODO Auto-generated method stub
		Deck deck = context.getDeck();
			deck.shuffle();
		Settings settings = context.getSettings();
		context.setGameState(GameState.PRE_FLOP);
		context.clearCommunityCard();
		context.setRound(context.getRound() +1);
		if(context.getRound() % settings.getRounds4IncrementBlind() == 0) {
			settings.setSmallBind(2 * settings.getSmallBind());
		}
		context.setPlayersAllIn(0);
		context.setHighBet(0L);
		List<PlayerEntity>players = context.getPlayers();
		for(PlayerEntity p : players){
			p.setState(PlayerState.READY);
			p.setHandValue(0);
			p.setBet(0);
			p.setShowCards(false);
			p.setCards(deck.obtainCard(), deck.obtainCard());
		}
		int numPlayers = context.getNumPlayers();
		context.setActivePlayers(numPlayers);
		
		int dealerIndex = (context.getDealer() + 1) % numPlayers;
		context.setDealer(dealerIndex);
		context.setPlayerTurn((dealerIndex +1) % numPlayers);
		if(numPlayers > MIN_PLAYERS){
			 compulsoryBet(context, settings.getSmallBind());
		}
		 compulsoryBet(context, settings.getSmallBind());
		return true;
	}
	
	private void compulsoryBet(ModelContext model, long chips) { int turn = model.getPlayerTurn();
	PlayerEntity player = model.getPlayer(turn);
	if (player.getChips() <= chips) {
	            player.setState(PlayerState.ALL_IN);
	            ModelUtil.playerBet(model, player, BetCommandType.ALL_IN, player.getChips());
	        } else {
	            ModelUtil.playerBet(player, chips);
	        }
	        model.setHighBet(chips);
	        model.setPlayerTurn((turn + 1) % model.getNumPlayers());
	    }
	

}
