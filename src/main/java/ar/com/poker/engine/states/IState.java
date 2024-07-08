package ar.com.poker.engine.states;

public interface IState<T> {

    String getName();
    boolean execute(T context);
}
