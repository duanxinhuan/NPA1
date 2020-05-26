package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class Server {
    // recieve and accept connection from the client
    Queue<ClientHandler> queue = new LinkedList<>();
    Game game;
    private final static int PORT = 61128;
    private Socket connection;
    private LogWriter writer = new LogWriter();

    public void start() throws IOException {
        ServerSocket server = null;
        server = new ServerSocket(PORT);
        System.out.println("Starts the server");
        game =new Game(queue);
        Thread t = new Thread(game);
        t.start();
        while(true) {
            if (queue.size() < 3) {
                connection = server.accept();
                String ip = connection.getRemoteSocketAddress().toString();
                writer.writeConnection(ip);
                ClientHandler ch = new ClientHandler(connection);
                queue.add(ch);
            }
            else {
                try {
                    TimeUnit.SECONDS.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
