package configuration.headers;

public class Host extends Header {
    private String host;
    public void init(String data) {
        host = data;
    }

    public String getHost() {
        return host;
    }
}
