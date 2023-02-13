package Database;
import Network.ClientThread;

public class DatabaseClient {
    public static void main(String[] args) {
	    String gateway = null;
        int port = 0;
        String operation = null;
        String parameter = null;

        // Parameter scan loop
        for(int i=0; i<args.length; i++) {
            switch (args[i]) {
                case "-gateway" -> {
                    String[] gatewayArray = args[++i].split(":");
                    gateway = gatewayArray[0];
                    port = Integer.parseInt(gatewayArray[1]);
                }
                case "-operation" -> {
                    operation = args[++i];
                    if(!"get-min get-max, terminate".contains(operation)) parameter = args[++i];
                }
            }
        }
        new ClientThread(gateway, port, operation, parameter).start();
    }
}
