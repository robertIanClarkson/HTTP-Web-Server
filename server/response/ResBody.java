package server.response;

import java.io.*;
import java.nio.file.Files;

public class ResBody {

    private byte[] body;

    public ResBody(InputStream is) throws IOException { // Script Body
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String strBody = "";
        String line;
        while((line = reader.readLine()) != null) {
            strBody += line + "\n";
        }
        body = strBody.getBytes();
    }

    public ResBody(String uri) throws IOException { // File Body
        File file = new File(uri);
        body = Files.readAllBytes(file.toPath());
    }

    public int getLength() {
        return body.length;
    }

    public byte[] getBody() {
        return body;
    }
}
