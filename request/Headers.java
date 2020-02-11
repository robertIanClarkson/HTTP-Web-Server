package request;

import configuration.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;


public class Headers {

    private HashMap<String, Header> headers;

    public Headers() {
        headers = new HashMap<>();
    }

    public Headers(BufferedReader reader, Configuration config) throws IOException {
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
            headers.put(key, header);
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
}
