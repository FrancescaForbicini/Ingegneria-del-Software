package it.polimi.ingsw.client;

import com.google.gson.Gson;
import it.polimi.ingsw.message.Message;
import it.polimi.ingsw.server.GameServer;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class SocketClientConnector implements ClientConnector{
    private final static Logger LOGGER = Logger.getLogger(SocketClientConnector.class.getName());
    private static String HOST = "127.0.0.1"; // TODO hardcoded?

    public Socket connectedServer;
    public PrintWriter outputWriter;
    public BufferedReader inputReader;

    @Override
    public boolean send(Message message) {
        LOGGER.info("The client sends a message");
        sendOnly(message);
        try {
            String res = inputReader.readLine();
            return res.equals("OK") ? true : false; // TODO stronger semantic
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public Message receive() {
        LOGGER.info("The client receives a message");
        return null;
    }

    private void sendOnly(Message message) {
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(message) + '\n';
        System.out.println(jsonMessage);
        outputWriter.println(jsonMessage);
    }

    public SocketClientConnector() throws IOException {
        connectedServer = new Socket(HOST, GameServer.PORT);
        outputWriter = new PrintWriter(connectedServer.getOutputStream(), true);
        inputReader = new BufferedReader(new InputStreamReader(connectedServer.getInputStream()));
    }
}
