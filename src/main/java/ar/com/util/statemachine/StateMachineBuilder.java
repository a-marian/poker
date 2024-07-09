package ar.com.util.statemachine;

import ar.com.poker.engine.states.IChecker;
import ar.com.poker.engine.states.IState;
import net.jcip.annotations.NotThreadSafe;

import javax.swing.plaf.nimbus.State;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NotThreadSafe
public class StateMachineBuilder<S extends Enum, T> {

    private static  final IChecker<?> DEFAULT_CHECKER = c -> true;

    private S initState = null;
    private Map<S, IStateTrigger<T>> triggerByState;
    private Map<S, List<Transition<S,T>>> transitions;

    public StateMachineBuilder() {
       init();
    }

    public void init(){
        triggerByState = new HashMap<>();
        transitions = new HashMap<>();
    }

    public static <S extends Enum, T> StateMachineBuilder<S, T> create(Class<S> statesType, Class<T> type){
        return new StateMachineBuilder<>();
    }

    public synchronized StateMachineBuilder<S, T> initState(S initState){
        this.initState = initState;
        return this;
    }

    public synchronized StateMachineBuilder<S, T> stateTrigger(S state, IStateTrigger<T> trigger){
       triggerByState.put(state, trigger);
        return this;
    }

    public synchronized StateMachineBuilder<S, T> addTransition(Transition<S, T> transition){
        S origin = transition.getOrigin();
        List<Transition<S, T>> listTransitions = transitions.get(origin);
        if(listTransitions == null){
            listTransitions = new ArrayList<>();
            transitions.put(origin, listTransitions);
        }
        listTransitions.add(transition);
        return this;
    }

    public StateMachineBuilder<S, T> transition(Transition<S, T> transition){
        return addTransition(transition);
    }
    public StateMachineBuilder<S, T> transition(S origin, S target, IChecker<T> checker){
        return addTransition(new Transition<>(origin, target, checker));
    }

    public StateMachineBuilder<S, T> transition(S origin, S target){
        return addTransition(new Transition<>(origin, target));
    }
    public synchronized StateMachine<S, T> build(){
        StateMachine<S, T> result = new StateMachine<>(initState, triggerByState, transitions);
        init();
        return result;
    }
}
