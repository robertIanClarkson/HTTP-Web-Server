package server.response;

import server.configuration.Configuration;
import server.request.Request;
import server.response.exception.InternalServerError;
import server.response.exception.NotFound;
import server.response.exception.NotModified;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
                break;
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
            case "GET":
                handleGET(request);
                break;
            case "POST":
                handlePOST(request);
                break;
            case "HEAD":
                handleHEAD(request);
                break;
            case "PUT":
                handlePUT(request);
                break;
            case "DELETE":
                handleDELETE(request);
                break;
        }
    }

    private void handleDELETE(Request request) throws NotFound, IOException {
        headers.addHeader("Connection", "close");
        String uri = request.getId().getURI();
        if (Files.exists(Paths.get(uri))) {
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
        if (Files.exists(Paths.get(uri))) {
            Files.delete(Paths.get(uri));
        }
        if (newFile.createNewFile()) {
            if (request.hasBody()) {
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

    private void handleHEAD(Request request) throws NotFound, InternalServerError, NotModified {
        if (request.hasScriptAlias()) {
            if (!runScript(request)) {
                throw new InternalServerError("Failed to run script \"" + request.getId().getOriginalURI() + "\"");
            }
        } else {
            headers.addHeader("Connection", "close");
            String uri = request.getId().getURI();
            if (Files.notExists(Paths.get(uri))) {
                throw new NotFound(uri);
            } else if (!modified(request)) {
                throw new NotModified("File \"" + request.getId().getOriginalURI() + "\" was not modified");
            } else {
                String extension = uri.substring(uri.lastIndexOf(".") + 1);
                headers.addHeader("Content-Type", Configuration.getMime().getMimeType(extension));
                code = new ResCode("200");
                phrase = new ResPhrase(handlePhrase(code));
            }
        }
    }

    private void handlePOST(Request request) throws InternalServerError, NotFound, NotModified {
        headers.addHeader("Connection", "close");
        if (request.hasScriptAlias()) {
            if (runScript(request)) {
                System.out.println("******Script \"" + request.getId().getOriginalURI() + "\" Ran Successfully******");
            } else {
                throw new InternalServerError("Failed to run script \"" + request.getId().getOriginalURI() + "\"");
            }
        } else {
            String uri = request.getId().getURI();
            if (Files.notExists(Paths.get(uri))) {
                throw new NotFound(request.getId().getOriginalURI());
            } else if (!modified(request)) {
                throw new NotModified("File \"" + request.getId().getOriginalURI() + "\" was not modified");
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

    private void handleGET(Request request) throws NotFound, InternalServerError, NotModified {
        headers.addHeader("Connection", "close");
        if (request.hasScriptAlias()) {
            if (runScript(request)) {
                System.out.println("******Script \"" + request.getId().getOriginalURI() + "\" Ran Successfully******");
            } else {
                throw new InternalServerError("Failed to run script \"" + request.getId().getOriginalURI() + "\"");
            }
        } else {
            String uri = request.getId().getURI();
            if (Files.notExists(Paths.get(uri))) {
                throw new NotFound(request.getId().getOriginalURI());
            } else if (!modified(request)) {
                SimpleDateFormat requestDatePattern = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
                File file = new File(uri);
                String fileDate = requestDatePattern.format(file.lastModified());
                headers.addHeader("Last-Modified", fileDate);
                code = new ResCode("304");
                phrase = new ResPhrase(handlePhrase(code));
                return;
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

    private boolean modified(Request request) throws InternalServerError {
        if (request.getRequestHeaders().hasHeader("If-Modified-Since")) {
            String requestDateValue = request.getRequestHeaders().getHeader("If-Modified-Since");
            requestDateValue = requestDateValue.substring(requestDateValue.indexOf(" ") + 1);
            SimpleDateFormat requestDatePattern = new SimpleDateFormat("dd MMM yyyy HH:mm:ss zzz");
            String uri = request.getId().getURI();
            File file = new File(uri);
            SimpleDateFormat fileDatePattern = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            try {
                Date requestDate = requestDatePattern.parse(requestDateValue);
                Date fileDate = fileDatePattern.parse(fileDatePattern.format(file.lastModified()));
                return fileDate.after(requestDate);
            } catch (ParseException e) {
                throw new InternalServerError("Failed to parse Request Date");
            }
        }
        return true;
    }

    private String handlePhrase(ResCode code) {
        switch (code.getCode()) {
            case "200":
                return "OK";
            case "201":
                return "Created";
            case "204":
                return "No Content";
            case "301":
                return "Moved Permanently";
            case "304":
                return "Not Modified";
            case "400":
                return "Bad Request";
            case "401":
                return "Unauthorized";
            case "403":
                return "Forbidden";
            case "404":
                return "Not Found";
            case "422":
                return "Unprocessable Entity";
            case "500":
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
            ProcessBuilder pb = new ProcessBuilder(request.getId().getURI());
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
            body = new ResBody(process.getInputStream());
            headers.addHeader("Content-Length", String.valueOf(body.getLength()));
        } catch (Exception e) {
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
