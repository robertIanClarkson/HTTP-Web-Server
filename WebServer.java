import server.Server;
import server.configuration.ConfigError;
import server.request.exceptions.BadRequest;

import java.io.IOException;

public class WebServer {
    public static void main(String[] args) throws IOException, ConfigError, BadRequest {
        new Server().start();
    }
}