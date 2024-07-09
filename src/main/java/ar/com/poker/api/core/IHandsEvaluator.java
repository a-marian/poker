package ar.com.poker.api.core;

/***
 * definir interfaces y utilizarlas en el tipo de los objetos en lugar
 *  de clases concretas facilitan la adaptabilidad del código */
@FunctionalInterface
public interface IHandsEvaluator {
	
	int eval(Card[] cards);

}
