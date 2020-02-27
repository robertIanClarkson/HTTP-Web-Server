package server.logs;

import server.configuration.Configuration;
import server.request.Request;
import server.response.Response;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Log {
    public Log() {}

    public static void newLog(Request request, Response response) throws IOException {
        String bodyLength = "-";
        if(Response.hasBody) {
            bodyLength = String.valueOf(response.getResponseBody().getLength());
        }
        String user = "-";
        FileWriter fileWriter = new FileWriter( Configuration.getHttpd().getLogFile(), true); //Set true for append mode
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.printf("%s - %s [%s] \"%s %-25s %s\" %s %s\n",
                request.getRequestHeaders().getHeader("Host"),
                user,
                response.getResponseHeaders().getHeader("Date"),
                request.getMethod().getVerb(),
                request.getId().getOriginalURI(),
                request.getRequestVersion().getVersion(),
                response.getCode(),
                bodyLength);
        printWriter.close();
    }
}
