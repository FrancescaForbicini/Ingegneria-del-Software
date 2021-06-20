package it.polimi.ingsw.controller.adapter;

import com.google.gson.*;
import it.polimi.ingsw.client.turn_taker.ClientTurnTaker;

import java.lang.reflect.Type;


public class ClientTurnTakerAdapter implements JsonSerializer<ClientTurnTaker>, JsonDeserializer<ClientTurnTaker> {
    @Override
    public JsonElement serialize(ClientTurnTaker src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
        result.add("properties", context.serialize(src, src.getClass()));
        return result;
    }

    @Override
    public ClientTurnTaker deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");
        try {
            return context.deserialize(element, Class.forName("it.polimi.ingsw.client.turn_taker." + type));
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown element type: " + type, e);
        }
    }
}
