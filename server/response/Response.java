package server.response;

import server.configuration.Configuration;
import server.request.Request;
import server.response.exception.InternalServerError;
import server.response.exception.NotFound;
import server.response.exception.NotModified;

import java.io.*;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class Response {

    private ResVersion version;
    private ResCode code;
    private ResPhrase phrase;
    private ResHeaders headers;
    private ResBody body;

    public Response(String error) {
        headers = new ResHeaders();
        version = new ResVersion("HTTP/1.1");
        headers.addHeader("Date", getServerTime());
        headers.addHeader("Server", "Clarkson_&_Gao_Server");

        switch (error) {
            case "304":
                code = new ResCode("304");
                phrase = new ResPhrase(handlePhrase(code));
            case "400": // Bad Request
                code = new ResCode("400");
                phrase = new ResPhrase(handlePhrase(code));
                break;
            case "401": // Unauthorized
                code = new ResCode("401");
                phrase = new ResPhrase(handlePhrase(code));
                headers.addHeader("WWW-Authenticate", "Basic");
                break;
            case "403": // Forbidden
                code = new ResCode("403");
                phrase = new ResPhrase(handlePhrase(code));
                break;
            case "404": // not Found
                code = new ResCode("404");
                phrase = new ResPhrase(handlePhrase(code));
                break;
            case "500": // Internal Server Error
                code = new ResCode("500");
                phrase = new ResPhrase(handlePhrase(code));
                break;
        }
    }

    public Response(Request request) throws IOException, NotFound, InternalServerError, NotModified {
        headers = new ResHeaders();
        version = new ResVersion(request.getRequestVersion().getVersion());
        headers.addHeader("Date", getServerTime());
        headers.addHeader("Server", "Clarkson_&_Gao_Server");
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
        }
    }

    private void handleDELETE(Request request) throws NotFound, IOException {
        headers.addHeader("Connection", "close");
        String uri = request.getId().getURI();
        if(Files.exists(Paths.get(uri))){
            Files.delete(Paths.get(uri));
        } else {
            throw new NotFound("Cannot Delete : No File --> " + uri);
        }
        code = new ResCode("201");
        phrase = new ResPhrase(handlePhrase(code));
    }

    private void handlePUT(Request request) throws IOException, InternalServerError {
        FileOutputStream out;
        String uri = request.getId().getURI();
        File newFile = new File(uri);
        if(Files.exists(Paths.get(uri))){
            Files.delete(Paths.get(uri));
        }
        if(newFile.createNewFile()){
            if(request.hasBody()) {
                out = new FileOutputStream(newFile, false);
                out.write(request.getRequestBody().getBody());
                out.close();
            }
        } else {
            throw new InternalServerError("Failed to create file : " + request.getId().getOriginalURI());
        }
        headers.addHeader("Connection", "close");
        code = new ResCode("201");
        phrase = new ResPhrase(handlePhrase(code));
    }

    private void handleHEAD(Request request) throws NotFound, InternalServerError, IOException {
        if(request.hasScriptAlias()) {
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
        code = new ResCode("200");
        phrase = new ResPhrase(handlePhrase(code));
    }

    private void handlePOST(Request request) throws InternalServerError, NotFound, NotModified {
        headers.addHeader("Connection", "close");
        if(request.hasScriptAlias()) {
            if(!runScript(request)) {
                throw new InternalServerError("Failed to run script \"" + request.getId().getOriginalURI() + "\"");
            }
        } else {
            String uri = request.getId().getURI();
            if (Files.notExists(Paths.get(uri))) {
                throw new NotFound(uri);
            } else if (!modified(uri)) {
                throw new NotModified("File \"" + uri + "\" was not modified");
            } else {
                String extension = uri.substring(uri.lastIndexOf(".") + 1);
                headers.addHeader("Content-Type", Configuration.getMime().getMimeType(extension));
                try {
                    body = new ResBody(request.getId().getURI());
                    headers.addHeader("Content-Length", String.valueOf(body.getLength()));
                } catch (Exception e) {
                    System.out.println("Notice: Response.handleGet --> No Body");
                }
            }
        }
        code = new ResCode("200");
        phrase = new ResPhrase(handlePhrase(code));
    }

    private void handleGET(Request request) throws IOException, NotFound, InternalServerError, NotModified {
        headers.addHeader("Connection", "close");
        if(request.hasScriptAlias()) {
            if(!runScript(request)) {
                throw new InternalServerError("Failed to run script \"" + request.getId().getOriginalURI() + "\"");
            }
        } else {
            String uri = request.getId().getURI();
            if (Files.notExists(Paths.get(uri))) {
                throw new NotFound(uri);
            } else if (!modified(uri)) {
                throw new NotModified("File \"" + uri + "\" was not modified");
            } else {
                String extension = uri.substring(uri.lastIndexOf(".") + 1);
                headers.addHeader("Content-Type", Configuration.getMime().getMimeType(extension));
                try {
                    body = new ResBody(request.getId().getURI());
                    headers.addHeader("Content-Length", String.valueOf(body.getLength()));
                } catch (Exception e) {
                    System.out.println("Notice: Response.handleGet --> No Body");
                }
            }
        }
        code = new ResCode("200");
        phrase = new ResPhrase(handlePhrase(code));
    }

    private boolean modified(String uri) {
        return true;
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
        dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
        return dateFormat.format(calendar.getTime());
    }

    private boolean runScript(Request request) {
        try {
            String envKey, envValue;
            ProcessBuilder pb = new ProcessBuilder(/*Path for script*/);
            for (String key : request.getRequestHeaders().getHeaders().keySet()) {
                envKey = "HTTP_" + key.toUpperCase();
                envValue = request.getRequestHeaders().getHeader(key);
                pb.environment().put(envKey, envValue);
            }
            if (request.getId().hasQuery()) {
                pb.environment().put("QUERYSTRING", request.getId().getQuery().getQuery());
            }
            pb.environment().put("HTTP_VERB", request.getMethod().getVerb());
            pb.environment().put("HTTP_PROTOCOL", request.getRequestVersion().getVersion());
            Process process = pb.start();
            OutputStream stdin = process.getOutputStream();
            for(int i = 0; i < request.getRequestBody().getBody().length; i++) {
                stdin.write(request.getRequestBody().getBody()[i]);
            }
            stdin.flush();
            stdin.close();
            body = new ResBody(process.getInputStream());
            headers.addHeader("Content-Length", String.valueOf(body.getLength()));
        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println("Response.runScript : " + e);
            return false;
        }
        return true;
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

    public boolean hasBody() {
        return (body != null);
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
