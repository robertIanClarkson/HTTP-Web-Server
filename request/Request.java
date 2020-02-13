package request;

import configuration.ConfigError;
import request.exceptions.RequestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.rmi.RemoteException;

public class Request {

    private BufferedReader reader;
    private Method method;
    private Identifier id;
    private Version version;
    private Headers headers;
    private Body body;

    public Request(Socket client) throws IOException, RequestException, ConfigError {
        String length;
        reader = new BufferedReader(
                new InputStreamReader( client.getInputStream() )
        );
        process(reader.readLine());
        headers = new Headers(reader);
        if (headers.hasBody()) {
            length = headers.getHeader("Content-Length").getValue();
            body = new Body(reader, Integer.parseInt(length));
        }
    }

    private void process(String line) throws RequestException, ConfigError {
        System.out.println("> " + line);
        String[] chunks = line.split(" ");
        if(chunks.length == 3) {
            method = new Method(chunks[0]);
            id = new Identifier(chunks[1]);
            version = new Version(chunks[2]);
        } else {
            throw new RequestException("Invalid HTTP Syntax");
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

