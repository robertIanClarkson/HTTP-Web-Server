package configuration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class MimeConfig {

    private HashMap<String, String> mimeTypes;

    MimeConfig(String fileName) throws IOException {
        BufferedReader reader;
        String line;
        String type;
        String[] extensions;
        mimeTypes = new HashMap<>();
        reader = new BufferedReader(new FileReader(fileName));
        while ((line = reader.readLine()) != null) {
            if ((!line.contains("#")) && (line.length() != 0) && (line.contains("\t"))) {
                type = getType(line);
                extensions = getExtensions(line);
                for (String extension : extensions) {
                    mimeTypes.put(extension, type);
                }
            }
        }
    }

    public String getMimeType(String extension) {
        return mimeTypes.get(extension);
    }

    private String getType(String line) {
        return line.substring(0, line.indexOf("\t"));
    }

    private String[] getExtensions(String line) {
        String right = line.substring(line.lastIndexOf("\t") + 1);
        return right.split(" ");
    }
 }
