package ar.com.poker.api.game;

import java.util.EnumMap;
import java.util.Map;

public class TexasHoldEmUtil {
	
	public static final int MIN_PLAYERS = 2;
	public static final int PLAYER_CARDS = 2;
	public static final int MAX_PLAYERS = 10;
	public static final int COMMUNITY_CARDS = 5;
	
	private static final Map<BetCommandType, PlayerState> PLAYER_STATE_CONVERSOR = buildPlayerStateConversor();
	
	public enum BetCommandType {
		ERROR, TIMEOUT, FOLD, CHECK, CALL, RAISE, ALL_IN
	} 
	
	public enum GameState {
		PRE_FLOP, FLOP, TURN, RIVER, SHOWDOWN, END
		
	}
	
	public enum PlayerState {
		READY(true),
		OUT(false),
		FOLD(false),
		CHECK(true),
		CALL(true),
		RAISE(true),
		ALL_IN(false);
		
		private final boolean active;
		
		private PlayerState(boolean isActive){
			this.active = isActive;
		}
		
		public boolean isActive(){
			return active;
		}
	}

	private TexasHoldEmUtil(){
	}
	
	
	private static Map<BetCommandType, PlayerState> buildPlayerStateConversor(){
		Map<BetCommandType, PlayerState> result = new EnumMap<>(BetCommandType.class);
		
		result.put(BetCommandType.FOLD, PlayerState.FOLD);
		result.put(BetCommandType.ALL_IN, PlayerState.ALL_IN);
		result.put(BetCommandType.CALL, PlayerState.CALL);
		result.put(BetCommandType.CHECK, PlayerState.CHECK);
		result.put(BetCommandType.RAISE, PlayerState.RAISE);
		result.put(BetCommandType.ERROR, PlayerState.FOLD);
		result.put(BetCommandType.TIMEOUT, PlayerState.FOLD);
		
		return result;
	}
	
	public static PlayerState convert(BetCommandType betCommand) {
		return PLAYER_STATE_CONVERSOR.get(betCommand);
	}
	
	
	
	
}
