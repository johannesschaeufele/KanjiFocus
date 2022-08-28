package me.johannesschaeufele.kanjifocus.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/**
 * Utility class for mapping between files containing JSON and object representations
 *
 * Serialization and deserialization can be performed via static methods,
 * no instance of this class is required or intended
 */
public final class JsonIO {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Loads a single object representation of the given type from a file containing corresponding JSON
     *
     * @param file The file to read from
     * @param clazz The class of the object to load
     * @param <T> The type of the loaded object
     * @return Object representation corresponding to the JSON found in the given file
     *
     * @throws IOException if an exception occurs while reading the file
     */
    public static <T> T loadJSON(File file, Class<T> clazz) throws IOException {
        return objectMapper.readValue(file, clazz);
    }

    /**
     * Writes a single object representation to file as JSON
     *
     * @param file The file to write to
     * @param o The object to serialize
     * @throws IOException             if an exception occurs while writing the file
     * @throws JsonProcessingException if an exception occurs when serializing the object to JSON
     */
    public static void saveJSON(File file, Object o) throws IOException, JsonProcessingException {
        Files.write(file.toPath(), objectMapper.writeValueAsBytes(o), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private JsonIO() {
    }

}
