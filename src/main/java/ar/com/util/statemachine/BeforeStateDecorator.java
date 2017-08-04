package ar.com.util.statemachine;

public class BeforeStateDecorator<T> implements IState<T> {
	
	private final IState<T> state;
	private final Runnable listener;
	private boolean executed = true;
	
	public BeforeStateDecorator(IState<T> state, Runnable listener){
		this.state = state;
		this.listener = listener;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return state.getName();
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
