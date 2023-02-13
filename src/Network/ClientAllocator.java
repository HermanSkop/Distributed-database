package Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientAllocator extends Thread{
    Node parentNode;

    public ClientAllocator(Node parentNode) {
        this.parentNode = parentNode;
    }

    /**
     * Creates socket that waits for connections.
     * Once connection established, creates new thread that handles request.
     */
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(parentNode.port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                RequestHandlerThread handlerThread = new RequestHandlerThread(socket, parentNode);
                handlerThread.start();
            }
        }
        catch (IOException | RuntimeException e) {
                e.printStackTrace();
            }
    }
}
