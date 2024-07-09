package ar.com.util.statemachine;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class StateMachineInstance<S extends Enum, T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(StateMachineInstance.class);
	 private final T context;
	 private final StateMachine<S,T> parent;
	 private S state;
	 private boolean finish;
	 private boolean pause;
	public StateMachineInstance(T context, StateMachine<S,T> parent, S state){
		 this.context = context;
		 this.parent = parent;
		 this.state = state;
	     this.finish = false;
	 }
	 
	 
	 public boolean  isFinish(){
		 return finish;
	 }
	 
	 public StateMachineInstance<S, T>execute(){
		 this.pause = false;
		 while(state != null && !pause){
			 state = executeState();
		 }
		 finish = state == null;
		 if(finish){
			 LOGGER.info("execute finish");
		 }
		 return this;
	 }
	 
	 public T getContext(){
		 return context;
	 }
	 
	 private S executeState(){
		 LOGGER.debug("state \"{}\"executing...", state);
		 pause = ! parent.getTrigger(state).execute(context);
		 S result = state;
		 if(!pause){
			 LOGGER.info("state \"{}\" [executed]", state);
			 for (Transition<S, T> transition : parent.getTransitionsByOrigin(state)) {
	                if (transition.getChecker().check(context)) {
	                    return transition.getTarget();
	                }
			 	}
	            result = null;
		 } else {
			 LOGGER.info("state \"{}\" [paused]", state);
		 }
		 return result;
	 }
}
