package json;

import com.fasterxml.jackson.databind.JsonNode;

public interface JSONConvertable{
    public void writeToJSON();
    public static<T> T readFromJSON(JsonNode node, int index, Class<T> clazz){
        return CustomJson.objectFromJsonArray(node, index, clazz);
    }
}