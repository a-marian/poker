package ar.com.util.statemachine;

@FunctionalInterface
public interface IChecker<T> {

	public boolean check(T context);
		
}
