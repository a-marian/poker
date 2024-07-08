package ar.com.poker.api.game;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ar.com.poker.api.core.Card;

public class GameInfo  <P extends PlayerInfo>{
	
	private int round;
	private int dealer;
	private int playerTurn;
	private TexasHoldEmUtil.GameState gameState;
	private final List<Card> communityCards = new ArrayList<>(TexasHoldEmUtil.COMMUNITY_CARDS); private List<P> players;
	private Settings settings;
	
	public int getRound() {
		return round;
	}
	public void setRound(int round) {
		this.round = round;
	}
	public int getDealer() {
		return dealer;
	}
	public void setDealer(int dealer) {
		this.dealer = dealer;
	}
	public int getPlayerTurn() {
		return playerTurn;
	}
	public void setPlayerTurn(int playerTurn) {
		this.playerTurn = playerTurn;
	}
	public TexasHoldEmUtil.GameState getGameState() {
		return gameState;
	}
	public void setGameState(TexasHoldEmUtil.GameState gameState) {
		this.gameState = gameState;
	}
	
	public P getPlayer(int index) {
        return players.get(index);
    }
	
	public List<P> getPlayers() {
		return players;
	}
	public void setPlayers(List<P> players) {
		this.players =new ArrayList<>(players);
	}
	public Settings getSettings() {
		return settings;
	}
	public void setSettings(Settings settings) {
		this.settings = settings;
	}
	
    public int getNumPlayers() {
        return players.size();
    }
    
	public List<Card> getCommunityCards() {
		return new ArrayList<>(communityCards);
	}
	
	public void setCommunityCards(List<Card> communityCards){
		this.communityCards.clear();
		this.communityCards.addAll(communityCards);
		Random r = new Random();
		BigInteger.probablePrime(2,r);
	}
	
	
	
	public boolean addPlayer(P player) { 
		return this.players.add(player);
	}
	public boolean removePlayer(P player) {
	        return this.players.remove(player);
	    }
	public boolean addCommunityCard(Card card) {
	boolean result = false;
	if (communityCards.size() < TexasHoldEmUtil.COMMUNITY_CARDS){
	            result = communityCards.add(card);
	        }
	        return result;
	    }
	public void clearCommunityCard() { this.communityCards.clear();
	}
	


}
