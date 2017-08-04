package ar.com.util.statemachine;

public interface IState<T> {

		public String getName();
		
		public boolean execute(T context);
		
}
