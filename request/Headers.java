package request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class Headers {

    private HashMap<String, String> headers;

    public Headers(BufferedReader reader) throws IOException {
        String line;
        String[] chunks;
        headers = new HashMap<>();
        while(!(line = reader.readLine()).equals("")) {
            chunks = line.split(": ");
            headers.put(chunks[0], chunks[1]);
        }
    }

    public boolean hasBody() {
        return headers.containsKey("Content-Length");
    }
}
