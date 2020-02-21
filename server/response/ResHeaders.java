package server.response;

import java.util.HashMap;

public class ResHeaders {
    private HashMap<String, String> headers;

    public ResHeaders() {
        headers = new HashMap<>();
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    @Override
    public String toString() {
        String format = "";
        for(String key : headers.keySet()) {
            format += key + ": " + headers.get(key) + "\r\n";
        }
        format += "\r\n";
        return format;
    }
}
