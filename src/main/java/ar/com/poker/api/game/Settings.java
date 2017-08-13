package ar.com.poker.api.game;

public class Settings {
	
	private int maxPlayers;
	private long time;
	private int maxErrors;
	private int maxRounds;
	private long smallBind;
	private long playerChip;
	private int rounds4IncrementBlind;
	
	public Settings(){
		
	}
	
	public Settings(Settings s){
		this.maxPlayers = s.maxPlayers;
		this.time = s.time;
		this.maxErrors = s.maxErrors;
		this.smallBind = s.smallBind;
		this.playerChip = s.playerChip;
		this.maxRounds = s.maxRounds;
		this.rounds4IncrementBlind = s.rounds4IncrementBlind;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getMaxErrors() {
		return maxErrors;
	}

	public void setMaxErrors(int maxErrors) {
		this.maxErrors = maxErrors;
	}

	public int getMaxRounds() {
		return maxRounds;
	}

	public void setMaxRounds(int maxRounds) {
		this.maxRounds = maxRounds;
	}

	public long getSmallBind() {
		return smallBind;
	}

	public long getBigBind() {
		return smallBind * 2;
	}
	
	public void setSmallBind(long smallBind) {
		this.smallBind = smallBind;
	}

	public long getPlayerChip() {
		return playerChip;
	}

	public void setPlayerChip(long playerChip) {
		this.playerChip = playerChip;
	}

	public int getRounds4IncrementBlind() {
		return rounds4IncrementBlind;
	}

	public void setRounds4IncrementBlind(int rounds4IncrementBlind) {
		this.rounds4IncrementBlind = rounds4IncrementBlind;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
	  return "{class:'Settings', maxPlayers:" + maxPlayers + ", time:" + time + ", maxErrors:" + maxErrors + ", playerChip:" + playerChip + ", maxRounds:" + maxRounds + ", smallBind:" + smallBind + ", rounds4IncrementBlind:" + rounds4IncrementBlind + '}';

	}
	
	
	

}
