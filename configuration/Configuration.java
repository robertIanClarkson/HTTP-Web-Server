package configuration;

import java.io.IOException;

public class Configuration {

    private HttpdConfig httpd;
    private MimeConfig mime;

    public Configuration(String httpdFile, String mimeFile) throws IOException {
        httpd = new HttpdConfig(httpdFile);
        mime = new MimeConfig(mimeFile);
    }

    public HttpdConfig getHttpd() {
        return httpd;
    }

    public MimeConfig getMime() {
        return mime;
    }
}
