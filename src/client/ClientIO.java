package client;
import Utilizer.StreamManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientIO implements Runnable {
    // this class is used to communicate with the server
    private StreamManager sm =new StreamManager();
    private Socket s;
    private InputStream in;
    private OutputStream out;
    private User user;
    private boolean gameOver = false;
    private UDPReciever receiver;
    private Scanner sc = new Scanner(System.in);
    private String consoleMessage;



    public void setUser(User user) {
        //set the user to the current user
        this.user = user;
        this.receiver =new UDPReciever(user.getPort());
    }


    public ClientIO(Socket socket) throws IOException {
        this.s = socket;
        this.in =socket.getInputStream();
        this.out = socket.getOutputStream();

    }

    public void gameStart() throws IOException {
        //start the game
        gameOver =false;
        System.out.println("waiting for game to start.....");
        String startMessage = sm.read(in);
        System.out.println(startMessage);
        sm.send(user.getName() +"/" +user.getInet_Addr()+"/" +user.getPort(),out);
        System.out.println("sent");
        Thread udp = new Thread(receiver);
        udp.start();
        while (!gameOver){
            System.out.println("input a number, or press q to exit:");
            String s = sc.nextLine();
            checkNumber(s);
            String response = sm.read(in);
            checkResponse(response);
            System.out.println(response);
        }
        String response = sm.read(in);
        System.out.println(response);
        System.out.println("press c to continue game, or q to quit");
        String s = sc.nextLine();
        checkNumber(s);
    }

    public void checkResponse(String response){
        //check the response recieved fromthe server
        if(response.equals("you have already guessed 4 times.....")||response.equals("Correct! plz wait for other people ")) gameOver =true;
    }
    public void checkNumber(String string) throws IOException {
        try{
            if(!gameOver){
                int number = Integer.parseInt(string);
                if((number>=0&&number<=12)) sm.send(string,out);
                else {
                    consoleMessage="your number should between 0-12";
                    throw new NumberFormatException();
                }
            }
            else {
                throw new NumberFormatException();
            }

        }catch (Exception e){
            if(string.equals("q")){
                sm.send("q",out);
                gameOver = true;
                quit();
            }
            else if(string.equals("c")){
                if(gameOver&&!s.isClosed()){
                    sm.send("c",out);
                    gameStart();
                }
                else{
                    System.out.println("you can't replay a game when the game is not ended");
                    reInput();
                }
            }
            else{
                System.out.println("invalid input, please reinput");
                reInput();
            }
        }
    }

    public void reInput() throws IOException {
        //a function that can let user reinput
        String s1 = sc.nextLine();
        checkNumber(s1);
    }

    public void quit() throws IOException {
        //quit from the game function
        s.close();
        System.out.println("game over thank u for attending the game");
        System.exit(1);
    }

    @Override
    public void run() {

        try {
            gameStart();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
