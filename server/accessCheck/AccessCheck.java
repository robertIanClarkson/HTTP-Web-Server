package server.accessCheck;

import server.configuration.Configuration;
import server.request.Request;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class AccessCheck {

    private HashMap<String, String> directives;

    public AccessCheck() throws IOException {
        String line, key, value;
        directives = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(Configuration.getHttpd().getAccessFile()));
        while((line = reader.readLine()) != null) {
            key = line.substring(0, line.indexOf(" ")).trim();
            value = stripQuotes(line.substring(line.indexOf(" ")).trim());
            directives.put(key, value);
        }
    }

    public static void check(Request request) {
        if (accessExist(request.getId().getURI())) {

        }
    }

    public String getDirective(String key) {
        return directives.get(key);
    }

    private static boolean accessExist(String uri) {
        return false;
    }

    private String stripQuotes(String line) {
        if(line.contains("\"")) {
            int x = line.indexOf("\"");
            int y = line.lastIndexOf("\"");
            return line.substring( x+1, y);
        }
        return line;
    }
}
