package ar.com.poker.dispatcher;

@FunctionalInterface
public interface IGameEventProcessor<T> {
	
	public void process(T target, GameEvent event);

}
