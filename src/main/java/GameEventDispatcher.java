import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


public class GameEventDispatcher<E extends Enum, T> implements IGameEventDispatcher<E> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GameEventDispatcher.class);

	public static final String EXIT_EVENT_TYPE = "exit";
	private final Map<String, IGameEventProcessor<E,T>> procesors;
	private final T target;
	private final E exitEven;
	private List<GameEvent> events = new ArrayList<GameEvent>();
	private volatile boolean exit = false;
	private ExecutorService executors;
	
	public GameEventDispatcher(T target, Map<E, IGameEventProcessor<E,T>> processors, ExecutorService executors, E exitEven){
		this.target = target;
        this.processors = processors;
        this.executors = executors;
        this.exitEven = exitEven;
	}
	
	public synchronized void dispatch(GameEvent<E> event){
		events.add(event);
		this.notify();
	}
	
	private void process(GameEvent<E> event) {
        IGameEventProcessor<E, T> processor = processors.get(event.getType());
        if (processor != null) {
            executors.execute(() -> processor.process(target, event));
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
				LOGGER.error("GameEventDispatcher<" + target.getClass() + ">.run(): " + target, ex);
			}
		}
		executors.shutdown();
		
	}
	@Override
	public void dispatcher(GameEvent event) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public synchronized exit() {
		// TODO Auto-generated method stub
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
			if(EXIT_EVENT_TYPE.equals(event.getType())){
				exit = true;
			} else {
				process(event);
			}
		}
		
		
	}
	
}
