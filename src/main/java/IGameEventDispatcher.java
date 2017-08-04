
public interface IGameEventDispatcher extends Runnable {

	public void dispatcher(GameEvent event);
	public void exit();
}
