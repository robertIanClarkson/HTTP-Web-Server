package server.request;

import response.StatusCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Request {

    private BufferedReader reader;

    private Method method;
    private Identifier id;
    private Version version;
    private Headers headers;
    private Body body;

    public static StatusCode code;
    public static boolean hasBody;

    public Request(Socket client) throws IOException {
        String bodyLength;
        hasBody = false;
        reader = new BufferedReader(
                new InputStreamReader( client.getInputStream() )
        );
        code = new StatusCode("200");
        process(reader.readLine());
        headers = new Headers(reader);
        if (headers.hasBody()) {
            bodyLength = headers.getHeader("Content-Length").getValue();
            body = new Body(client.getInputStream(), Integer.parseInt(bodyLength));
            hasBody = true;
        }
    }

    private void process(String line) {
        if (line != null) {
            String[] chunks = line.split(" ");
            if (chunks.length == 3) {
                method = new Method(chunks[0]);
                id = new Identifier(chunks[1]);
                version = new Version(chunks[2]);
            } else {
                code = new StatusCode("400");
            }
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

    public StatusCode getCode() {
        return code;
    }

    @Override
    public String toString() {
        String request = "";
        request += method;
        request += id;
        request += version.getVersion() + "\r\n";
        request += headers;
        return request;
    }
}

