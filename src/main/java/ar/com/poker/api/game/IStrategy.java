package ar.com.poker.api.game;

import java.util.List;

import ar.com.poker.api.core.Card;

public interface IStrategy {

	public String getName();
	
	public BetCommand getCommand(GameInfo<PlayerInfo> state);
	
	public default void updateState(GameInfo<PlayerInfo> state){}
	
	public default void check(List<Card> comunityCards){}
	
	public default void onPlayerCommand(String player, BetCommand betCommand){}
}
