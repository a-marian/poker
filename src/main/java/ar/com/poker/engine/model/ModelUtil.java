package ar.com.poker.engine.model;

import ar.com.poker.api.game.Settings;
import ar.com.poker.api.game.TexasHoldEmUtil.BetCommandType;
import ar.com.poker.api.game.TexasHoldEmUtil.PlayerState;

public final class ModelUtil {

	public static final int NO_PLAYER_TURN = -1;
	
	private ModelUtil(){
		
	}
	
	public static boolean range(int min, int max, int value){
		return min <= value && value < max;
	}
	
	public static int nextPlayer(ModelContext model, int turn){
		int result = NO_PLAYER_TURN;
		int players = model.getNumPlayers();
		if(players > 1 && range(0, players, turn)){
			int i = (turn + 1) % players;
			while (i != turn && result == NO_PLAYER_TURN){
				if(model.getPlayer(i).isActive()){
					result = i;
				}else{
					i = (i + 1) % players;
				}
			}
			result = checkNextPlayer(model, result);
		}
		return result;
	}
	
	private static int checkNextPlayer(ModelContext model, int index){
		int result = index;
		if(result != NO_PLAYER_TURN
				&& model.getPlayer(result).getBet() == model.getHighBet()
				&& (model.getPlayer(result).getState() != PlayerState.READY
				|| model.getActivePlayers() == 1)) {
			result = NO_PLAYER_TURN;
		}
		return result;
	}
	
	public static void playerBet(ModelContext model, PlayerEntity player, BetCommandType betCommand, long chips) {
        if (betCommand == BetCommandType.ALL_IN) {
            model.setPlayersAllIn(model.getPlayersAllIn() + 1);
            model.setActivePlayers(model.getActivePlayers() - 1);
        } else if (betCommand == BetCommandType.FOLD || betCommand == BetCommandType.TIMEOUT) {
            model.setActivePlayers(model.getActivePlayers() - 1);
        }
        playerBet(player, chips);
        model.setHighBet(Math.max(model.getHighBet(), player.getBet()));
        model.setBets(model.getBets() + 1);
    }

    public static void playerBet(PlayerEntity player, long chips) {
        player.setBet(player.getBet() + chips);
        player.setChips(player.getChips() - chips);
    }

    public static void incrementErrors(PlayerEntity player, Settings settings) {
        int errors = player.getErrors() + 1;
        player.setErrors(errors);
        if (errors >= settings.getMaxErrors()) {
            player.setState(PlayerState.OUT);
            player.setChips(0);
        } else {
            player.setState(PlayerState.FOLD);
        }
    }
}
