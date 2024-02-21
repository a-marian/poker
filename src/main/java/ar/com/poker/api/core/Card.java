package ar.com.poker.api.core;

/***
 * Cuando una clase tiene por delante el modificador final
 * es una clase no extensible 
 * Al definir sus stributos inmutables  la clase se hace inmutable,
 * es decir es una estrictura fija que no tendra clases hijas*/
public final class Card {
	
	private static final String STRING_RANK_CARDS = "23456789TJQKA";
	public enum Suit {
		SPADE('♠'), HEART('♥'), DIAMOND('♦'), CLUB('♣');
		Suit(char c) {
			this.c = c;
		}
		private final char c;
	}
	
	public enum Rank {
		TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
	}
	
	
	private final Suit suit;
	private final Rank rank;
	
	public Card (Suit suit, Rank rank) {
		if (suit == null) {
			throw new IllegalArgumentException("Suit can't be null");
		}
		if (rank == null){
			throw new IllegalArgumentException("Rank can't be null");
		}
		
		this.suit = suit;
		this.rank = rank;
	}
	
	public Suit getSuit() {
		return suit;
	}
	
	public Rank getRank(){
		return rank;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stu
		int rankValue = rank.ordinal();
		return STRING_RANK_CARDS.substring(rankValue, rankValue +1) + suit.c;

	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return rank.ordinal() * Suit.values().length + suit.ordinal();
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		boolean result = true;
		result = false;
		if(obj != null && getClass() == obj.getClass()){
				result = hashCode() == ((Card) obj).hashCode();
		}
		return result;
	}

}
