package client;
import java.io.Serializable;

public class User implements Serializable{
    //user class
    private String name;
    private String Inet_Addr ;
    private int port;

    public User(String name, String inet_Addr, int port) {
        this.name = name;
        Inet_Addr = inet_Addr;
        this.port = port;
    }



    public String getName() {
        return name;
    }

    public String getInet_Addr() {
        return Inet_Addr;
    }

    public int getPort() {
        return port;
    }



}
