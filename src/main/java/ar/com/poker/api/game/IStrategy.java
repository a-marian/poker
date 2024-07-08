package ar.com.poker.api.game;

import java.util.List;

import ar.com.poker.api.core.Card;

public interface IStrategy {

	String getName();
	
	BetCommand getCommand(GameInfo<PlayerInfo> state);
	
	default void updateState(GameInfo<PlayerInfo> state){}
	
	default void check(List<Card> comunityCards){}
	
	default void onPlayerCommand(String player, BetCommand betCommand){}
}
