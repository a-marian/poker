package ar.com.poker.api.core;

/***
 * Cuando una clase tiene por delante el modificador final
 * es una clase no extensible 
 * Al definir sus stributos inmutables  la clase se hace inmutable,
 * es decir es una estrictura fija que no tendra clases hijas*/
public final class Card {
	
	private static final String STRING_RANK_CARDS = "23456789TJQKA";
	public static enum Suit {
		SPADE('♠'), HEART('♥'), DIAMOND('♦'), CLUB('♣');
		private Suit(char c) {
			this.c = c;
		}
		private final char c;
	}
	
	public  static enum Rank {
		TWO, TRHEE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
	}
	
	
	private final Suit suit;
	private final Rank rank;
	
	public Card (Suit suit, Rank rank) {
		if (suit == null) {
			throw new IllegalArgumentException("suit no puede tener un valor nulo");
		}
		if (rank == null){
			throw new IllegalArgumentException("rank no puede tener valor nulo");
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
	
	//overriding method for descriptive result
	@Override
	public String toString() {
		// TODO Auto-generated method stu
		int rankValue = rank.ordinal();
		return STRING_RANK_CARDS.substring(rankValue, rankValue +1) + suit.c;
	  //	return STRING_RANK_CARDS.substring(rankValue, rankValue +1).concat(suit.name().substring(0, 1));
		//return "Card{"+"suit="+ suit +", rank="+ rank+"}";
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
