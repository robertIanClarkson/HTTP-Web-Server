package request;

import request.exceptions.InvalidHeaderException;
import response.StatusCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class Headers {

    private static List<String> HEADERS = Arrays.asList(
            "Host", "Connection", "Cache-Control", "DNT", "Upgrade-Insecure-Requests",
            "User-Agent", "Sec-Fetch-User", "Accept", "Sec-Fetch-Site", "Sec-Fetch-Mode",
            "Accept-Encoding", "Accept-Language", "Referer", "X-Requested-With",
            "If-Modified-Since", "If-None-Match"

    );
    private HashMap<String, Header> headers;

    public Headers() {
        headers = new HashMap<>();
    }

    public Headers(BufferedReader reader) throws IOException, InvalidHeaderException {
        Header header;
        String line, key, value;
        headers = new HashMap<>();
        while(!(line = reader.readLine()).equals("")) {
            key = line.substring(0, line.indexOf(":")).trim();
            value = line.substring(line.indexOf(":") + 1).trim();
            header = new Header(value);
            if(HEADERS.contains(key)) {
                headers.put(key, header);
            } else {
                Request.code = new StatusCode("400");
//                throw new InvalidHeaderException("\"" + key + "\" is not a valid Header");
            }
        }
    }

    public boolean hasBody() {
        return headers.containsKey("Content-Length");
    }

    public Header getHeader(String key) {
        return headers.get(key);
    }

    public void addHeader(String key, Header value) {
        headers.put(key, value);
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
