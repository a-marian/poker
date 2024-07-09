package ar.com.poker.dispatcher;

public interface IGameEventDispatcher<E extends Enum> extends Runnable {

	void dispatcher(GameEvent<E> event);
	 void exit();
}
