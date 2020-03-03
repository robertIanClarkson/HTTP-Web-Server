package server.configuration;

import java.io.IOException;

public class HttpdConfig {

    private HttpdLines lines;
    private String serverRoot, documentRoot, directoryIndex, logFile, accessFile;
    private int listen;

    HttpdConfig(String fileName) throws IOException, ConfigError {
        lines = new HttpdLines(fileName);
        serverRoot = lines.getDirective("ServerRoot");
        documentRoot = lines.getDirective("DocumentRoot");
        directoryIndex = lines.getDirective("DirectoryIndex");
        logFile = lines.getDirective("LogFile");
        listen = Integer.parseInt(lines.getDirective("Listen"));
        accessFile = lines.getDirective("AccessFile");
    }

    public String getAccessFile() {
        return accessFile;
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

    public boolean isAlias(String id) {
        return lines.isAlias(id);
    }

    public boolean isScriptAlias(String id) {
        return lines.isScriptAlias(id);
    }

    public String getAlias(String key) {
        return lines.getAlias(key);
    }

    public String getScriptAlias(String key) {
        return lines.getScriptAlias(key);
    }
}
