package Server;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class LogWriter {
    FileWriter writer = null;
    public String getTime(){

        /// write date time to log
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        return strDate;
    }
    public void writeConnection(String address){
        // write to connection log
        try {
            writer = new FileWriter("connection.txt",true);
            writer.write("address: " + address +"\n"
            + "date:"+ getTime() +"\n"
            +"activity: connect to play the game\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeGame(String result){
        // write to game log
        try {
            writer = new FileWriter("gamelog.txt",true);
            writer.write("------game results-------\n"
                    +result+'\n'
                    + "date:"+ getTime() +"\n");

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
