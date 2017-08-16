package ar.com.poker.dispatcher;

@FunctionalInterface
public interface IGameEventProcessor<E extends Enum, T> {
	
	public void process(T target, GameEvent<E> event);

}
