package configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class HttpdLines {

    private HashMap<String, String> directives = new HashMap<>();
    private HashMap<String, String> alias = new HashMap<>();

    public HttpdLines(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line, key, value;
        while((line = reader.readLine()) != null) {
            key = line.substring(0, line.indexOf(" ")).trim();
            if(key.equals("Alias")) {
                addAlias(line);
            } else {
                value = stripQuotes(line.substring(line.indexOf(" "))).trim();
                directives.put(key, value);
            }
        }
    }

    public String getDirective(String key) throws ConfigError {
        if(directives.containsKey(key)) {
            return directives.get(key);
        } else {
            throw new ConfigError("Directive \"" + key + "\" is not found");
        }
    }

    public boolean isAlias(String key) {
        return alias.containsKey(key);
    }

    public String getAlias(String key) throws ConfigError{
        if(alias.containsKey(key)) {
            return alias.get(key);
        } else {
            throw new ConfigError("Alias \"" + key + "\" is not found");
        }
    }

    private String stripQuotes(String line) {
        if(line.contains("\"")) {
            int x = line.indexOf("\"");
            int y = line.lastIndexOf("\"");
            return line.substring( x+1, y);
        }
        return line;
    }

    private void addAlias(String line) {
        String key = line.substring(line.indexOf(" "), line.lastIndexOf(" ")).trim();
        String value = stripQuotes(line.substring(line.lastIndexOf(" ")));
        alias.put(key, value);
    }
}
