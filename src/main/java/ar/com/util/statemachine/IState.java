package ar.com.util.statemachine;
public interface IState<T> {

	String getName();
	boolean execute(T context);
		
}
