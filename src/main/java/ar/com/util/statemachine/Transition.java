package ar.com.util.statemachine;

public class Transition <T>{

		private final IState<T> origin;
		private final IState<T> target;
		private final IChecker<T> checker;
		
		public Transition(IState<T> origin, IState<T> target, IChecker<T> checker) { 
			this.origin = origin;
			this.target = target;
			this.checker = checker;
		}
		
		public IState<T> getOrigin() { 
			return origin;
		}
		
		public IState<T> getTarget() { 
			return target;
		}
		public IChecker<T> getChecker() { 
			return checker;
		}
}
