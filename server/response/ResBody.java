package server.response;

/* Used code from: https://stackoverflow.com/questions/858980/file-to-byte-in-java */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ResBody {

    private byte[] body;

    public ResBody(InputStream in) throws IOException { // Script Body
        body = in.readAllBytes();
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
