package ar.com.poker;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.poker.api.core.Card;

public class Main {

	private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
	
	private static void insert(Set<Card> cards, Card card){
		if(!cards.contains(card)){
			LOGGER.info("Insert card : {}", card);
			cards.add(card);
		}else{
			LOGGER.info("Card: {} is already in ", card);
		}
	}
	
	public static void main(String[] args) {
			
		Set<Card> cards = new HashSet<Card>();
		
		Card[] cards2Insert = {
				new Card(Card.Suit.CLUB, Card.Rank.ACE),
				new Card(Card.Suit.CLUB, Card.Rank.TWO),
				new Card(Card.Suit.CLUB, Card.Rank.THREE),
				new Card(Card.Suit.CLUB, Card.Rank.ACE),
				new Card(Card.Suit.CLUB, null),
		};
		for(Card card : cards2Insert) {
			insert(cards, card);
		}
	}

}
