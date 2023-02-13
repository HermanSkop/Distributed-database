package Network;

import Database.Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class RequestHandlerThread extends NetworkThread{
    Socket socket;
    Node server;
    List<String> visitedNodes = new LinkedList<>();
    /**
     * Constructor for RequestHandlerThread
     * @param socket to handle
     * @param server is a node that started this thread
     */
    RequestHandlerThread(Socket socket, Node server) {
        this.socket = socket;
        this.server = server;
    }
    /**
     * Asks neighbours for key of given value, if this node doesn't contain.
     * @param value of searched key
     * @return key for given value
     */
    private String findKey(String value) {
        if(Objects.equals(value, server.value)) return server.key + ":" + server.value;
        else return waitStatus(new ClientThread(server.neighbours, "find-key", value, visitedNodes));
    }
    /**
     * Asks neighbours for value, compares to current node's and returns maximum of it.
     * @return max value contained in connected nodes
     */
    private String getMax() {
        try {
            String max = waitStatus(new ClientThread(server.neighbours, "get-max", null, visitedNodes));
            if (Integer.parseInt(max) >= Integer.parseInt(server.value)) return max;
            return server.value;
        }catch (NumberFormatException e){
            return server.value;
        }
    }
    /**
     * Asks neighbours for value, compares to current node's and returns minimum of it.
     * @return min value contained in connected nodes
     */
    private String getMin() {
        try {
            String min = waitStatus(new ClientThread(server.neighbours, "get-min", null, visitedNodes));
            if (Integer.parseInt(min) <= Integer.parseInt(server.value)) return min;
            return server.value;
        }catch (NumberFormatException e){
            return server.value;
        }
    }
    /**
     * Rewrites current node's key and value.
     * @param key to be assigned to this node
     * @param value to be assigned to this node
     * @return OK message
     */
    private String newRecord(String key, String value) {
        server.key = key;
        server.value = value;
        return "OK";
    }
    /**
     * Searches for the node with given key and rewrites its value.
     * @param key of the node
     * @param value to be assigned to the node
     * @return OK message
     */
    private String setValue(String key, String value) {
        if(Objects.equals(key, server.key)){
            server.value = value;
            return "OK";
        }
        else return waitStatus(new ClientThread(server.neighbours, "set-value", key + ":" + value, visitedNodes));
    }
    /**
     * Adds given host and port to node's {@link Node#neighbours}.
     * @param host_port to be added
     * @return bool if parameter was successfully added
     */
    private boolean connect(String host_port){
        return server.neighbours.add(host_port);
    }
    /**
     * Searches for the node with given key and returns its value.
     * @param key to search
     * @return found value or ERROR otherwise
     */
    private String getValue(String key) {
        if(Objects.equals(key, server.key)) return server.value;
        else return waitStatus(new ClientThread(server.neighbours, "get-value", key, visitedNodes));
    }
    /**
     * Runs {@link ClientThread} and waits for changing its status.
     * @param clientThread to be started
     * @return returned value from {@link ClientThread} as field {@link ClientThread#status}
     */
    private String waitStatus(ClientThread clientThread) {
        clientThread.start();
        while (clientThread.status==null) System.out.print("");
        return clientThread.status;
    }
    /**
     * Handles request from the client thread, depending on operation.
     */
    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
            String line;
            while (!server.isInterrupted() && (line = reader.readLine()) != null) {
                System.out.println("Server received: [" + line + ']');
                List<String> input = Arrays.stream(line.split(" ")).toList();
                if(input.size()>=3)visitedNodes = new LinkedList<>(Arrays.asList(input.get(2).split("\\|")));
                visitedNodes.add(getHostPortAsParameter("localhost", server.port));
                if(input.size()<=3) {
                    String operation = input.get(0);
                    String parameter = input.get(1);
                    switch (operation) {
                        case "CONNECT" -> writer.println(connect(parameter));
                        case "get-value" -> writer.println(getValue(parameter));
                        case "get-max" -> writer.println(getMax());
                        case "get-min" -> writer.println(getMin());
                        case "find-key" -> writer.println(findKey(parameter));
                        case "set-value" -> writer.println(setValue(Database.extractKeyValue(parameter).get(0),Database.extractKeyValue(parameter).get(1)));
                        case "new-record" -> writer.println(newRecord(Database.extractKeyValue(parameter).get(0),Database.extractKeyValue(parameter).get(1)));
                        case "terminate" -> {
                            writer.println("OK");
                            server.interrupt();
                        }
                    }
                }
                else writer.println("ERROR");
            }
        } catch (IOException e) {
            System.err.println("Error while serving client");
        }
    }
}
