package ar.com.poker.api.game;

import java.util.Map;

public interface IGameController {

	 void setSettings(Settings settings);
	
	 boolean addStrategy(IStrategy strategy);
	
	 void start() throws Exception;
	
	 void waitFinish();

	 void stop();
	 Map<String, Double> getScores();
	
}
