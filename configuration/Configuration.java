package configuration;

public class Configuration {

    private HttpdConfig httpd;
    private MimeConfig mime;

    public Configuration(String httpdFile, String mimeFile) {
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
