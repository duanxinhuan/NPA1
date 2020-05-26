package Server;
import Utilizer.StreamManager;
import client.User;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;


public class ClientHandler implements Runnable{
    private int attempt =0;
    private boolean correct = false;
    private int number;
    private OutputStream out;
    private InputStream in;
    private boolean gameOver = false;
    private boolean terminate = false;
    private Socket socket;
    private StreamManager sm = new StreamManager();
    private String continueStatus = "q";
    private User user;
    private AliveKeeper keeper;
    private long startTime;
    private int timeUsed;

    public String getContinueStatus() {
        return continueStatus;
    }


    public ClientHandler(Socket socket) throws IOException {

        this.socket = socket;
        out = socket.getOutputStream();
        in = socket.getInputStream();
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void run() {
        // establish connection from the client and
        String customerGuess;
        sm.send("Game start", out);
        startTime =System.currentTimeMillis();
        try {
            String userString= sm.read(in);
            String[] userArray = userString.split("/");
            user = new User(userArray[0],userArray[1],Integer.parseInt(userArray[2]));
            keeper = new AliveKeeper(user.getPort(),user.getInet_Addr(),System.currentTimeMillis(),false);
            Thread t1 = new Thread(keeper);
            t1.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!gameOver) {
                attempt++;
                customerGuess = sm.read(in);
                String response = handleGuess(customerGuess);
                if(!gameOver){
                    sm.send(response, out);
                }
                keeper.setLastInputTime(System.currentTimeMillis());
                if(checkEnd()){
                    gameOver = true;
                    keeper.setGameOver(true);
                }
        }

        while(!terminate){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
            customerGuess = sm.read(in);
            System.out.println(customerGuess);
            this.continueStatus =customerGuess;
            System.out.println(this.continueStatus);
    }


    public void send(String s){
        // send string to the client
        this.sm.send(s,this.out);
        terminate = true;
    }

    public String handleGuess(String s){
        //create abd send response according to client's guess
        String response;
        //if(correct) response = "you have already get the correct answer, please wait for other people to join in.";
        if(attempt>4)
            response = "you have already guessed 4 times.....";
        else if(s.equals("q")){
            gameOver = true;
            response = null;
        }
        else {

            int guessNumber = Integer.parseInt(s);
            if (guessNumber == this.number) {
                response = "Correct! plz wait for other people ";
                correct = true;
                timeUsed =  (int)TimeUnit.MILLISECONDS.toSeconds((System.currentTimeMillis() - startTime));
            }
            else if (guessNumber > number) response = "it is a bigger number";
            else response = "it is a smaller number";
        }
        return response;
    }

    public boolean checkEnd(){
        // check if this game is end or not
        if(correct) return true;
        if (attempt>4) return true;
        if(keeper != null){
            if(keeper.isGameOver()) return true;
        }
        if(gameOver) return true;
        return false;
    }

    public String getUserName(){
        return this.user.getName();
    }
    public String getResult(){
        //return the result of this game
        String result = "user : " + getUserName() + "\n"
                + "success: " + correct + "\n"
                + "time used: " + timeUsed;
        if(!correct) result += "(not finished)";
        return result;
    }

    public int getTimeUsed() {
        return timeUsed;
    }

    public boolean isCorrect() {
        return correct;
    }
}
