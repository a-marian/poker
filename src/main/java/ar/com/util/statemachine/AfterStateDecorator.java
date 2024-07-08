package ar.com.util.statemachine;

public class AfterStateDecorator<T> implements IState<T> { 
	
	private final IState<T> state;
	private final Runnable listener;
	
	public AfterStateDecorator(IState<T> state, Runnable listener) { 
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
