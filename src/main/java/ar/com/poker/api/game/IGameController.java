package ar.com.poker.api.game;

public interface IGameController {

	public void setSettings(Settings settings);
	
	public boolean addStrategy(IStrategy strategy);
	
	public void start();
	
	public void waitFinish();
	
}
