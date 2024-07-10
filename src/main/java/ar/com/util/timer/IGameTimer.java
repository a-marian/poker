package ar.com.util.timer;

import ar.com.poker.dispatcher.IGameEventDispatcher;

public interface IGameTimer extends Runnable {
	
	void exit();
	long getTime();
	void resetTimer(Long timeroutId);
	void changeTimeoutId(Long timeoutId);
	void setTime(long time);
	void setNotifier(TimeoutNotifier notifier);
	
	

}
