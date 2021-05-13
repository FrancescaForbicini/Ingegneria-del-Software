package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.message.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Optional;
import java.util.logging.Logger;

//TODO close socket, streams.. in some ways
public class SocketConnector implements Connector {
    private final static Logger LOGGER = Logger.getLogger(SocketConnector.class.getName());
    private final Socket socket;
    private final PrintWriter outputWriter;
    private final BufferedReader inputReader;
    private final Gson gson;

    public SocketConnector(Socket socket) throws IOException {
        this.socket = socket;
        outputWriter = new PrintWriter(socket.getOutputStream(), true);
        inputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        gson = new Gson();
    }

    @Override
    public boolean sendMessage(Message message) {
        LOGGER.info("SocketConnector sends a message");
        String jsonMessage = gson.toJson(message);
        outputWriter.println(jsonMessage);
        try {
            return inputReader.readLine().equals("OK");
        } catch (IOException e) {
            return false;
        }
    }
    private <T extends Message> T deserialize(String jsonMessage, Type typeOfMessage) throws JsonSyntaxException, JsonIOException {
        return gson.fromJson(jsonMessage, typeOfMessage);
    }

    @Override
    public Optional<Message> receiveMessage(Type typeOfMessage) {
        LOGGER.info("SocketConnector receives a message");
        String jsonMessage;
        Optional<Message> optionalMessage;
        String ack;
        try {
            jsonMessage = inputReader.readLine();
            Message message = deserialize(jsonMessage, typeOfMessage);
            ack = "OK";
            optionalMessage = Optional.ofNullable(message);
        } catch (IOException | JsonSyntaxException | JsonIOException e) {
            ack = "KO";
            optionalMessage = Optional.empty();
        }
        outputWriter.println(ack);
        return optionalMessage;
    }
}
