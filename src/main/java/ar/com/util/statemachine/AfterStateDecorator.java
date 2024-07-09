package ar.com.util.statemachine;

public class AfterStateDecorator<T> implements IStateTrigger<T> {
	
	private final IStateTrigger<T> state;
	private final Runnable listener;
	
	public AfterStateDecorator(IStateTrigger<T> state, Runnable listener) {
		this.state = state;
		this.listener = listener;
	}
	@Override
	public boolean execute(T context) {
    	boolean result = state.execute(context);
    	if (result) {
        	listener.run();
    	}
    	return result;
	}
}
