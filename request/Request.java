package request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Request {

    private Method method;
    private Identifier id;
    private Version version;
    private Headers headers;
    private Body body;

    public Request(Socket client) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader( client.getInputStream() )
        );
        process(reader.readLine());
    }

    private void process(String line) {
        if (isMethod(line)) {
            String[] chunks = line.split(" ");
            if (chunks.length == 3) {
                method = new Method(chunks[0]);
                id = new Identifier(chunks[1]);
                version = new Version(chunks[2]);
            }
        } else if (isHeaders(line)) {
//            headers = new Headers();
        } else { //is body?
//            body = new Body();
        }
    }

    private boolean isMethod(String line) {
        return (line.contains("GET") ||
                line.contains("HEAD") ||
                line.contains("POST") ||
                line.contains("PUT") ||
                line.contains("DELETE") ||
                line.contains("TRACE") ||
                line.contains("OPTIONS") ||
                line.contains("CONNECT") ||
                line.contains("PATCH"));
    }

//    private boolean isIdentifier(String line) {
//        return true;
//    }
//
//    private boolean isVersion(String line) {
//        return (line.contains() ||
//                line.contains);
//    }

    private boolean isHeaders(String line) {
        return true;
    }

    public Method getMethod() {
        return method;
    }

    public Identifier getId() {
        return id;
    }

    public Version getVersion() {
        return version;
    }

    public Headers getHeaders() {
        return headers;
    }

    public Body getBody() {
        return body;
    }
}

