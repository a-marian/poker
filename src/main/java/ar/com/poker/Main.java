package ar.com.poker;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.poker.api.core.Card;

public class Main {
	
	// los loggers deben ser privados estaticos y finales 
	// siempre definir al principio de la clase
	//
	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	
	private static void insert(Set<Card> cards, Card card){
		if(!cards.contains(card)){
			LOGGER.info("insertamos la carta: {}", card);
			cards.add(card);
		}else{
			LOGGER.info("La carta: {} ya estaba en el conjunto", card);
		}
	}
	
	public static void main(String[] args) {

		/**Card card = new Card(Card.Suit.CLUB, Card.Rank.ACE);
		//System.out.println("As de trebols: " + card);
		LOGGER.info("As de tréboles: {}" , card);
		**/
			
		Set<Card> cards = new HashSet<Card>();
		
		Card[] cards2Insert = {
				new Card(Card.Suit.CLUB, Card.Rank.ACE),
				new Card(Card.Suit.CLUB, Card.Rank.TWO),
				new Card(Card.Suit.CLUB, Card.Rank.TRHEE),
				new Card(Card.Suit.CLUB, Card.Rank.ACE),
				new Card(Card.Suit.CLUB, null),
		};
		for(Card card : cards2Insert) {
			insert(cards, card);
		}
	}

}
