package server.logs;

import server.configuration.Configuration;
import server.request.Request;
import server.response.Response;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;

public class Log {
    public Log() {}

    public static void newLog(Request request, Response response) throws IOException {
        String bodyLength = "-";
        if(Response.hasBody) {
            bodyLength = String.valueOf(response.getBody().getLength());
        }
        FileWriter fileWriter = new FileWriter( Configuration.getHttpd().getLogFile(), true); //Set true for append mode
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.printf("%s - user [%s] \"%s %-25s %s\" %s %s\n",
                request.getHeaders().getHeader("Host").getValue(),
                response.getHeaders().getHeader("Date").getValue(),
                request.getMethod().getVerb(),
                request.getId().getOriginalURI(),
                request.getVersion().getVersion(),
                Request.code,
                bodyLength);
        printWriter.close();
    }
}
