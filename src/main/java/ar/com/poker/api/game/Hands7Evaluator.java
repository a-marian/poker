package ar.com.poker.api.game;

import java.util.List;

import ar.com.util.combinatorial.Combination;
import ar.com.poker.api.core.Card;
import ar.com.poker.api.core.IHandsEvaluator;
import net.jcip.annotations.NotThreadSafe;

import static ar.com.poker.api.game.TexasHoldEmUtil.COMMUNITY_CARDS;
import static ar.com.poker.api.game.TexasHoldEmUtil.PLAYER_CARDS;

@NotThreadSafe
public class Hands7Evaluator {
	
	public static final int TOTAL_CARDS = PLAYER_CARDS + COMMUNITY_CARDS;
	public final int [] combinatorialBuffer = new int[COMMUNITY_CARDS];
	private final Combination combinatorial = new Combination(COMMUNITY_CARDS, TOTAL_CARDS);
	
	private final IHandsEvaluator evaluator;
	private final Card[] evalBuffer = new Card[COMMUNITY_CARDS];
	private final Card[] cards = new Card[TOTAL_CARDS];
	private int communityCardsValue = 0;
	
	public Hands7Evaluator (IHandsEvaluator evaluator){
		this.evaluator = evaluator;
	}
	
	public void setCommunityCards(List<Card> cc){
		int i = 0;
		for(Card card : cc){
			evalBuffer[i] = card;
			cards[i++] = card; 
		}
		communityCardsValue = evaluator.eval(evalBuffer);
	}

	public int eval(Card c0, Card c1) {
		cards[COMMUNITY_CARDS] = c0; 
		cards[COMMUNITY_CARDS + 1] = c1; 
		return evalCards();
	}
	static Card[] copy(Card[] src, Card[] target, int[] positions) { int i = 0;
	        for (int p : positions) {
	            target[i++] = src[p];
	}
	        return target;
	    }
	private int evalCards() { 
		combinatorial.clear();
		combinatorial.next(combinatorialBuffer); 
		int result = communityCardsValue;
	        while (combinatorial.hasNext()) {
	            result = Math.max(result, evaluator.eval(
	                 copy(cards, evalBuffer, combinatorial.next(combinatorialBuffer))));
	        }
	        return result;
	}
	
}
