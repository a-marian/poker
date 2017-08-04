package ar.com.poker;


import static org.junit.Assert.*;
import ar.com.poker.api.core.CardUtil4Testing;
import ar.com.poker.api.core.HandEvaluator;
import ar.com.poker.api.core.IHandsEvaluator;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class HandEvaluatorSteps {

	private static final String[] VALORES = {"mano0", "iguales", "mano1"};
	private IHandsEvaluator handEvaluator;
	private String resultado;
	
	@Given("^un IHandEvaluator$")
	public  void un_IHandEvaluator() throws Throwable{
		handEvaluator = new HandEvaluator();
	}
	
	@When("ˆcalculamos la comparacion entre (.*) y (.*)$")
	public void calculamos_la_comparacion(String h0, String h1) throws Throwable{
		int evalhand0 = handEvaluator.eval(CardUtil4Testing.fromStringCards(h0));
		int evalhand1 = handEvaluator.eval(CardUtil4Testing.fromStringCards(h1));
		
		int diferencia = evalhand1 - evalhand0;
		
		if(diferencia != 0){
			diferencia = Math.abs(diferencia) / diferencia;
		}
		resultado = VALORES[diferencia + 1];
	}
	
	@Then("^el resultado esperado es (.*)$")
	public void el_resultado_esperado_es(String expResult) throws Throwable {
		assertEquals(expResult, resultado);
	}
	
}
