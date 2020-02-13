package configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class HttpdConfig {

    private HashMap<String, String> alias = new HashMap<>();
    private String serverRoot, documentRoot, directoryIndex, logFile, scriptAlias, aliasA, aliasB;
    private int listen;

    HttpdConfig( String fileName ) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader( fileName ));
            serverRoot = stripQuotes(stripData(reader.readLine()));
            documentRoot = stripQuotes(stripData(reader.readLine()));
            directoryIndex = stripQuotes(stripData(reader.readLine()));
            listen = Integer.parseInt(stripData(reader.readLine()));
            logFile = stripQuotes(stripData(reader.readLine()));
            scriptAlias = stripQuotes(stripData(reader.readLine()));
            aliasA = stripQuotes(stripData(reader.readLine()));
            aliasB = stripQuotes(stripData(reader.readLine()));
        } catch (IOException e) {
            System.out.println("configuration.HttpdConfig --> IOException: " + e);
        }
    }

    public String getServerRoot() {
        return serverRoot;
    }

    public String getDocumentRoot() {
        return documentRoot;
    }

    public String getDirectoryIndex() {
        return directoryIndex;
    }

    public int getListen() {
        return listen;
    }

    public String getLogFile() {
        return logFile;
    }

    public String getScriptAlias() {
        return scriptAlias;
    }

    public boolean isAlias(String key) {
        return alias.containsKey(key);
    }

    public String getAlias(String key) {
        return alias.get(key);
    }

    private String stripQuotes(String line ) {
        int x = line.indexOf("\"");
        int y = line.lastIndexOf("\"");
        return line.substring( x+1, y);
    }

    private String stripData(String line) {
        int x = line.indexOf(" ");
        return line.substring(x+1);
    }
}
