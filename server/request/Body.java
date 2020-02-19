package server.request;

import java.io.*;

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
