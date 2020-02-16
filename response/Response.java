package response;

import configuration.Configuration;
import request.*;
import response.exception.ResponseErrorException;

import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Response {

    private Version version;
    private StatusCode code;
    private Phrase phrase;
    private Headers headers;
    private Body body;

    public Response(Socket client, Request request) throws ResponseErrorException, IOException {
        version = request.getVersion();
        code = request.getCode();
        phrase = new Phrase(handlePhrase(code));
        headers = new Headers();
        headers.addHeader("Date", new Header(getServerTime()));
        headers.addHeader("Server", new Header("Clarkson_&_Gao_Server"));
        switch (request.getMethod().getVerb()) {
            case "GET" :
                handleGETRequest(request);
                break;
            case "POST" :
//                handlePOSTRequest(request);
                break;
        }

//        version = request.getVersion();
//        code = new StatusCode("200");
//        phrase = new Phrase("OK");
//
//

//        body = new Body();
    }

    private void handleGETRequest(Request request) throws IOException {
        headers.addHeader("Connection", new Header("close"));
        headers.addHeader("WWW-Authenticate", new Header("Basic"));

        String uri = request.getId().getURI();
        String extension = uri.substring(uri.lastIndexOf(".") + 1);
        String contentType = Configuration.getMime().getMimeType(extension);
        headers.addHeader("Content-Type", new Header(contentType));

        body = new Body(request.getId().getURI());

        headers.addHeader("Content-Length", new Header(body.getLength()));
    }

    private String handlePhrase(StatusCode code) {
        switch(code.getCode()) {
            case "100" :
                return "Continue";
            case "101" :
                return "Switching Protocols";
            case "102" :
                return "Processing";
            case "200" :
                return "OK";
            case "201" :
                return "Created";
            case "202" :
                return " Accepted";
            case "204" :
                return "No Content";
            case "301" :
                return "Moved Permanently";
            case "304" :
                return "Not Modified";
            case "400" :
                return "Bad Request";
            case "401" :
                return "Unauthorized";
            case "403" :
                return "Forbidden";
            case "404" :
                return "Not Found";
            case "418" :
                return "I’m a teapot";
            case "420" :
                return "Enhance Your Calm";
            case "500" :
                return "Internal Server Error";
            case "501" :
                return "Not Implemented";
            case "502" :
                return "Bad Gateway";
            case "503" :
                return "Service Unavailable";
        }
        return null;
    }

    private String getServerTime() { //https://stackoverflow.com/questions/7707555/getting-date-in-http-format-in-java
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
//        SimpleDateFormat dateFormat = new SimpleDateFormat(
//                "EEE, dd MMM yyyy HH:mm:ss", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
        return dateFormat.format(calendar.getTime());
    }

    public Version getVersion() {
        return version;
    }

    public StatusCode getCode() {
        return code;
    }

    public Phrase getPhrase() {
        return phrase;
    }

    public Headers getHeaders() {
        return headers;
    }

    public Body getBody() {
        return body;
    }

    @Override
    public String toString() {
        String response = "";
        response += version;
        response += code;
        response += phrase;
        response += headers;
//        response += body;
        return response;
    }
}
