package configuration;

import configuration.headers.*;

import java.io.IOException;
import java.util.HashMap;

public class Configuration {

    private HttpdConfig httpd;
    private MimeConfig mime;
    private HashMap<String, Header> headers;

    public Configuration(String httpdFile, String mimeFile) throws IOException {
        httpd = new HttpdConfig(httpdFile);
        mime = new MimeConfig(mimeFile);
        headers = new HashMap<>();
        initializeHeaders();
    }

    public HttpdConfig getHttpd() {
        return httpd;
    }

    public MimeConfig getMime() {
        return mime;
    }

    public Header getHeader(String name) {
        return headers.get(name);
    }

    private void initializeHeaders() {
        headers.put("Host", new Host());
        headers.put("Content-Length", new ContentLength());
        headers.put("Connection", new Connection());
        headers.put("Cache-Control", new CacheControl());
        headers.put("DNT", new DNT());
        headers.put("Upgrade-Insecure-Requests", new UpgradeInsecureRequest());
        headers.put("User-Agent", new UserAgent());
        headers.put("Sec-Fetch-User", new SecFetchUser());
        headers.put("Accept", new Accept());
        headers.put("Sec-Fetch-Site", new SecFetchSite());
        headers.put("Sec-Fetch-Mode", new SecFetchMode());
        headers.put("Accept-Encoding", new AcceptEncoding());
        headers.put("Accept-Language", new AcceptLanguage());
        headers.put("Pragma", new Pragma());
        headers.put("Referer", new Referer());
//        headers.put("", new Header());
    }
}
