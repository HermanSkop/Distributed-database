package Database;

import java.util.List;
import java.util.stream.Stream;

public abstract class Database {
    /**
     * Returns list containing key under index 0 and value under index 1.
     * Key and value are extracted from keyValue parameter separated by ':'.
     * @param keyValue key and value separated by ':'
     * @return list of String containing key under index 0 and value under index 1
     */
    public static List<String> extractKeyValue(String keyValue){
        return Stream.of(keyValue.split(":")).toList();
    }
}
