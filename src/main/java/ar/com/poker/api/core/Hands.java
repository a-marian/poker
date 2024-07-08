package ar.com.poker.api.core;

public final class Hands {

	public static final int CARDS = 5;
	
	
	/***
	 * representa el tipo de ctegoria de mano ordendos de menor valor a mayor
	 ***/
	public enum Type {
		HIGH_CARD,
		ONE_PAIR,
		TWO_PAIR,
		THREE_OF_A_KIND,
		STRAIGHT,
		FLUSH,
		FULL_HOUSE,
		FOUR_OF_A_KIND,
		STRAIGHT_FLUSH
		
	}
	
	private Hands(){}
	
}
