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

    public static boolean hasBody;

    public Response(Socket client, Request request) throws ResponseErrorException, IOException {
        version = request.getVersion();
        code = request.getCode();
        phrase = new Phrase(handlePhrase(code));
        headers = new Headers();
        headers.addHeader("Date", new Header(getServerTime()));
        headers.addHeader("Server", new Header("Clarkson_&_Gao_Server"));
        hasBody = false;
        switch (request.getMethod().getVerb()) {
            case "GET" :
                handleGET(request);
                break;
            case "POST" :
                handlePOST(request);
                break;
            case "HEAD" :
                handleHEAD(request);
                break;
            case "PUT" :
                handlePUT(request);
                break;
            case "DELETE" :
                handleDELETE(request);
                break;
            case "TRACE" :
                handleTRACE(request);
                break;
            case "CONNECT" :
                handleCONNECT(request);
                break;
            case "PATCH" :
                handlePATCH(request);
                break;
        }
    }

    private void handlePATCH(Request request) {
    }

    private void handleCONNECT(Request request) {
    }

    private void handleTRACE(Request request) {
    }

    private void handleDELETE(Request request) {
    }

    private void handlePUT(Request request) {
    }

    private void handleHEAD(Request request) {
    }

    private void handlePOST(Request request) {
        if(code.getCode().equals("200")) {
            headers.addHeader("Connection", new Header("close"));
            headers.addHeader("WWW-Authenticate", new Header("Basic"));
            String uri = request.getId().getURI();
            String extension = uri.substring(uri.lastIndexOf(".") + 1);
            String contentType = Configuration.getMime().getMimeType(extension);
            headers.addHeader("Content-Type", new Header(contentType));
            try {
                body = new Body(request.getId().getURI());
                headers.addHeader("Content-Length", new Header(body.getLength()));
                hasBody = true;
            } catch (Exception e) {
                System.out.println("No Body");
            }
        }
    }

    private void handleGET(Request request) throws IOException {
        if(code.getCode().equals("200")) {
            headers.addHeader("Connection", new Header("close"));
            headers.addHeader("WWW-Authenticate", new Header("Basic"));
            String uri = request.getId().getURI();
            String extension = uri.substring(uri.lastIndexOf(".") + 1);
            String contentType = Configuration.getMime().getMimeType(extension);
            headers.addHeader("Content-Type", new Header(contentType));
            try {
                body = new Body(request.getId().getURI());
                headers.addHeader("Content-Length", new Header(body.getLength()));
                hasBody = true;
            } catch (Exception e) {
                System.out.println("No Body");
            }
        }
    }

    private String handlePhrase(StatusCode code) {
        switch(code.getCode()) {
            case "200" :
                return "OK";
            case "201" :
                return "Created";
            case "204" :
                return "No Content";
            case "301" :
                return "Moved Permanently";
            case "400" :
                return "Bad Request";
            case "401" :
                return "Unauthorized";
            case "403" :
                return "Forbidden";
            case "404" :
                return "Not Found";
            case "422" :
                return "Unprocessable Entity";
            case "500" :
                return "Internal Server Error";
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
