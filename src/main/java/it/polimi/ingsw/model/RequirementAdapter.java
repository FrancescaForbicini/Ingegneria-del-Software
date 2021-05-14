package it.polimi.ingsw.model;

import com.google.gson.*;

import it.polimi.ingsw.model.requirement.Requirement;

import java.lang.reflect.Type;

public class RequirementAdapter implements JsonSerializer<Requirement>, JsonDeserializer<Requirement> {
    @Override
    public JsonElement serialize(Requirement src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
        result.add("properties", context.serialize(src, src.getClass()));
        return result;
    }

    @Override
    public Requirement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
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
