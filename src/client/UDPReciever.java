package client;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPReciever implements Runnable{
    private DatagramSocket ds;

    public UDPReciever(int port) {
        try {
            this.ds = new DatagramSocket(port);

        } catch (SocketException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void run() {
        // a thread that runs on the client side to receive udp packet from the server
        while(true&&!ds.isClosed()){

            byte[] buff = new byte[1024];

            // Receive the information and print it.
            DatagramPacket dp = new DatagramPacket(buff, buff.length);
            try {

                ds.receive(dp);
                String message = new String(dp.getData(), 0, dp.getLength());
                System.out.println(message);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
