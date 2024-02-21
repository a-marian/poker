package ar.com.poker.engine.model;

import ar.com.poker.api.game.BetCommand;
import ar.com.poker.api.game.PlayerInfo;


public class PlayerEntity extends PlayerInfo {
	
	private int handValue = 0;
	private BetCommand betCommand;
	private boolean showCards;
	private int roundsSurvival;
	private long lastRoundChips;
	    
	
	public int getHandValue() {
		return handValue;
	}
	public void setHandValue(int handValue) {
		this.handValue = handValue;
	}
	public BetCommand getBetCommand() {
		return betCommand;
	}
	public void setBetCommand(BetCommand betCommand) {
		this.betCommand = betCommand;
	}
	public boolean isShowCards() {
		return showCards;
	}
	public void setShowCards(boolean showCards) {
		this.showCards = showCards;
	}
	public int getRoundsSurvival() {
		return roundsSurvival;
	}
	public void setRoundsSurvival(int roundsSurvival) {
		this.roundsSurvival = roundsSurvival;
	}
	public long getLastRoundChips() {
		return lastRoundChips;
	}
	public void setLastRoundChips(long lastRoundChips) {
		this.lastRoundChips = lastRoundChips;
	}
	
	 public boolean showCards() {
	        return showCards;
	    }

	    public void showCards(boolean showCards) {
	        this.showCards = showCards;
	    }
	
	
	

}
