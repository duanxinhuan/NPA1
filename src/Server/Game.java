package Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Game implements Runnable{
    private Queue<ClientHandler> queue;
    private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
    private int number;
    private boolean gameOver =false;
    private Thread[] threads = new Thread[3];
    LogWriter writer = new LogWriter();

    public Game(Queue<ClientHandler> queue) {
        this.queue = queue;

    }

    public int generateNumber()
    {
        // create a number betwwen 0-12
        Random rand = new Random();
        int rand_int1 = rand.nextInt(13);
        System.out.println("number for this turn: " + rand_int1);
        return rand_int1;
    }

    public void gameStart() throws InterruptedException, IOException {
        // start the game
        this.number = generateNumber();
        gameOver =false;
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i =0;i<clients.size();i++){
            clients.get(i).setGameOver(gameOver);
            clients.get(i).setNumber(number);
            threads[i] = new Thread(clients.get(i));

        }
        for(int i=0; i<3;i++){
            threads[i].start();
        }

        while (!gameOver){
            TimeUnit.SECONDS.sleep(3);
            boolean[] results = new boolean[3];
            for(int i =0;i<clients.size();i++){
                results[i] =clients.get(i).checkEnd();
            }
            if(results[0]&&results[1]&&results[2]){
                gameOver = true;
                String resultString = "";
                String winner = "nobody";
                int time = 5000;
                for(int i =0;i<clients.size();i++){
                    if(clients.get(i).isCorrect() && clients.get(i).getTimeUsed()<time){
                        winner = clients.get(i).getUserName();
                        time = clients.get(i).getTimeUsed();
                    }
                    resultString+=clients.get(i).getResult()+"\n";
                }
                resultString += "\n" +"winner: " +winner;
                writer.writeGame(resultString);

                for(int i =0;i<clients.size();i++){
                    clients.get(i).send(resultString);
                    System.out.println("sending....");
                }

            }
        }
    }

    @Override
    public void run() {
        System.out.println("waiting for connections.....");
        // start the game, game over
        while(true){

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while(queue.size()>=1 && clients.size()<3){

                    clients.add(queue.remove()) ;
            }
            if(clients.size()>=3){
                try {
                    gameStart();
                    TimeUnit.SECONDS.sleep(15);
                    System.out.println("gameOver");
                    System.out.println(clients.size());
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
                ArrayList<ClientHandler> newClients = new ArrayList<ClientHandler>();
                for(int i=0;i<clients.size();i++){
                    threads[i] = null;
                    if(clients.get(i).getContinueStatus().equals("c")){
                        try {
                            System.out.println(clients.get(i).getUserName() + " rejoin the game");
                            newClients.add( new ClientHandler(clients.get(i).getSocket()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            this.clients =newClients;
            }
        }
    }
}

