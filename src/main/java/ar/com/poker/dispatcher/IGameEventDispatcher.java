package ar.com.poker.dispatcher;

public interface IGameEventDispatcher<E extends Enum> extends Runnable {

	void dispatch(GameEvent<E> event);
	 void exit();
}
