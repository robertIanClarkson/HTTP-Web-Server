package request;

import configuration.Configuration;
import configuration.headers.Header;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class Headers {

    private HashMap<String, Header> headers;

    public Headers(BufferedReader reader, Configuration config) throws IOException {
        Header header;
        String line;
        String[] chunks;
        headers = new HashMap<>();
        while(!(line = reader.readLine()).equals("")) {
            System.out.println("> " + line);
            chunks = line.split(": ");
            header = config.getHeader(chunks[0]);
            header.init(chunks[1]);
            headers.put(chunks[0], header);
//            headers.put(chunks[0], config.getHeader(chunks[0]).init(chunks[1]));
        }
    }

    public boolean hasBody() {
        return headers.containsKey("Content-Length");
    }

    public Header getHeader(String name) {
        return headers.get(name);
    }
}
