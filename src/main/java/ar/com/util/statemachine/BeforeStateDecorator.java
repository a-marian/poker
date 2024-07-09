package ar.com.util.statemachine;

public class BeforeStateDecorator<T> implements IStateTrigger<T> {
	
	private final IStateTrigger<T> state;
	private final Runnable listener;
	private boolean executed = true;
	
	public BeforeStateDecorator(IStateTrigger<T> state, Runnable listener){
		this.state = state;
		this.listener = listener;
	}

	@Override
	public boolean execute(T context) {
		// TODO Auto-generated method stub
		if(executed){
			listener.run();
		}
		executed = state.execute(context);
		return executed;
	}

	
}
