package ar.com.poker.engine.states;

import java.util.List;

import ar.com.poker.api.game.TexasHoldEmUtil.PlayerState;
import ar.com.poker.engine.model.ModelContext;
import ar.com.poker.engine.model.PlayerEntity;
import ar.com.util.statemachine.IState;

/*
 * el metodo execute emple streams
 * Los streams vienen con una funcionalidad muy interesante, 
 * admiten filtros, colectores, agrupadores, mapeo de tipo a otro, saltos de elementos, ejecutar acciones en medio, 
 * ordenar elementos, eliminar duplicados, reducir el flujo, etc... */

public class WinnerState implements IState<ModelContext>{

	public static final String NAME ="Winner";
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public boolean execute(ModelContext context) {
		List<PlayerEntity> players = context.getPlayers();
		players.stream()
				.filter(p -> p.isActive() || p.getState() == PlayerState.ALL_IN)
				.findFirst()
				.get()
				.addChips(players.stream()
						.mapToLong(p-> p.getBet()).sum());
		return true;
	}

	
}
