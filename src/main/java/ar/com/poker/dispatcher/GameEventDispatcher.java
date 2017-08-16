package ar.com.poker.dispatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.slf4j.LoggerFactory;

import net.jcip.annotations.ThreadSafe;

import org.slf4j.Logger;

@ThreadSafe
public class GameEventDispatcher<E extends Enum, T> implements IGameEventDispatcher<E> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GameEventDispatcher.class);

	public static final String EXIT_EVENT_TYPE = "exit";
	private final Map<E, IGameEventProcessor<E,T>> processors;
	private final T target;
	private final E exitEven;
	private List<GameEvent> events = new ArrayList<>();
	private volatile boolean exit = false;
	private ExecutorService executors;
	
	public GameEventDispatcher(T target, Map<E, IGameEventProcessor<E,T>> processors, ExecutorService executors, E exitEven) {
        this.target = target;
        this.processors = processors;
        this.executors = executors;
        this.exitEven = exitEven;
    }
	
	public synchronized void dispatch(GameEvent event){
		events.add(event);
		this.notify();
	}

	
	private void process (GameEvent event){
		IGameEventProcessor processor = processors.get(event.getType());
		if(processor != null){
			executors.execute(() -> processor.process(target, event));
		}
	}
	
	public synchronized void exit() {
		exit = true;
		this.notify();
	}
	
	private void doTask() throws InterruptedException {
		List<GameEvent> lastEvents;
		synchronized (this) {
			if(events.isEmpty()){
				this.wait();
			}
			lastEvents = events;
			events = new ArrayList<>();
		}
		for(int i = 0; i < lastEvents.size() && !exit; i++){
			GameEvent event = lastEvents.get(i);
			if(exitEven.equals(event.getType())){
				exit = true;
			} else {
				process(event);
			}
		}
	}

	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!exit){
			try{
				doTask();
			}catch (InterruptedException ex) {
				// TODO: handle exception
				LOGGER.error("GameEventDispatcher<" +  target.getClass() + ">.run(): " + target, ex);
			}
		}
		executors.shutdown();
		
	}

	@Override
	public void dispatcher(GameEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	
}
