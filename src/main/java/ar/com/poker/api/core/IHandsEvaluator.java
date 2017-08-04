package ar.com.poker.api.core;

/***
 * definir interfaces y utilizarlas en el tipo de los objetos en lugar
 *  de clases concretas facilitan la adaptabilidad del código */
public interface IHandsEvaluator {
	
	public int eval(Card[] cards);

}
