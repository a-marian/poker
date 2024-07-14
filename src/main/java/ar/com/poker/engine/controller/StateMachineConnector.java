package ar.com.poker.engine.controller;

import java.util.Collections;
import java.util.Map;
import ar.com.poker.api.core.Deck;
import ar.com.poker.api.game.BetCommand;
import ar.com.poker.api.game.Settings;
import ar.com.poker.api.game.TexasHoldEmUtil;
import ar.com.poker.dispatcher.GameEvent;
import ar.com.poker.dispatcher.IGameEventDispatcher;
import ar.com.poker.engine.model.ModelContext;
import ar.com.poker.engine.model.ModelUtil;
import ar.com.poker.engine.states.BetRoundTrigger;
import ar.com.poker.engine.states.CheckTrigger;
import ar.com.poker.engine.states.EndHandState;
import ar.com.poker.engine.states.InitHandTrigger;
import ar.com.poker.engine.states.PokerStates;
import ar.com.poker.engine.states.ShowDownTrigger;
import ar.com.poker.engine.states.WinnerTrigger;
import ar.com.util.statemachine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ar.com.util.timer.IGameTimer;

import static ar.com.poker.engine.controller.GameController.SYSTEM_CONTROLLER;

public class StateMachineConnector {


    private static final Logger LOGGER = LoggerFactory.getLogger(StateMachineConnector.class);

    private static final int END_HAND_SLEEP_TIME = 1000;
    private final StateMachine<PokerStates, ModelContext> texasStateMachine = buildStateMachine();
    private final Map<String, IGameEventDispatcher<PokerEventType>> playersDispatcher;
    private final IGameTimer timer;
    private ModelContext model;
    private IGameEventDispatcher<ConnectorGameEventType> system;
    private StateMachineInstance<PokerStates, ModelContext> instance;
    private long timeoutId = 0;
    private Map<String, Double> scores;

    public StateMachineConnector(IGameTimer t,Map<String,IGameEventDispatcher<PokerEventType>> pd){
    	this.playersDispatcher = pd;
    	this.timer = t;
    }
    
    public void setSystem(IGameEventDispatcher<ConnectorGameEventType> system) {
    	this.system = system;
    }
    
    public void createGame(Settings settings) {
    	if (model == null) {
    		LOGGER.debug("createGame: {}", settings);
    		model = new ModelContext(settings);
    		model.setDealer(-1);
    	} 
    }
    
    public void addPlayer(String playerName) { 
    	if (model != null) {
    		LOGGER.debug("addPlayer: \"{}\"", playerName);
    		model.addPlayer(playerName);
    	}
    }
    
    public void startGame() {
    		LOGGER.debug("startGame");
    	if (instance == null && model != null) {
    		model.setDeck(new Deck());
    		instance = texasStateMachine.startInstance(model);
    		model.setDealer(0);
    		execute();
    	}
    }

    public void betCommand(String playerName, BetCommand command) {
    	LOGGER.debug("betCommand: {} -> {}", playerName, command);
    if (instance != null && playerName.equals(model.getPlayerTurnName())) {
            BetCommand betCommand = command;
            if (betCommand == null) {
                betCommand = new BetCommand(TexasHoldEmUtil.BetCommandType.ERROR);
            	}
            model.getPlayerByName(playerName).setBetCommand(betCommand);
            execute();
    		}
    }
    
    public void timeOutCommand(Long timeoutId) {
    	LOGGER.debug("timeOutCommand: id: {}", timeoutId);
    	if (instance != null && timeoutId == this.timeoutId) {
            LOGGER.debug("timeOutCommand: player: {}", model.getPlayerTurnName());
            model.getPlayerByName(model.getPlayerTurnName()).setBetCommand(
            		new BetCommand(TexasHoldEmUtil.BetCommandType.TIMEOUT));
            execute();
            }
    }
    
    private void execute(){
        if (instance.execute().isFinish()) {
            model.setGameState(TexasHoldEmUtil.GameState.END);
            model.setCommunityCards(Collections.emptyList());
            notifyEndGame();
            instance = null;
        }
    }
    private void notifyInitHand() {
        notifyEvent(PokerEventType.INIT_HAND);
    }
    
    private void notifyBetCommand() {
        String playerTurn = model.getLastPlayerBet().getName();
        BetCommand lbc = model.getLastBetCommand();
        LOGGER.debug("notifyBetCommand -> {}: {}", playerTurn, lbc);
        playersDispatcher.entrySet().stream().forEach(
                entry -> entry.getValue().dispatch(
                        new GameEvent<>(PokerEventType.CHECK, SYSTEM_CONTROLLER, model.getCommunityCards())));

    }

    void notifyCheck() {
        LOGGER.debug("notifyCheck: {}", GameController.CHECK_PLAYER_EVENT_TYPE);
        playersDispatcher.entrySet().stream().forEach(
                entry -> entry.getValue().dispatch(
                        new GameEvent<>(PokerEventType.CHECK, SYSTEM_CONTROLLER, model.getCommunityCards())));
    }

