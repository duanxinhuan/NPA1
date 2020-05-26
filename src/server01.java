import Server.Server;

import java.io.IOException;

public class server01 {
    public static void main(String[] args) {
        Server s = new Server();
        try {
            s.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
