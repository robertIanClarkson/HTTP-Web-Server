package request;
import configuration.headers.ContentLength;
import configuration.headers.Header;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteBuffer;

public class Body {

    private int length;

    public Body(Socket client, ContentLength header) throws IOException {
        length = header.getLength();
    }
}
