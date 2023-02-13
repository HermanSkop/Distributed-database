package Network;

import java.util.Collection;

public abstract class NetworkThread extends Thread{
    String status;

    /**
     * Creates parameter in proper form to send it to another server.
     * @param host of server
     * @param port of server
     * @return host:port
     */
    protected static String getHostPortAsParameter(String host, int port){
        return host + ":" + port;
    }

    /**
     * Creates string of connections to be sent to another server.
     * @param nodes to be transformed
     * @return node1|node2|nodeN
     */
    protected static String getAllAsParameter(Collection<String> nodes){
        StringBuilder stringBuilder = new StringBuilder();
        for (String node:nodes) {
            stringBuilder.append(node).append("|");
        }
        if (!stringBuilder.isEmpty() && stringBuilder.lastIndexOf("|")>=0)stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("|"));

        return stringBuilder.toString();
    }
}
