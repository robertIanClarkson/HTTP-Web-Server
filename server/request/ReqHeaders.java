package server.request;

import server.request.exceptions.BadRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class ReqHeaders {

    private HashMap<String, String> headers;

    public ReqHeaders(BufferedReader reader) throws IOException, BadRequest {
        String line, key, value;
        headers = new HashMap<>();
        line = reader.readLine();
        while(line != null && !(line.equals(""))) {
            key = line.substring(0, line.indexOf(":")).trim();
            value = line.substring(line.indexOf(":") + 1).trim();
            headers.put(key, value);
            line = reader.readLine();
        }
    }

    public boolean hasBody() {
        return headers.containsKey("Content-Length");
    }

    public boolean hasHeader(String key) {
        return headers.containsKey(key);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public HashMap<String, String> getHeaders() {
        return headers;
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
