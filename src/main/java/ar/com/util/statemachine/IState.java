package ar.com.util.statemachine;

@FunctionalInterface
public interface IState<T> {

	
		public boolean execute(T context);
		
}
