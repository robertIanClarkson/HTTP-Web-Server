package request;

import configuration.Configuration;
import request.exceptions.InvalidHeaderException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class Headers {

    private static List<String> HEADERS = Arrays.asList(
            "Host", "Connection", "Cache-Control", "DNT", "Upgrade-Insecure-Requests",
            "User-Agent", "Sec-Fetch-User", "Accept", "Sec-Fetch-Site", "Sec-Fetch-Mode",
            "Accept-Encoding", "Accept-Language"
    );
    private HashMap<String, Header> headers;

    public Headers() {
        headers = new HashMap<>();
    }

    public Headers(BufferedReader reader) throws IOException, InvalidHeaderException {
        Header header;
        String line, key, value;
        String[] data;
        headers = new HashMap<>();
        while(!(line = reader.readLine()).equals("")) {
            System.out.println("> " + line);
            data = line.split(": ");
            key = data[0];
            value = data[1];
            header = new Header(value);
            if(HEADERS.contains(key)) {
                headers.put(key, header);
            } else {
                throw new InvalidHeaderException("\"" + key + "\" is not a valid Header");
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
        return format;
    }
}
