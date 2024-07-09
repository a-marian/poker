package ar.com.util.statemachine;

public interface IStateTrigger<T> {

    boolean execute(T context);
}
