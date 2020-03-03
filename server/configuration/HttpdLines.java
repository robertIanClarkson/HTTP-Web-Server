package server.configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class HttpdLines {

    private HashMap<String, String> directives = new HashMap<>();
    private HashMap<String, String> alias = new HashMap<>();
    private HashMap<String, String> scriptAlias = new HashMap<>();

    public HttpdLines(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line, key, value;
        while ((line = reader.readLine()) != null) {
            key = line.substring(0, line.indexOf(" ")).trim();
            if (key.equals("Alias")) {
                addAlias(line);
            } else if (key.equals("ScriptAlias")) {
                addScriptAlias(line);
            } else {
                value = stripQuotes(line.substring(line.indexOf(" "))).trim();
                directives.put(key, value);
            }
        }
        addDefaults();
    }

    private void addDefaults() {
        if (!directives.containsKey("DirectoryIndex")) {
            directives.put("DirectoryIndex", "index.html");
        }
        if (!directives.containsKey("Listen")) {
            directives.put("Listen", "8080");
        }
        if (!directives.containsKey("AccessFile")) {
            directives.put("AccessFile", ".htaccess");
        }
    }

    public String getDirective(String key) throws ConfigError {
        if (directives.containsKey(key)) {
            return directives.get(key);
        } else {
            throw new ConfigError("Directive \"" + key + "\" is not found");
        }
    }

    public boolean isScriptAlias(String id) {
        for (String currentkey : scriptAlias.keySet()) {
            if (id.contains(currentkey)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAlias(String id) {
        for (String currentkey : alias.keySet()) {
            if (id.contains(currentkey)) {
                return true;
            }
        }
        return false;
    }

    public String getScriptAlias(String id) {
        String key, value, resolvedID = "";
        for (String currentKey : scriptAlias.keySet()) {
            if (id.contains(currentKey)) {
                key = currentKey;
                value = scriptAlias.get(key);
                resolvedID = value + id.substring(id.lastIndexOf(key) + key.length());
                break;
            }
        }
        return resolvedID;
    }

    public String getAlias(String id) {
        String key, value, resolvedID = "";
        for (String currentKey : alias.keySet()) {
            if (id.contains(currentKey)) {
                key = currentKey;
                value = alias.get(key);
                resolvedID = value + id.substring(id.lastIndexOf(key) + key.length());
                break;
            }
        }
        return resolvedID;
    }

    private String stripQuotes(String line) {
        if (line.contains("\"")) {
            int x = line.indexOf("\"");
            int y = line.lastIndexOf("\"");
            return line.substring(x + 1, y);
        }
        return line;
    }

    private void addAlias(String line) {
        String key = line.substring(line.indexOf(" "), line.lastIndexOf(" ")).trim();
        String value = stripQuotes(line.substring(line.lastIndexOf(" ")));
        alias.put(key, value);
    }

    private void addScriptAlias(String line) {
        String key = line.substring(line.indexOf(" "), line.lastIndexOf(" ")).trim();
        String value = stripQuotes(line.substring(line.lastIndexOf(" ")));
        scriptAlias.put(key, value);
    }
}
