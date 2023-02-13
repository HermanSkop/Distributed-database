package Network;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Objects;

public class ClientThread extends NetworkThread{
    String host;
    int port;
    List<String> neighbours;
    String operation;
    String parameter;
    List<String> visitedNodes;

    /**
     * After running object created by this constructor, single message will be sent to a server.
     * @param host of server to send message
     * @param port of server to send message
     * @param operation to be sent to the server
     * @param parameter to be sent to the server
     * @see ClientThread#ClientThread(List, String, String, List)
     */
    public ClientThread(String host, int port, String operation, String parameter) {
        this.host = host;
        this.port = port;
        this.operation = operation;
        this.parameter = parameter;
    }
    /**
     * After running object created by this constructor operation will be sent to all neighbours avoiding visited nodes.
     * @param neighbours of the node, will receive message
     * @param operation operation to be sent to the server
     * @param parameter parameter to be sent to the server
     * @param visitedNodes nodes that are already checked
     * @see ClientThread#ClientThread(String, int, String, String)
     */
    public ClientThread(List<String> neighbours, String operation, String parameter, List<String> visitedNodes) {
        this.visitedNodes = visitedNodes;
        this.operation = operation;
        this.parameter = parameter;
        this.neighbours = neighbours;
    }

    /**
     * Sends one or many messages, depending on constructed fields.
     * @see ClientThread#ClientThread
     */
    @Override
    public void run(){
        if(neighbours == null) System.out.println(sendOperation(host, port, operation, parameter));
        else status = sendOperationToAll(operation, parameter, neighbours, visitedNodes);
    }

    /**
     * Calls {@link ClientThread#sendOperation} for all neighbours that are not in visited nodes.
     * @param operation operation to be sent to the server
     * @param parameter parameter to be sent to the server
     * @param neighbours of the node, will receive message
     * @param visitedNodes nodes that are already checked
     * @return response of the server or ERROR otherwise
     */
    private static String sendOperationToAll(String operation, String parameter, List<String> neighbours, List<String> visitedNodes) {
        String returnStatement = "ERROR";
        String response = "ERROR";
        for (String host_port:neighbours) {
            if(!visitedNodes.contains(host_port)) {
                String[] h_p = host_port.split(":");
                visitedNodes.add(host_port);
                response =
                        sendOperation(h_p[0], Integer.parseInt(h_p[1]), operation,
                                visitedNodes.isEmpty()?parameter:parameter + " " + getAllAsParameter(visitedNodes));

            }
            if(!Objects.equals(response, "ERROR")){
                returnStatement = response;
                break;
            }
        }
        return returnStatement;
    }

    /**
     * Sends operation and value to the server.
     * @param host of server to send message
     * @param port of server to send message
     * @param operation to be sent to the server
     * @param parameter to be sent to the server
     * @return response from the server
     */
    private static String sendOperation(String host, int port, String operation, String parameter) {
        try (Socket socket = new Socket(host, port); InputStream input = socket.getInputStream();
             OutputStream output = socket.getOutputStream()) {
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(input));
            PrintWriter serverWriter = new PrintWriter(output, true);
            serverWriter.println(operation + " " + parameter);
            String response = serverReader.readLine();
            return response;
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
        return "Client Thread smth wrong!";
    }
}
