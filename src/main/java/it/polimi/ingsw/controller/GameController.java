package it.polimi.ingsw.controller;

import it.polimi.ingsw.server.GamesRegistry;


public class GameController {
    private static final ThreadLocal<GameController> instance = ThreadLocal.withInitial(GameController::new);

    /**
     * Returns the thread local singleton instance
     */
    public static GameController getInstance() {
        return instance.get();
    }

    private GameController() {

    }

    public void startGame(String gameId) {
        GamesRegistry.getInstance().addThreadLocalGame(gameId);
        // TODO ENTRY POINT OF THE GAME, WHEN THIS METHOD ENDS, THE THREAD DIES

    }

}
