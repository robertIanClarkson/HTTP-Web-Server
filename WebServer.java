import server.Server;
import server.configuration.ConfigError;

import java.io.IOException;

public class WebServer {
    public static void main(String[] args) throws IOException, ConfigError {
        new Server().start();
    }
}