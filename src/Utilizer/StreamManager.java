package Utilizer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

public class StreamManager {
    // read from the inputstream
    public String read(InputStream inputStream)
    {

        // this class is in charge of input and out put
        StringBuffer stringBuffer = new StringBuffer();
        byte [] buffer = new byte[1024];

        try
        {
            inputStream.read(buffer);

            for(int i=0; i < 1024; i++) {
                if(buffer[i] == 0)
                    break;

                stringBuffer.append((char)buffer[i]);
            }
        }
        catch (IOException e )
        {
            e.printStackTrace();
        }

        return stringBuffer.toString();
    }

    // send message via output stream
    public void send(String number, OutputStream outputStream)
    {
        try
        {
            outputStream.write((number).getBytes());
            outputStream.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
