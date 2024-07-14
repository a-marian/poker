package ar.com.poker.engine.states;

import ar.com.poker.api.game.PlayerInfo;
import ar.com.poker.api.game.TexasHoldEmUtil;
import ar.com.poker.engine.model.ModelContext;
import ar.com.poker.engine.model.ModelUtil;
import ar.com.util.statemachine.IStateTrigger;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class CheckTrigger implements IStateTrigger<ModelContext> {

    private static final TexasHoldEmUtil.GameState[] GAME_STATE = TexasHoldEmUtil.GameState.values();
    private static final int[] OBTAIN_CARDS = {3,1,1,0,0};

    private int indexByGameState(TexasHoldEmUtil.GameState gameState){
        int i=0;
        while(i < GAME_STATE.length && GAME_STATE[i] != gameState){
            i++;
        }
        return i;
    }
    @Override
    public boolean execute(ModelContext context) {
        int indexGameState = indexByGameState(context.getGameState());
        if(OBTAIN_CARDS[indexGameState] >0){
            context.addCommunityCards(OBTAIN_CARDS[indexGameState]);
        }
        context.setGameState(GAME_STATE[indexGameState+1]);
        context.setGameState(GAME_STATE[indexGameState+1]);
        context.setBets(0);
        context.getPlayers().stream().filter(PlayerInfo::isActive).forEach(p -> p.setState(TexasHoldEmUtil.PlayerState.READY));
        context.setPlayerTurn(ModelUtil.nextPlayer(context, context.getDealer()));
        context.setLastBetCommand(null);
        context.setLastPlayerBet(null);
        return true;
    }
}
