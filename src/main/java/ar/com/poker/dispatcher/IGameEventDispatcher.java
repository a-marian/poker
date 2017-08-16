package ar.com.poker.dispatcher;

public interface IGameEventDispatcher<E extends Enum> extends Runnable {

	public void dispatcher(GameEvent<E> event);
	public void exit();
}
