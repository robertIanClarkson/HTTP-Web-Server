package server.response;

/* Used code from: https://stackoverflow.com/questions/858980/file-to-byte-in-java */
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ResBody {

    private byte[] body;

    public ResBody(String uri) throws IOException {
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