    private void notifyPlayerTurn() {
        String playerTurn = model.getPlayerTurnName();
        if (playerTurn != null) {
            LOGGER.debug("notifyPlayerTurn -> {}", playerTurn);
            playersDispatcher.get(playerTurn).dispatch(
                    new GameEvent<>(PokerEventType.GET_COMMAND,
                            GameController.SYSTEM_CONTROLLER, PlayerAdapter.toTableState(model, playerTurn)));
        }
        timer.changeTimeoutId(++timeoutId);
    }

    private void notifyEndHand() {
        notifyEvent(PokerEventType.END_HAND);
    }

    private void notifyEvent(PokerEventType type) {
        LOGGER.debug("notifyEvent: {} -> {}", type, model);
        playersDispatcher.entrySet().stream().forEach(entry
                -> entry.getValue().dispatch(
                new GameEvent<>(type, GameController.SYSTEM_CONTROLLER, PlayerAdapter.toTableState(model, entry.getKey()))));
    }

    private void notifyEndGame() {
        LOGGER.debug("notifyEvent: {} -> {}", PokerEventType.END_GAME, model);
        scores = model.getScores();
        playersDispatcher.entrySet().forEach(entry
                -> entry.getValue().dispatch(
                        new GameEvent<>(PokerEventType.END_GAME, SYSTEM_CONTROLLER, scores)));
        system.dispatch(new GameEvent<>(ConnectorGameEventType.EXIT, SYSTEM_CONTROLLER));
        notifyEvent(PokerEventType.EXIT);
    }

    public Map<String, Double> getScores() {
        return scores;
    }

    private StateMachine<PokerStates, ModelContext> buildStateMachine() {
        final IStateTrigger<ModelContext> initHandTrigger = StateDecoratorBuilder.after(new InitHandTrigger(), () -> notifyInitHand());
        final IStateTrigger<ModelContext> betRoundTrigger = StateDecoratorBuilder
                .create(new BetRoundTrigger())
                .before(() -> notifyPlayerTurn())
                .after(() -> notifyBetCommand())
                .build();
        final IStateTrigger<ModelContext> checkTrigger = StateDecoratorBuilder.after(new CheckTrigger(), () -> notifyCheck());
        final IStateTrigger<ModelContext> showDownTrigger = new ShowDownTrigger();
        final IStateTrigger<ModelContext> winnerTrigger = new WinnerTrsigger();
        final IStateTrigger<ModelContext> endHandTrigger = StateDecoratorBuilder.before(new EndHandTrigger(), () -> notifyEndHand());
        final IStateTrigger<ModelContext> endGameTrigger = StateDecoratorBuilder.after(new EndGameTrigger(), () -> notifyEndGame());

        return StateMachineBuilder.create(PokerStates.class, ModelContext.class)
                .initState(PokerStates.INIT_HAND)
                .stateTrigger(PokerStates.INIT_HAND, initHandTrigger)
                .stateTrigger(PokerStates.BET_ROUND, betRoundTrigger)
                .stateTrigger(PokerStates.CHECK, checkTrigger)
                .stateTrigger(PokerStates.WINNER, winnerTrigger)
                .stateTrigger(PokerStates.SHOWDOWN, showDownTrigger)
                .stateTrigger(PokerStates.END_HAND, endHandTrigger)
                .stateTrigger(PokerStates.END_GAME, endGameTrigger)
                .stateTrigger(PokerStates.CHECK, checkTrigger)
                
                .transition(PokerStates.INIT_HAND, PokerStates.BET_ROUND)
                .transition(PokerStates.BET_ROUND, PokerStates.BET_ROUND, m -> m.getPlayerTurn() != ModelUtil.NO_PLAYER_TURN)
                .transition(PokerStates.BET_ROUND, PokerStates.WINNER, m -> m.getPlayersAllIn() + m.getActivePlayers() == 1)
                .transition(PokerStates.BET_ROUND, PokerStates.CHECK)
                .transition(PokerStates.CHECK, PokerStates.SHOWDOWN, m -> m.getGameState() == TexasHoldEmUtil.GameState.SHOWDOWN)
                .transition(PokerStates.CHECK, PokerStates.BET_ROUND, m -> m.getPlayerTurn() != ModelUtil.NO_PLAYER_TURN)
                .transition(PokerStates.CHECK, PokerStates.CHECK)
                .transition(PokerStates.WINNER, PokerStates.END_HAND)
                .transition(PokerStates.SHOWDOWN, PokerStates.END_HAND)
                .transition(PokerStates.END_HAND, PokerStates.INIT_HAND, m -> m.getNumPlayers() > 1 && m.getRound() < m.getSettings().getMaxRounds())
                .transition(PokerStates.END_HAND, PokerStates.END_GAME).build();
    }
}
