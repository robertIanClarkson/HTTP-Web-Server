package server.request;

import java.io.*;

public class ReqBody {

    private byte[] body;

    public ReqBody(BufferedReader reader, int length) throws IOException {
        body = new byte[length];
        for(int i = 0; i < length; i++) {
            body[i] = (byte) reader.read();
        }
    }

    public byte[] getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "\r\nbody";
    }
}
