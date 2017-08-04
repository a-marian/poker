package ar.com.util.statemachine;

public class StateDecoratorBuilder<T> {

	private IState<T> state;
	
	private StateDecoratorBuilder(IState<T> state){
		this.state = state;
	}
	
	public static <T> StateDecoratorBuilder<T> create(IState<T> state){
		return new StateDecoratorBuilder<>(state);
	}
	
	public StateDecoratorBuilder<T>after(Runnable r){
		this.state = new AfterStateDecorator<>(state, r);
		return this;
	}
	
	public StateDecoratorBuilder<T>before(Runnable r){
		this.state = new BeforeStateDecorator<>(state, r);
		return this;
	}
	
	public IState<T>build(){
		return state;
	}
	
	public static <T> IState<T> after(IState<T> state, Runnable r) { 
		return new AfterStateDecorator<>(state, r);
	}
	
	public static <T> IState<T> before(IState<T> state, Runnable r) {
		return new BeforeStateDecorator<>(state, r);
	}
	
}
