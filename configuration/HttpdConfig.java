package configuration;

import java.io.IOException;

public class HttpdConfig {

    private HttpdLines lines;
    private String serverRoot, documentRoot, directoryIndex, logFile, scriptAlias;
    private int listen;

    HttpdConfig( String fileName ) throws IOException, ConfigError {
        lines = new HttpdLines(fileName);
        serverRoot = lines.getDirective("ServerRoot");
        documentRoot = lines.getDirective("DocumentRoot");
        directoryIndex = lines.getDirective("DirectoryIndex");
        logFile = lines.getDirective("LogFile");
        scriptAlias = lines.getDirective("ScriptAlias");
        listen = Integer.parseInt(lines.getDirective("Listen"));
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
        return lines.isAlias(key);
    }

    public String getAlias(String key) throws ConfigError {
        return lines.getAlias(key);
    }
}
