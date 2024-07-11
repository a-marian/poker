package ar.com.poker.engine.controller;
/*
TODO remove
* https://github.com/dperezcabrera/jpoker/tree/master
* */
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ar.com.poker.api.core.Card;
import ar.com.poker.api.game.*;
import ar.com.poker.dispatcher.GameEvent;
import ar.com.util.timer.GameTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.poker.dispatcher.GameEventDispatcher;
import ar.com.poker.dispatcher.IGameEventDispatcher;
import ar.com.poker.dispatcher.IGameEventProcessor;
import ar.com.util.timer.IGameTimer;

public class GameController implements IGameController, Runnable {


    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);
    private static final int DISPATCHER_THREADS = 1;
    private static final int EXTRA_THREADS = 2;
    public static final String SYSTEM_CONTROLLER = "system";
    public static final String INIT_HAND_EVENT_TYPE = "initHand";
    public static final String BET_COMMAND_EVENT_TYPE = "betCommand";
    public static final String END_HAND_PLAYER_EVENT_TYPE = "endHand";
    public static final String CHECK_PLAYER_EVENT_TYPE = "check";
    public static final String GET_COMMAND_PLAYER_EVENT_TYPE = "getCommand";

    private final Map<String, IGameEventDispatcher<PokerEventType>> players = new HashMap<>();
    private final List<String> playersByName = new ArrayList<>();
    private final List<ExecutorService> subExecutors = new ArrayList<>();
    private final Map<PokerEventType, IGameEventProcessor<PokerEventType, IStrategy>> playerProcessors;
    private final GameEventDispatcher<ConnectorGameEventType, StateMachineConnector> connectorDispatcher;
    private final StateMachineConnector stateMachineConnector;
    private final IGameTimer timer;
    private Settings settings;
    private ExecutorService executors;
    private boolean finish = false;

    public GameController() {
        timer = new GameTimer(SYSTEM_CONTROLLER, buildExecutor(DISPATCHER_THREADS));
        stateMachineConnector = new StateMachineConnector(timer, players);
        connectorDispatcher = new GameEventDispatcher<>(stateMachineConnector,
                buildConnectorProcessors(),
                buildExecutor(1), ConnectorGameEventType.EXIT);
        stateMachineConnector.setSystem(connectorDispatcher);
        timer.setNotifier(timeoutId -> connectorDispatcher.dispatch(new GameEvent<>(ConnectorGameEventType.TIMEOUT, SYSTEM_CONTROLLER, timeoutId)));
        playerProcessors = buildPlayerProcessors();
    }

    private static Map<ConnectorGameEventType, IGameEventProcessor<ConnectorGameEventType, StateMachineConnector>> buildConnectorProcessors(){
        Map<ConnectorGameEventType, IGameEventProcessor<ConnectorGameEventType, StateMachineConnector>> connectorProcessorsMap
                = new EnumMap<>(ConnectorGameEventType.class);
        connectorProcessorsMap.put(ConnectorGameEventType.CREATE_GAME, (connector, event)->  connector.createGame((Settings) event.getPayload()));
        connectorProcessorsMap.put(ConnectorGameEventType.ADD_PLAYER, (connector, event) -> connector.addPlayer(event.getSource()));
        connectorProcessorsMap.put(ConnectorGameEventType.INIT_GAME, (connector, event) -> connector.startGame());
        connectorProcessorsMap.put(ConnectorGameEventType.BET_COMMAND, (connector, event) -> connector.betCommand(event.getSource(), (BetCommand) event.getPayload()));
        connectorProcessorsMap.put(ConnectorGameEventType.TIMEOUT, (connector, event) -> connector.timeOutCommand((Long) event.getPayload()));
        return connectorProcessorsMap;
    }

    private Map<PokerEventType, IGameEventProcessor<PokerEventType, IStrategy>> buildPlayerProcessors(){
        Map<PokerEventType, IGameEventProcessor<PokerEventType, IStrategy>> playerProcessorsMap = new EnumMap<>(PokerEventType.class);
        playerProcessorsMap.put(PokerEventType.INIT_HAND, (strategy, event) -> strategy.initHand((GameInfo) event.getPayload()));
        playerProcessorsMap.put(PokerEventType.END_HAND, (strategy, event) -> strategy.endHand((GameInfo) event.getPayload()));
        playerProcessorsMap.put(PokerEventType.END_GAME, (strategy, event) -> strategy.endGame((Map<String, Double>) event.getPayload()));
        playerProcessorsMap.put(PokerEventType.BET_COMMAND, (strategy, event) -> strategy.onPlayerCommand(event.getSource(), (BetCommand) event.getPayload()));
        playerProcessorsMap.put(PokerEventType.CHECK, (strategy, event) -> strategy.check((List<Card>) event.getPayload()));
        playerProcessorsMap.put(PokerEventType.GET_COMMAND, (strategy, event) -> {
            GameInfo<PlayerInfo> gameInfo = (GameInfo<PlayerInfo>) event.getPayload();
            String playerTurn = gameInfo.getPlayers().get(gameInfo.getPlayerTurn()).getName();
            BetCommand command = strategy.getCommand(gameInfo);
            connectorDispatcher.dispatch(new GameEvent<>(ConnectorGameEventType.BET_COMMAND, playerTurn, command));
        });
        return playerProcessorsMap;
    }

    private ExecutorService buildExecutor(int threads) {
        ExecutorService result = Executors.newFixedThreadPool(threads);
        subExecutors.add(result);
        return result;
    }

    @Override
    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    @Override
    public boolean addStrategy(IStrategy strategy) {
        return false;
    }

    @Override
    public void start() {

    }

    @Override
    public void waitFinish() {

    }

    @Override
    public void stop() {

    }

    @Override
    public Map<String, Double> getScores() {
        return null;
    }

    @Override
    public void run() {

    }
}
