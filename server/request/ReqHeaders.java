package server.request;

import server.request.exceptions.BadRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class ReqHeaders {

    private static List<String> HEADERS = Arrays.asList(
            "Host", "Connection", "Cache-Control", "DNT", "Upgrade-Insecure-Requests",
            "User-Agent", "Sec-Fetch-User", "Accept", "Sec-Fetch-Site", "Sec-Fetch-Mode",
            "Accept-Encoding", "Accept-Language", "Referer", "X-Requested-With",
            "If-Modified-Since", "If-None-Match", "Pragma", "Authorization",
            "Content-Length", "Origin", "Content-Type"

    );
    private HashMap<String, String> headers;

    public ReqHeaders(BufferedReader reader) throws IOException, BadRequest {
        String line, key, value;
        headers = new HashMap<>();
        line = reader.readLine();
        while(line != null && !(line.equals(""))) {
            key = line.substring(0, line.indexOf(":")).trim();
            value = line.substring(line.indexOf(":") + 1).trim();
            if(HEADERS.contains(key)) {
                headers.put(key, value);
            } else {
                throw new BadRequest("Invalid HTTP Header: " + line);
            }
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
