package ar.com.poker.engine.states;

import ar.com.poker.api.core.Deck;
import ar.com.poker.api.game.Settings;
import ar.com.poker.api.game.TexasHoldEmUtil;
import ar.com.poker.engine.model.ModelContext;
import ar.com.poker.engine.model.ModelUtil;
import ar.com.poker.engine.model.PlayerEntity;
import ar.com.util.statemachine.IStateTrigger;
import net.jcip.annotations.ThreadSafe;

import java.util.List;

import static ar.com.poker.api.game.TexasHoldEmUtil.MIN_PLAYERS;

@ThreadSafe
public class InitHandTrigger implements IStateTrigger<ModelContext> {
    @Override
    public boolean execute(ModelContext context) {
        context.setScores(null);
        Deck deck = context.getDeck();
        deck.shuffle();
        Settings settings = context.getSettings();
        context.setGameState(TexasHoldEmUtil.GameState.PRE_FLOP);
        context.clearCommunityCard();
        context.setRound(context.getRound() +1);
        if(context.getRound() % settings.getRounds4IncrementBlind() == 0){
            settings.setSmallBind(2 * settings.getSmallBind());
        }
        context.setPlayersAllIn(0);
        context.setHighBet(0L);
        List<PlayerEntity> players = context.getPlayers();
        for (PlayerEntity playerEntity : players) {
            playerEntity.setRoundsSurvival(context.getRound());
            playerEntity.setState(TexasHoldEmUtil.PlayerState.READY);
            playerEntity.setBet(0);
            playerEntity.setCards(deck.obtainCard(), deck.obtainCard());
        }
        int numPlayers = context.getNumPlayers();
        context.setActivePlayers(numPlayers);
        int dealerIndex = (context.getDealer()+1)%numPlayers;
        context.setDealer(dealerIndex);
        context.setPlayerTurn((dealerIndex+1)%numPlayers);
        if(numPlayers > MIN_PLAYERS){
            compulsoryBet(context, settings.getSmallBind());
        }
        compulsoryBet(context, settings.getBigBind());
        return true;
    }

    private void compulsoryBet(ModelContext context, long chips){
        int turn = context.getPlayerTurn();
        PlayerEntity playerEntity = context.getPlayer(turn);
        if(playerEntity.getChips() <= chips){
            playerEntity.setState(TexasHoldEmUtil.PlayerState.ALL_IN);
            ModelUtil.playerBet(context, playerEntity, TexasHoldEmUtil.BetCommandType.ALL_IN, playerEntity.getChips());
        } else {
            ModelUtil.playerBet(playerEntity, chips);
        }
        context.setHighBet(chips);
        context.setPlayerTurn((turn+1) % context.getNumPlayers());
    }
}
