package it.polimi.ingsw.controller.adapter;

import com.google.gson.*;
import it.polimi.ingsw.model.turn_taker.TurnTaker;

import java.lang.reflect.Type;


public class TurnTakerAdapter implements JsonSerializer<TurnTaker>, JsonDeserializer<TurnTaker> {
    @Override
    public JsonElement serialize(TurnTaker src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
        result.add("properties", context.serialize(src, src.getClass()));
        return result;
    }

    @Override
    public TurnTaker deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");
        try {
            return context.deserialize(element, Class.forName("it.polimi.ingsw.model.turn_taker." + type));
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown element type: " + type, e);
        }
    }

}
