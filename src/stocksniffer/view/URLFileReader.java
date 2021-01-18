package stocksniffer.view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.validator.routines.UrlValidator;

public class URLFileReader implements Reader{

    @Override
    public Set<String> read(String string) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(string)));
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR URLFileReader:: fichero no encontrado: " + ex.getMessage());
        }
        String line = "";
        Set<String> conjunto = new TreeSet<>();
        UrlValidator validator = new UrlValidator();
        while (true){
            try {
                line = reader.readLine();
            } catch (IOException ex) {
                System.out.println("ERROR URLFileReader:: IO: " + ex.getMessage());
            }
            if (line == null) break;
            if (validator.isValid(line)) {
                conjunto.add(line);
            }
        }
        return conjunto;
    }

}
