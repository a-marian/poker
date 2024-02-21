package ar.com.poker.engine.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ar.com.util.timer.GameTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.poker.api.game.IGameController;
import ar.com.poker.api.game.IStrategy;
import ar.com.poker.api.game.Settings;
import ar.com.poker.dispatcher.GameEventDispatcher;
import ar.com.poker.dispatcher.IGameEventDispatcher;
import ar.com.poker.dispatcher.IGameEventProcessor;
import ar.com.util.timer.IGameTimer;

public class GameController implements IGameController, Runnable{


    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);
    private static final int DISPATCHER_THREADS = 1;
    private static final int EXTRA_THREADS = 2;
    public static final String SYSTEM_CONTROLLER = "system";
    public static final String INIT_HAND_EVENT_TYPE = "initHand";
    public static final String BET_COMMAND_EVENT_TYPE = "betCommand";
    public static final String END_GAME_PLAYER_EVENT_TYPE = "endGame";
    public static final String END_HAND_PLAYER_EVENT_TYPE = "endHand";
    public static final String CHECK_PLAYER_EVENT_TYPE = "check";
    public static final String GET_COMMAND_PLAYER_EVENT_TYPE = "getCommand";
    public static final String EXIT_CONNECTOR_EVENT_TYPE = "exit";
    public static final String ADD_PLAYER_CONNECTOR_EVENT_TYPE = "addPlayer";
    public static final String TIMEOUT_CONNECTOR_EVENT_TYPE = "timeOutCommand";
    public static final String CREATE_GAME_CONNECTOR_EVENT_TYPE = "createGame";
    private final Map<String, IGameEventDispatcher> players = new HashMap<>();
    private final List<String> playersByName = new ArrayList<>();
    private final Map<String, IGameEventProcessor<IStrategy>> playerProcessors;
    private final GameEventDispatcher<StateMachineConnector> connectorDispatcher;
    private final StateMachineConnector stateMachineConnector;
    private final IGameTimer timer;
    private Settings settings;
    private ExecutorService executors;
    private List<ExecutorService> subExecutors = new ArrayList<>();
public GameController() {
timer = new GameTimer(SYSTEM_CONTROLLER, buildExecutor(DISPATCHER_THREADS)); stateMachineConnector = new StateMachineConnector(timer, players); connectorDispatcher = new GameEventDispatcher<>(stateMachineConnector,
                                                        buildConnectorProcessors(),
                                                        buildExecutor(1));
        stateMachineConnector.setSystem(connectorDispatcher);
        timer.setDispatcher(connectorDispatcher);
        playerProcessors = buildPlayerProcessors();
    }
private ExecutorService buildExecutor(int threads){ ExecutorService result = Executors.newFixedThreadPool(threads); subExecutors.add(result);
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
    public void run() {

    }
}
