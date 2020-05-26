package client;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    // the client class, it is used to connect to the server.
    private Scanner sc = new Scanner(System.in);
    private int localPort;
    private String localIP;
    private ClientIO io;



    public Client(String serverIp, int serverPort,String localIP,int localPort) {

        this.localPort = localPort;
        this.localIP = localIP;

        try {
            Socket s = new Socket(serverIp,serverPort);


            getName( s);
            io.gameStart();

        } catch (IOException e) {
            System.out.println("there is already six people in the server");
        }
    }



    public void getName(Socket s) throws IOException {
        //get user's name from the console
        System.out.println("input your name:");
        String name = sc.nextLine();
        io = new ClientIO(s);
        this.io.setUser(new User(name, localIP, localPort));
    }




}
