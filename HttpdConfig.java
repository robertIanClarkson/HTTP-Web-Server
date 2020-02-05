import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HttpdConfig {

    private BufferedReader reader;

    public String serverRoot, documentRoot, listen, logFile, scriptAlias, aliasA, aliasB;

    HttpdConfig( String fileName ) {
        try {
            reader = new BufferedReader(new FileReader( fileName ));
            serverRoot = stripQuotes(stripData(reader.readLine()));
            documentRoot = stripQuotes(stripData(reader.readLine()));
            listen = stripData(reader.readLine());
            logFile = stripQuotes(stripData(reader.readLine()));
            scriptAlias = stripQuotes(stripData(reader.readLine()));
            aliasA = stripQuotes(stripData(reader.readLine()));
            aliasB = stripQuotes(stripData(reader.readLine()));
        } catch (IOException e) {
            System.out.println("HttpdConfig --> IOException: " + e);
        }
    }

    private String stripQuotes( String line ) {
        int x = line.indexOf("\"");
        int y = line.lastIndexOf("\"");
        return line.substring( x+1, y);
    }

    private String stripData(String line) {
        int x = line.indexOf(" ");
        return line.substring(x+1);
    }
}
