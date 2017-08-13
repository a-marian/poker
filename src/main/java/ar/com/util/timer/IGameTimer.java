package ar.com.util.timer;

import ar.com.poker.dispatcher.IGameEventDispatcher;

public interface IGameTimer extends Runnable {
	
	public void exit();
	public long getTime();
	public void resetTimer(Long timeroutId);
	public IGameEventDispatcher getDispatcher();
	public void setTime(long time);
	public void setDispatcher(IGameEventDispatcher dispatcher);
	
	

}
