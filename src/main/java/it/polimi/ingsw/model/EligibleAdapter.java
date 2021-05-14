package it.polimi.ingsw.model;

import com.google.gson.*;
import it.polimi.ingsw.model.requirement.Eligible;

import java.lang.reflect.Type;

public class EligibleAdapter implements JsonSerializer<Eligible>, JsonDeserializer<Eligible> {
    @Override
    public JsonElement serialize(Eligible src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
        result.add("properties", context.serialize(src, src.getClass()));
        return result;
    }

    @Override
    public Eligible deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");
        try {
            return context.deserialize(element, Class.forName("it.polimi.ingsw.model" + type));
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown element type: " + type, e);
        }
    }
}
