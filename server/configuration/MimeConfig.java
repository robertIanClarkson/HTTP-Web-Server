package server.configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

public class MimeConfig {

    private final String DELIMITERS = " \t";
    private HashMap<String, String> mimeTypes;

    MimeConfig(String fileName) throws IOException {
        BufferedReader reader;
        String line;
        mimeTypes = new HashMap<>();
        reader = new BufferedReader(new FileReader(fileName));
        while ((line = reader.readLine()) != null) {
            if ((!line.contains("#")) && (line.length() != 0)) {
                tokenize(line);
            }
        }
    }

    private void tokenize(String line) {
        String type, extension;
        StringTokenizer tokens = new StringTokenizer(line, DELIMITERS);
        if (tokens.countTokens() > 1) {
            type = tokens.nextToken();
            while (tokens.hasMoreTokens()) {
                extension = tokens.nextToken();
                mimeTypes.put(extension, type);
            }
        }
    }

    public String getMimeType(String extension) {
        return mimeTypes.get(extension);
    }
}
