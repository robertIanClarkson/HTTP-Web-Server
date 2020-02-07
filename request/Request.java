package request;

import configuration.Configuration;
import configuration.headers.ContentLength;
import configuration.headers.Host;

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
    private boolean methodSet;
    public Request(Socket client, Configuration config) throws IOException {
        String line;
        methodSet = false;
        reader = new BufferedReader(
                new InputStreamReader( client.getInputStream() )
        );
        process(reader.readLine());
        if (methodSet) {
            headers = new Headers(reader, config);
            if (headers.hasBody()) {
                ContentLength length = (ContentLength) headers.getHeader("Content-Length");
                body = new Body(client, length);
            }
        }
    }

    private void process(String line) throws IOException{
        if (isMethod(line)) {
            String[] chunks = line.split(" ");
            if (chunks.length == 3) {
                method = new Method(chunks[0]);
                id = new Identifier(chunks[1]);
                version = new Version(chunks[2]);
                methodSet = true;
            } else {
                throw new IOException("Request Method Syntax");
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
}

