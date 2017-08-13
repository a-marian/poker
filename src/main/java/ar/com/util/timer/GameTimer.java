package ar.com.util.timer;

import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.poker.dispatcher.IGameEventDispatcher;

public class GameTimer  implements IGameTimer {
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(GameTimer.class);
	 public static final String TIMEOUT_EVENT_TYPE = "timeOutCommand";
	 
	 private final String source;
	 private long time;
	 private IGameEventDispatcher dispatcher;
	 private TimeoutNotifier notifier;
	 private boolean reset = false;
	 private volatile boolean exit = false;
	 private final ExecutorService executors;
	 private Long timeoutId;
	 
	 public GameTimer(String source, ExecutorService executors){
		 this.source = source;
		 this.executors = executors;
	 }


	@Override
	public long getTime() {
		// TODO Auto-generated method stub
		return time;
	}
	
	   @Override
	    public void setTime(long time) {
	        this.time = time;
	    }


	@Override
	public void resetTimer(Long timeroutId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized IGameEventDispatcher getDispatcher() {
		return dispatcher;
	}

	@Override
	public void setDispatcher(IGameEventDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}
	
	 @Override
	    public synchronized void exit() {
	        this.exit = true;
	        this.reset = false;
	       // this.player = null;
	        this.timeoutId = null;
	        notify();
	    }

	    @Override
	    public void run() {
	        LOGGER.debug("run");
	        while (!exit) {
	            try {
	                doTask();
	            } catch (Exception ex) {
	                LOGGER.error("Timer interrupted", ex);
	            }
	        }
	        LOGGER.debug("finish");
	    }

	    private synchronized void doTask() throws InterruptedException {
	        if (timeoutId == null) {
	            wait();
	        }
	        if (timeoutId != null) {
	            reset = false;
	            wait(time);
	            if (!reset && timeoutId != null) {
	                final Long timeoutToNotify = timeoutId;
	                //GameEvent event = new GameEvent(TIMEOUT_EVENT_TYPE, source,  timeoutId);
	            	executors.execute(() -> notifier.notify(timeoutToNotify));
	                timeoutId = null;
	            }
	        }
	    }

}
