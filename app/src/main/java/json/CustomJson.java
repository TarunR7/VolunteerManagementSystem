package json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class CustomJson {
    public static final ObjectMapper MAPPER = (new ObjectMapper())
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY).findAndRegisterModules();
    public static final ObjectWriter WRITER = MAPPER.writer(new DefaultPrettyPrinter());

    public static <T> T fromJson(String json, Class<T> type) {
        try {
            return MAPPER.readValue(json, type);
        } catch (JsonProcessingException e) {
            System.out.println(e);
            return null;
        }
    }

    public static <T> T fromJson(JsonNode json, Class<T> type) {
        if (json.isMissingNode())
            json = JsonNodeFactory.instance.arrayNode();

        try {
            return MAPPER.treeToValue(json, type);
        } catch (JsonProcessingException e) {
            System.out.println(e);
            return null;
        }
    }

    public static <T> LinkedHashSet<T> fromJsonArray(JsonNode json, Class<? extends T> type) {
        if (json.isMissingNode())
            json = JsonNodeFactory.instance.arrayNode();

        try {
            return MAPPER.treeToValue(json, MAPPER.getTypeFactory().constructCollectionType(LinkedHashSet.class, type));
        } catch (JsonProcessingException e) {
            System.out.println(e);
            return null;
        }
    }

    public static <T> LinkedHashSet<T> objectArrayFromFile(String filePath, Class<T> clazz) {
        try {
            // Check if the file is empty or contains only whitespaces
            if (Files.lines(Paths.get(filePath)).allMatch(String::isBlank)) {
                System.out.println("File is empty or contains only whitespaces. Returning an empty LinkedHashSet.");
                return new LinkedHashSet<>();
            }





            LinkedHashSet<T> objects = MAPPER.readValue(new File(filePath), MAPPER.getTypeFactory().constructCollectionLikeType(LinkedHashSet.class, clazz));
            if (objects == null)
                return (new LinkedHashSet<T>());

            return objects;
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    public static <T> T objectFromJsonArray(JsonNode json, int index, Class<T> clazz) {
        if (index > json.size() || index < 0)
            throw new IndexOutOfBoundsException();
        try {
            JsonNode objectJson = json.get(index);
            return MAPPER.treeToValue(objectJson, clazz);
        } catch (JsonProcessingException e) {
            System.out.println(e);
            return null;
        }
    }

    public static <T> LinkedHashSet<T> fromJsonArray(String json, Class<? extends T> type) {
        try {
            return MAPPER.readValue(json, MAPPER.getTypeFactory().constructCollectionType(LinkedHashSet.class, type));
        } catch (JsonProcessingException e) {
            System.out.println(e);
            return null;
        }
    }

    public static <T> String toJson(T obj) {
        try {
            return WRITER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            System.out.println(e);
            return "";
        }
    }

    public static String toJsonArray(LinkedHashSet<?> obj) {
        try {
            return WRITER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            System.out.println(e);
            return "";
        }
    }

    public static String readJsonArrayFromFile(String filePath) {
        String emptyJsonArray = "[ ]";
        try {
            Path jsonPath = Paths.get(filePath);
            String json = new String(Files.readAllBytes(jsonPath));
            System.out.println(json);
            if (json.isEmpty())
                return emptyJsonArray;
            return json;

        } catch (IOException e) {
            System.out.println("Error reading JSON from " + filePath);
            return emptyJsonArray;
        }
    }

    public static void writeJsonToFile(String filePath, String json) {
        try {
            writeToFile(filePath, json, false);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void writeJsonToFile(String filePath, JsonNode json) {
        try {
            WRITER.writeValue(new File(filePath), json);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static <T> void addJsonToJsonArray(String filePath, T object, Class<? extends T> clazz) {
        String jsonArray = readJsonArrayFromFile(filePath);
        LinkedHashSet<T> objectArray = fromJsonArray(jsonArray, clazz);
        objectArray.add(object);
        String updatedJsonArray = toJsonArray(objectArray);
        writeJsonToFile(filePath, updatedJsonArray);
    }

    public static void writeToFile(String filePath, String content, boolean append) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, append))) {
            writer.write(content);
        }
    }
}
