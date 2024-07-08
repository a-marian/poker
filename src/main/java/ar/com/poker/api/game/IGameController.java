package ar.com.poker.api.game;

public interface IGameController {

	 void setSettings(Settings settings);
	
	 boolean addStrategy(IStrategy strategy);
	
	 void start();
	
	 void waitFinish();
	
}
