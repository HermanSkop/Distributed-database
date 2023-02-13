package Database;

import Network.Node;

import java.util.LinkedList;
import java.util.List;

public class DatabaseNode extends Database {
    public static void main(String[] args) {
        int port = 0;
        String key = null;
        String value = null;
        List<String> connections = new LinkedList<>();
        // Parameter scan loop
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-tcpport" -> {
                    port = Integer.parseInt(args[++i]);
                }
                case "-record" -> {
                    String[] key_value = args[++i].split(":");
                    key = key_value[0];
                    value = key_value[1];
                }
                case "-connect" -> connections.add(args[++i]);
                }
            }
        (connections.isEmpty()?new Node(port, key, value):new Node(port, key, value, connections)).start();
    }
}