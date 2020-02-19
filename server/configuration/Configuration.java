package server.configuration;

import java.io.IOException;

public class Configuration {

    private static HttpdConfig httpd;
    private static MimeConfig mime;

    public Configuration(String httpdFile, String mimeFile) throws IOException, ConfigError {
        httpd = new HttpdConfig(httpdFile);
        mime = new MimeConfig(mimeFile);
    }

    public static HttpdConfig getHttpd() {
        return httpd;
    }

    public static MimeConfig getMime() {
        return mime;
    }
}
