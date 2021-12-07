package algos;

import api.NodeData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class PosSerializer implements JsonSerializer<NodeData> {

    @Override
    public JsonElement serialize(NodeData nodeData, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObj = new JsonObject();
        String formatted= String.format("%f,%f,%f",nodeData.getLocation().x(),nodeData.getLocation().y(),nodeData.getLocation().z());
        jsonObj.addProperty("pos",formatted);
        jsonObj.addProperty("id",nodeData.getKey());
        return jsonObj;
    }
}
