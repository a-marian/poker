package ar.com.util.statemachine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class StateMachine<S extends Enum, T> {

	private static final IStateTrigger DEFAULT_TRIGGER = context -> true;
	private final S initState;
	private final Map<S, IStateTrigger<T>> triggersByState;
	private final Map<S, List<Transition<S,T>>> transitions;

	public StateMachine(S initState, Map<S, IStateTrigger<T>> triggersByState,
						Map<S, List<Transition<S, T>>> transitions) {
		this.initState = initState;
		this.triggersByState = new HashMap<>(triggersByState);
		this.transitions = new HashMap<>(transitions.size());
		//transitions.entrySet().stream().forEach(e -> this.transitions.put(e.getKey(), new ArrayList<>(e.getValue())));
		transitions.forEach((key, value) -> this.transitions.put(key, new ArrayList<>(value)));

	}
	List<Transition<S,T>> getTransitionsByOrigin(S state){
		List<Transition<S, T>> result = transitions.get(state);
		if (result == null){
			result = Collections.emptyList();
		}
		return result;
	}

	public IStateTrigger<T> getTrigger(S state){
		IStateTrigger<T> result = triggersByState.get(state);
		if(result == null){
			result = (IStateTrigger<T>) DEFAULT_TRIGGER;
		}
		return result;
	}

	public StateMachineInstance<S, T> startInstance(T data) {
	return new StateMachineInstance((Object) data, (StateMachine) this, initState).execute();
	}
}
