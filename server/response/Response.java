package server.response;

import server.configuration.Configuration;
import server.request.Request;
import server.response.exception.InternalServerError;
import server.response.exception.NotFound;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Response {

    private ResVersion version;
    private ResCode code;
    private ResPhrase phrase;
    private ResHeaders headers;
    private ResBody body;

    public static boolean hasBody;

    public Response(String error) {
        headers = new ResHeaders();
        version = new ResVersion("HTTP/1.1");
        headers.addHeader("Date", getServerTime());
        headers.addHeader("Server", "Clarkson_&_Gao_Server");

        switch (error) {
            case "500": // Internal Server Error
                code = new ResCode("500");
                phrase = new ResPhrase(handlePhrase(code));
                break;
            case "400": // Bad Request
                code = new ResCode("400");
                phrase = new ResPhrase(handlePhrase(code));
                break;
            case "401": // Unauthorized
                code = new ResCode("401");
                phrase = new ResPhrase(handlePhrase(code));
                break;
            case "403": // Forbidden
                code = new ResCode("403");
                phrase = new ResPhrase(handlePhrase(code));
                break;
            case "404": // not Found
                code = new ResCode("404");
                phrase = new ResPhrase(handlePhrase(code));
                break;
        }
    }

    public Response(Request request) throws IOException, NotFound, InternalServerError {
        headers = new ResHeaders();
        version = new ResVersion(request.getRequestVersion().getVersion());
        headers.addHeader("Date", getServerTime());
        headers.addHeader("Server", "Clarkson_&_Gao_Server");
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

    }

    private void handleGET(Request request) throws IOException, NotFound, InternalServerError {
        if(Request.hasScriptAlias) {
            if(!runScript(request)) {
                throw new InternalServerError("Failed to run script \"" + request.getId().getOriginalURI() + "\"");
            }
        }
        headers.addHeader("Connection", "close");
        String uri = request.getId().getURI();
        if(Files.notExists(Paths.get(uri))){
            throw new NotFound(uri);
        }
        String extension = uri.substring(uri.lastIndexOf(".") + 1);
        headers.addHeader("Content-Type", Configuration.getMime().getMimeType(extension));
        try {
            body = new ResBody(request.getId().getURI());
            headers.addHeader("Content-Length", String.valueOf(body.getLength()));
            hasBody = true;
        } catch (Exception e) {
            System.out.println("No Body");
        }
        code = new ResCode("200");
        phrase = new ResPhrase(handlePhrase(code));
    }

    private String handlePhrase(ResCode code) {
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

    private boolean runScript(Request request) throws IOException {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("perl -T " + request.getId().getURI());
            process.waitFor();
            System.out.println(process.exitValue());
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public ResCode getCode() {
        return code;
    }

    public ResHeaders getResponseHeaders() {
        return headers;
    }

    public ResBody getResponseBody() {
        return body;
    }

    @Override
    public String toString() {
        String response = "";
        response += version;
        response += code;
        response += phrase;
        response += headers;
        return response;
    }
}
