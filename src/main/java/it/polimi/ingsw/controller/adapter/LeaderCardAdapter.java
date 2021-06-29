package it.polimi.ingsw.controller.adapter;

import com.google.gson.*;
import it.polimi.ingsw.model.cards.leader_cards.LeaderCard;

import java.lang.reflect.Type;

public class LeaderCardAdapter implements JsonSerializer<LeaderCard>, JsonDeserializer<LeaderCard> {
    @Override
    public JsonElement serialize(LeaderCard src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
        result.add("properties", context.serialize(src, src.getClass()));
        return result;
    }

    @Override
    public LeaderCard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");
        try {
            return context.deserialize(element, Class.forName("it.polimi.ingsw.model.cards." + type));
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown element type: " + type, e);
        }
    }
}
