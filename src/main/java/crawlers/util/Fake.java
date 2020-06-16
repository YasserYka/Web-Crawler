package crawlers.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Fake {
    
    public static String loadHtml(){
        StringBuilder content = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("fake.html"));
            String holder;
            while ((holder = bufferedReader.readLine()) != null)
                content.append(holder);
            
            bufferedReader.close();
        } catch (IOException e) {
             e.printStackTrace(); 
        }
        return content.toString();
    }
}