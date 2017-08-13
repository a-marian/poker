package ar.com.util.timer;

@FunctionalInterface
public interface TimeoutNotifier {

	public void notify(Long timeoutId) ;
}
