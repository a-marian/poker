package ar.com.poker.engine.states;

import java.util.ArrayList;
import java.util.List;

import ar.com.poker.engine.model.ModelContext;
import ar.com.poker.engine.model.PlayerEntity;
import ar.com.util.statemachine.IState;

public class EndGameState implements IState<ModelContext> {
	public static final String NAME = "EndHand";
	

	public String getName() {
		// TODO Auto-generated method stub
		return NAME;
	}

	@Override
	public boolean execute(ModelContext context) {
		PlayerEntity dealerPlayer = context.getPlayer(context.getDealer());
        List<PlayerEntity> players = context.getPlayers();
        List<PlayerEntity> nextPlayers = new ArrayList<>(players.size());
        int i = 0;
        int dealerIndex = 0;
        for (PlayerEntity p : players) {
            if (p.getChips() > 0) {
                nextPlayers.add(p);
                i++;
            }
            if (dealerPlayer == p) {
                dealerIndex = i-1;
            }
        }
        context.setDealer(dealerIndex);
        context.setPlayers(nextPlayers);
        return true;
	}
	
	

}
