package server.request;

import server.request.exceptions.BadRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Request {
    private BufferedReader reader;
    private Method method;
    private Identifier id;
    private RequestVersion requestVersion;
    private RequestHeaders requestHeaders;
    private RequestBody requestBody;
    public static boolean hasBody;
    public static boolean hasScriptAlias;

    public Request(Socket client) throws IOException, BadRequest {
        String bodyLength;
        hasBody = false;
        reader = new BufferedReader(
                new InputStreamReader( client.getInputStream() )
        );
        process(reader.readLine());
        requestHeaders = new RequestHeaders(reader);
        if (requestHeaders.hasBody()) {
            bodyLength = requestHeaders.getHeader("Content-Length");
            requestBody = new RequestBody(client.getInputStream(), Integer.parseInt(bodyLength));
            hasBody = true;
        }
    }

    private void process(String line) throws BadRequest {
        if (line != null) {
            String[] chunks = line.split(" ");
            if (chunks.length == 3) {
                method = new Method(chunks[0]);
                id = new Identifier(chunks[1]);
                requestVersion = new RequestVersion(chunks[2]);
            } else {
                throw new BadRequest("Missing Field: " + line);
            }
        }
    }

    public Method getMethod() {
        return method;
    }

    public Identifier getId() {
        return id;
    }

    public RequestVersion getRequestVersion() {
        return requestVersion;
    }

    public RequestHeaders getRequestHeaders() {
        return requestHeaders;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    @Override
    public String toString() {
        String request = "";
        request += method;
        request += id.getOriginalURI() + " ";
        request += requestVersion.getVersion() + "\r\n";
        request += requestHeaders;
        return request;
    }
}

