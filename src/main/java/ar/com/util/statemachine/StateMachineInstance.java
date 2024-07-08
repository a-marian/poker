package ar.com.util.statemachine;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class StateMachineInstance<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(StateMachineInstance.class);
	
	 private final T context;
	 private final StateMachine<T> parent;
	 private IState<T> state;
	 private boolean finish;
	 private boolean pause;
	public StateMachineInstance(T context, StateMachine<T> parent, IState<T> state){
		 this.context = context;
		 this.parent = parent;
		 this.state = state;
	     this.finish = false;
	 }
	 
	 
	 public boolean  isFinish(){
		 return finish;
	 }
	 
	 public StateMachineInstance<T>execute(){
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
	 
	 private IState<T> executeState(){
		 LOGGER.info("state \"{}\"executing...", state);
		 pause = ! state.execute(context);
		 IState<T>result = state;
		 if(!pause){
			 LOGGER.info("state \"{}\" [executed]", state);
			 for (Transition<T> transition : parent.getTransitionsByOrigin(state)) {
	                if (transition.getChecker().check(context)) {
	                    return transition.getTarget();
	                }
			 	}
	            result = parent.getDefaultTransition(state);
		 } else {
			 LOGGER.info("state \"{}\" [paused]", state);
		 }
		 return result;
	 }
}
