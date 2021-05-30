package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.controller.Settings;
import it.polimi.ingsw.message.MessageDTO;

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
        gson = Settings.getGson();
    }

    @Override
    public boolean sendMessage(MessageDTO messageDTO) {
        LOGGER.info("SocketConnector sends a message");
        String messageType = messageDTO.getClass().getName();
        String jsonMessage = gson.toJson(messageDTO);
        outputWriter.println(messageType);
        outputWriter.println(jsonMessage);
        try {
            return inputReader.readLine().equals("OK");
        } catch (IOException e) {
            return false;
        }
    }
    private <T extends MessageDTO> T deserialize(String jsonMessage, Type typeOfMessage) throws JsonSyntaxException, JsonIOException {
        return gson.fromJson(jsonMessage, typeOfMessage);
    }

    private Optional<MessageDTO> readMessage(Type typeOfMessage) {
        String jsonMessage;
        Optional<MessageDTO> optionalMessage;
        String ack;
        try {
            jsonMessage = inputReader.readLine();
            MessageDTO messageDTO = deserialize(jsonMessage, typeOfMessage);
            ack = "OK";
            optionalMessage = Optional.ofNullable(messageDTO);
        } catch (IOException | JsonSyntaxException | JsonIOException e) {
            ack = "KO";
            optionalMessage = Optional.empty();
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
            ack = "tmp";
            optionalMessage = Optional.empty();
        }

        outputWriter.println(ack);
        return optionalMessage;
    }


    @Override
    public Optional<MessageDTO> receiveMessage(Type typeOfMessage) {
        LOGGER.info("SocketConnector receives a message of type " + typeOfMessage);
        String messageType;
        try {
            messageType = inputReader.readLine();
            assert typeOfMessage.getTypeName().equals(messageType);
            return readMessage(typeOfMessage);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<MessageDTO> receiveAnyMessage() {
        LOGGER.info("SocketConnector receives a message");
        try {
            Type type = Class.forName(inputReader.readLine());
            return readMessage(type);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
