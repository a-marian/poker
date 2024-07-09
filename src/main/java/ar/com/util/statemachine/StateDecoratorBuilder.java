package ar.com.util.statemachine;

import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class StateDecoratorBuilder<T> {

	private IStateTrigger<T> trigger;
	
	private StateDecoratorBuilder(IStateTrigger<T> state){
		this.trigger = state;
	}
	
	public static <T> StateDecoratorBuilder<T> create(IStateTrigger<T> state){
		return new StateDecoratorBuilder<>(state);
	}
	
	public StateDecoratorBuilder<T>after(Runnable r){
		this.trigger = new AfterStateDecorator<>(trigger, r);
		return this;
	}
	
	public StateDecoratorBuilder<T>before(Runnable r){
		this.trigger = new BeforeStateDecorator<>(trigger, r);
		return this;
	}
	
	public IStateTrigger<T>build(){
		return trigger;
	}
	
	public static <T> IStateTrigger<T> after(IStateTrigger<T> state, Runnable r) {
		return new AfterStateDecorator<>(state, r);
	}
	
	public static <T> IStateTrigger<T> before(IStateTrigger<T> state, Runnable r) {
		return new BeforeStateDecorator<>(state, r);
	}
	
}
