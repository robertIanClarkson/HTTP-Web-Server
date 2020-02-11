package response;

import configuration.Configuration;
import request.*;

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

    public Response(Socket client, Configuration config, Request request) {
        version = request.getVersion();
        code = new StatusCode("200");
        phrase = new Phrase("OK");

        headers = new Headers();
        headers.addHeader("Date", new Header(getServerTime()));
        headers.addHeader("Server", new Header("Clarkson_&_Gao_Server"));

        body = new Body();
    }

    private String getServerTime() { //https://stackoverflow.com/questions/7707555/getting-date-in-http-format-in-java
        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat dateFormat = new SimpleDateFormat(
//                "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, dd MMM yyyy HH:mm:ss", Locale.US);
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
        response += body;
        return response;
    }
}
