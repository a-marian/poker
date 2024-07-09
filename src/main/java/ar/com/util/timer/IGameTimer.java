package ar.com.util.timer;

import ar.com.poker.dispatcher.IGameEventDispatcher;

public interface IGameTimer extends Runnable {
	
	void exit();
	long getTime();
	void resetTimer(Long timeroutId);
	IGameEventDispatcher getDispatcher();
	void setTime(long time);
	void setDispatcher(IGameEventDispatcher dispatcher);
	
	

}
