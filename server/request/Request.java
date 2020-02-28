package server.request;

import server.request.exceptions.BadRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Request {
    private BufferedReader reader;
    private ReqMethod method;
    private ReqIdentifier id;
    private ReqVersion requestVersion;
    private ReqHeaders requestHeaders;
    private ReqBody requestBody;
    private boolean hasScriptAlias;

    public Request(Socket client) throws IOException, BadRequest {
        String bodyLength;
        reader = new BufferedReader(
                new InputStreamReader( client.getInputStream() )
        );
        process(reader.readLine());
        requestHeaders = new ReqHeaders(reader);
        if (requestHeaders.hasBody()) {
            bodyLength = requestHeaders.getHeader("Content-Length");
            requestBody = new ReqBody(client.getInputStream(), Integer.parseInt(bodyLength));
        }
    }

    private void process(String line) throws BadRequest {
        if (line != null) {
            String[] chunks = line.split(" ");
            if (chunks.length == 3) {
                method = new ReqMethod(chunks[0]);
                id = new ReqIdentifier(chunks[1]);
                requestVersion = new ReqVersion(chunks[2]);
            } else {
                throw new BadRequest("Missing Field: " + line);
            }
        } else {
            throw new BadRequest("null request");
        }
    }

    public ReqMethod getMethod() {
        return method;
    }

    public ReqIdentifier getId() {
        return id;
    }

    public ReqVersion getRequestVersion() {
        return requestVersion;
    }

    public ReqHeaders getRequestHeaders() {
        return requestHeaders;
    }

    public ReqBody getRequestBody() {
        return requestBody;
    }

    public boolean hasBody() {
        return (requestBody != null);
    }

    public void hasScriptAlias(boolean hasScriptAlias) {
        this.hasScriptAlias = hasScriptAlias;
    }

    public boolean hasScriptAlias() {
        return this.hasScriptAlias;
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

