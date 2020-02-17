package request;

import java.io.*;
import java.net.Socket;

public class Body {

    private byte[] body;

    public Body(InputStream is, int length) throws IOException {
        body = new byte[length];
        body = is.readNBytes(length);
    }

    public byte[] getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "\r\nbody";
    }
}
