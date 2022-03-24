package MessagesHandler;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MessageHandler{
    int count = 0;
    public void saveMessage(String message)
    {
        count++;
        String filename = "messages.json";
        try (FileWriter file = new FileWriter(filename, true)) {
            //We can write any JSONArray or JSONObject instance to the file
            file.write(message);
            file.flush();
            if(count == 1024)
            {
                count = 0;
                System.out.println("Threshold");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
