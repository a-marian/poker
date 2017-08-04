package ar.com.poker.api.core;
import static ar.com.poker.api.core.Deck.getAllCards;

import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public final class CardUtil4Testing {
	
	private static final String ARGUMENTO_NO_VALIDO = "Argumento {} no valido";
	private static final int CARD_STRING_LENGTH = 2;
	private static final char SEPARATOR =' ';
	private static final Map<String, Card> STRING_TO_CARD = getAllCards().stream().collect(Collectors.groupingBy(Card::toString, Collectors.reducing(null,(c,t) -> t)));
	
	private CardUtil4Testing(){
	}
	
	public static Card fromString(String s){
		Card result = null;
		if(s != null){
			result = STRING_TO_CARD.get(s);
		}
		return result;
	}
	
	public static Card[] fromStringCards(String s) {
		StringTokenizer st = new StringTokenizer(s);
		Card[] result = new Card[st.countTokens()];
		int i = 0;
		while(st.hasMoreTokens()){
			result[i++] = fromString(st.nextToken());
		}
		return result;
	} 
	
	public static String toStringCards(Card... c){
		String result = null;
		if(c != null && c.length >0){
			StringBuilder sb = new StringBuilder(c.length * (CARD_STRING_LENGTH + 1) -1);
			sb.append(c[0]);
			for (int i = 1; i < c.length; i++) {
				sb.append(SEPARATOR);
				sb.append(c[i]);
			}
			result = sb.toString();
		}
		return result;
	}
	

}
