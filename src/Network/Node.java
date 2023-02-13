package Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import static Network.NetworkThread.getHostPortAsParameter;

public class Node extends Thread{
    String key;
    String value;
    int port;
    List<String> neighbours = new LinkedList<>();
    public Node(int port, String key, String value) {
        this.key = key;
        this.value = value;
        this.port = port;
    }
    public Node(int port, String key, String value, List<String> neighbours) {
        this(port, key, value);
        this.neighbours = neighbours;
        connectNeighbours(neighbours, getHostPortAsParameter("localhost", port));
    }

    /**
     * Sends 'CONNECT' command to all provided nodes.
     * @param neighbours who will receive operation
     * @param originalNode node to connect to
     */
    private static void connectNeighbours(List<String> neighbours, String originalNode) {
        List<String> visited = new LinkedList<>();
        visited.add(originalNode);
        new ClientThread(neighbours, "CONNECT", originalNode, visited).start();
    }

    @Override
    public String toString() {
        return "port: " + port + " | " + key + ":" + value + " | neighbours: " + neighbours;
    }

    /**
     * Creates {@link ClientAllocator} and starts it. Then wait until {@link Node#isInterrupted()} becomes true.
     */
    @Override
    public void run() {
        ClientAllocator clientAllocator = new ClientAllocator(this);
        clientAllocator.start();
        while (!this.isInterrupted()) {
            System.out.print("");
        }
        System.exit(0);
    }
}
