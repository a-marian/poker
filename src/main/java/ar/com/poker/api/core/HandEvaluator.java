package ar.com.poker.api.core;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ar.com.poker.ExceptionUtil;
import ar.com.poker.api.core.Hands.Type;

public class HandEvaluator implements IHandsEvaluator{

	
	private static final int ENCODE_BASE = Card.Rank.ACE.ordinal() + 1;
	private static final int INDEXES_LENGTH = 2;
	private static final int RANK_INDEX = 0;
	private static final int REPEATS_INDEX = 1;
	private static final Type[][] MATRIX_TYPES = {
		        {Type.HIGH_CARD}, {Type.ONE_PAIR, Type.TWO_PAIR},
		        {Type.THREE_OF_A_KIND, Type.FULL_HOUSE},{Type.FOUR_OF_A_KIND}
		    };
	private final int[][] indexes = new int [Hands.CARDS][INDEXES_LENGTH];
	private final int [] ranks  = new int [ENCODE_BASE];
	private final int[] suits = new int [Card.Suit.values().length];
	private boolean isStraight = false;
	private boolean isFlush = false;
	
	
	
	public int eval(Card[] cards) {
		// TODO Auto-generated method stub
		ExceptionUtil.checkArrayLengthArgument(cards, "cards", Hands.CARDS);
		isFlush = false;
		Arrays.fill(suits, 0);
		Arrays.fill(ranks, 0);
		
		int index = 0;
		Set<Card> previousCards = new HashSet<Card>(Hands.CARDS);
		for(Card card: cards){
			ExceptionUtil.checkNullArgument(card, "card["+ (index++) +"]");
			ExceptionUtil.checkArgument(previousCards.contains(card), "La carta{} esta repetida", card);
			previousCards.add(card);
			ranks[card.getRank().ordinal()]++;
			suits[card.getSuit().ordinal()]++;
		}
		isFlush = suits[cards[0].getSuit().ordinal()] == Hands.CARDS;
		isStraight = false;
		int straightCounter = 0;
		int j = 0;
		for(int i = ranks.length -1 ; i >=0; i--){
			if(ranks[i] > 0){
				straightCounter++;
				isStraight = straightCounter == Hands.CARDS;
				indexes[j] [RANK_INDEX] = i;
				indexes[j][REPEATS_INDEX] = ranks[i];
				upIndex(j++);
			}else{
				straightCounter = 0;
			}
		}
		isStraight = isStraight || checkStraight5toAce(straightCounter);
		return calculateHandValue();
	}
	
	//actualiza el orden de los pares en los indices
		private void upIndex(int i){
			int k = i;
			while (k > 0 && indexes[k-1][REPEATS_INDEX] < indexes[k][REPEATS_INDEX]){
				int[] temp = indexes[k-1];
				indexes[k-1] = indexes[k];
				indexes[k] = temp;
				k--;
			}
		}
		
		private boolean checkStraight5toAce(int straightCntr){
			boolean straight5toAce = false;
			//Evalua si setrata del caso especial
			if(ranks[Card.Rank.ACE.ordinal()] == 1 && straightCntr == Hands.CARDS - 1){
				//Si es el caso especial hay que reorganizar los indices
				straight5toAce = true;
				for (int i = 0; i < indexes.length; i++) {
					
				}
			} 
			return straight5toAce;
		}
		
		private  int calculateHandValue(){
			Type type;
			if(isStraight){
				type = isFlush ? Type.STRAIGHT_FLUSH : Type.STRAIGHT;
			} else if(isFlush){
				type = Type.FLUSH;
			}else {
				type = MATRIX_TYPES[indexes[0][REPEATS_INDEX] - 1][indexes[1][REPEATS_INDEX] -1];
			}
			return encodeValue(type, indexes);
		}
		
		private static int encodeValue(Type type, int[][] indexes){
			int result = type.ordinal();
			int i=0;
			int j=0;
			while(j < Hands.CARDS){
				for (int k = 0; k < indexes[i][REPEATS_INDEX]; k++) {
					result = result * ENCODE_BASE + indexes[i][RANK_INDEX];
					j++;
				}
				i++;
			}
			return result;
		}
	
}
