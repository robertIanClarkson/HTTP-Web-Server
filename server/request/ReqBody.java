package server.request;

import java.io.*;

public class ReqBody {

    private byte[] body;

    public ReqBody(InputStream is, int length) throws IOException {
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
