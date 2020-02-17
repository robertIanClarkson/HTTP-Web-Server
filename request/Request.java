package request;

import configuration.ConfigError;
import request.exceptions.RequestException;
import response.StatusCode;

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

    public static StatusCode code;

    public Request(Socket client) throws IOException, RequestException, ConfigError {
        String bodyLength;
        reader = new BufferedReader(
                new InputStreamReader( client.getInputStream() )
        );
        code = new StatusCode("200");
        process(reader.readLine());
        headers = new Headers(reader);
        if (headers.hasBody()) {
            bodyLength = headers.getHeader("Content-Length").getValue();
            body = new Body(reader, Integer.parseInt(bodyLength));
        }
    }

    private void process(String line) throws RequestException, ConfigError {
        System.out.println("> " + line);
        if (line != null) {
            String[] chunks = line.split(" ");
            if (chunks.length == 3) {
                method = new Method(chunks[0]);
                id = new Identifier(chunks[1]);
                version = new Version(chunks[2]);
            } else {
                code = new StatusCode("400");
                //            throw new RequestException("Invalid HTTP Syntax");
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
}

