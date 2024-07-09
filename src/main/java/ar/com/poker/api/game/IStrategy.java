package ar.com.poker.api.game;

import java.util.List;
import java.util.Map;

import ar.com.poker.api.core.Card;

@FunctionalInterface
public interface IStrategy {

	String getName();
	
	default BetCommand getCommand(GameInfo<PlayerInfo> state){
		return null;
	}
	default void initHand(GameInfo<PlayerInfo> state){}
	default void endHand(GameInfo<PlayerInfo> state){}

	default void endGame(Map<String, Double> scores){}
	default void check(List<Card> comunityCards){}
	default void onPlayerCommand(String player, BetCommand betCommand){}
}
