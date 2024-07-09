package ar.com.util.statemachine;

public class Transition <S extends Enum, T>{

		private final S origin;
		private final S target;
		private final IChecker<T> checker;
		
		public Transition(S origin, S target, IChecker<T> checker) {
			this.origin = origin;
			this.target = target;
			this.checker = checker;
		}

	public S getOrigin() {
		return origin;
	}

	public S getTarget() {
		return target;
	}

	public IChecker<T> getChecker() {
		return checker;
	}
}
