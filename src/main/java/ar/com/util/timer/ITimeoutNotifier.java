package ar.com.util.timer;

@FunctionalInterface
public interface ITimeoutNotifier {

	public void notify(Long timeoutId);
}
