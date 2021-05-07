package it.polimi.ingsw.server;

import com.google.gson.Gson;
import it.polimi.ingsw.message.LoginMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;
import java.util.logging.Logger;

public class PlayerConnectionHandler implements Runnable{
    public static int MAX_LOGIN_ATTEMPT = 5;
    private final static Logger LOGGER = Logger.getLogger(PlayerConnectionHandler.class.getName());
    private Socket userSocket;

    private PrintWriter outputWriter;
    private BufferedReader inputReader;
    private Gson gson;


    public PlayerConnectionHandler(Socket userSocket) throws IOException {
        this.userSocket = userSocket;
        outputWriter = new PrintWriter(userSocket.getOutputStream(), true);
        inputReader = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
        gson = new Gson();
    }

    private Optional<LoginMessage> getLogin() {
        try {
            String json_login_message = inputReader.readLine();
            LoginMessage loginMessage = gson.fromJson(json_login_message, LoginMessage.class); // todo TRY-CATCH bad parsing
            return Optional.of(loginMessage);
        } catch (IOException | NullPointerException e) {
            return Optional.empty();
        }
    }

    private Optional<LoginMessage> getValidLogin() {
        LOGGER.info("Waiting for a login request");
        Optional<LoginMessage> loginMessageOptional = getLogin();
        int i = 0;
        while(loginMessageOptional.isEmpty() && i < MAX_LOGIN_ATTEMPT) {
            LOGGER.warning("Bad request. Waiting for a correct login request");
            loginMessageOptional = getLogin();
            i++;
        }
        return loginMessageOptional;
    }

    public boolean handleLogin(LoginMessage loginMessage) {
        LOGGER.info(String.format("Login request from '%s' in game with id '%s'", loginMessage.getUsername(), loginMessage.getGameId()));
        return GamesRegistry.getInstance().subscribe(loginMessage.getUsername(), loginMessage.getGameId(), userSocket);
    }

    public void run() {
        Optional<LoginMessage> loginMessageOptional = getValidLogin();
        String response = "KO";
        if (!loginMessageOptional.isEmpty() && handleLogin(loginMessageOptional.get())) {
            LOGGER.warning(String.format("Successful login[username: %s, gameId: %s]", loginMessageOptional.get().getUsername(), loginMessageOptional.get().getGameId()));
            response = "OK";
        }
        outputWriter.println(response);
    }
}
