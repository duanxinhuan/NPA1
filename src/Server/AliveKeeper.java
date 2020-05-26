package Server;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.TimeUnit;

public class AliveKeeper implements Runnable{


    private int port;
    private String IP;
    private long lastInputTime;
    private boolean gameOver;
    private DatagramSocket ds = new DatagramSocket();

    public AliveKeeper(int port, String IP, long lastInputTime, boolean gameOver) throws SocketException {
        this.port = port;
        this.IP = IP;
        this.lastInputTime = lastInputTime;
        this.gameOver = gameOver;
    }

    @Override
    public void run() {
        //keep sending message to the client,give them notification if they haven't input any value for 20 seconds
        try {
            for(int i=0;i<6;i++){
                if(!gameOver){
                    TimeUnit.SECONDS.sleep(20);
                    long time = System.currentTimeMillis() - lastInputTime;
                    if(TimeUnit.MILLISECONDS.toSeconds(time)>19&&!gameOver){
                        send("Notice: you haven't input any number, the time limit of this game is only 2 minutes!");

                    }
                }

        }
            if(!gameOver){
                this.gameOver = true;
                send("you have exceed the time limitation, please quit the game and reconnect to the server");
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setLastInputTime(long lastInputTime) {
        this.lastInputTime = lastInputTime;
    }

    public void send(String s) throws IOException {
        InetAddress ip = InetAddress.getByName(this.IP);
        DatagramPacket dp = new DatagramPacket(s.getBytes(), s.length(), ip, port);
        ds.send(dp);
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
