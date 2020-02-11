package request;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class Body {

    private Byte[] body;

    public Body() {
    }

    public Body(BufferedReader reader, int length) {
    }

    @Override
    public String toString() {
        return "\r\nbody";
    }
}
