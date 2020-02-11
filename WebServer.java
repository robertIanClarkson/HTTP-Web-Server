import configuration.Configuration;
import request.Request;
import response.Response;

import java.net.*;
import java.io.*;

public class WebServer {

    public static final int DEFAULT_PORT = 8096;

    public static void main(String[] args) throws IOException {
        Configuration config = new Configuration("conf/httpd.conf", "conf/mime.types");

        ServerSocket socket = new ServerSocket( config.getHttpd().getListen() );
        Socket client = null;

        while( true ) {
            client = socket.accept();
//            outputRequest( client );
            Request request = new Request(client, config);
            Response response = new Response(client, config, request);
            sendResponse(client, response);
//            sendResponse( client );
            client.close();
        }
    }

    private static void sendResponse(Socket client, Response response) throws IOException {
        PrintWriter out = new PrintWriter( client.getOutputStream(), true );
//        out.printf("%s %s %s\n%s\r\n%s",
//                response.getVersion(), response.getCode(), response.getPhrase(), response.getHeaders(), response.getBody());
//        out.println("HTTP/1.1 200 OK\n" +
//                "Date: Mon, 27 Jul 2009 12:28:53 GMT\n" +
//                "Server: Apache/2.2.14 (Win32)\n" +
//                "Last-Modified: Wed, 22 Jul 2009 19:15:56 GMT\n" +
//                "Content-Length: 88\n" +
//                "Content-Type: text/html\n" +
//                "Connection: Closed\r\n" +
//                "<html>\n" +
//                "<body>\n" +
//                "<h1>Hello, World!</h1>\n" +
//                "</body>\n" +
//                "</html");
        out.println("HTTP/1.1 200 OK\r\n" +
                "Content-Length: 11\r\n" +
                "Content-Type: text/plain\r\n\r\n" +
                "Hello World");
//        out.println("HTTP/1.1 404 Not Found\n" +
//                "Date: Sun, 18 Oct 2012 10:36:20 GMT\n" +
//                "Server: Apache/2.2.14 (Win32)\n" +
//                "Connection: Closed\n");
        outputLineBreak();
    }

    protected static void outputRequest( Socket client ) throws IOException {
        String line;

        BufferedReader reader = new BufferedReader(
                new InputStreamReader( client.getInputStream() )
        );

        while( true ) {
            line = reader.readLine();
            System.out.println( "> " + line );

            // Why do we need to do this?
            if( line.equals("") ) {
                break;
            }
        }
        outputLineBreak();
    }

    protected static void outputLineBreak() {
        System.out.println( "-------------------------" );
    }

    protected static void sendResponse( Socket client ) throws IOException {
        PrintWriter out = new PrintWriter( client.getOutputStream(), true );
        int gift = (int) Math.ceil( Math.random() * 100 );
        String response = "Gee, thanks, this is for you: " + gift;

        out.println( response );

        outputLineBreak();
        System.out.println( "I sent: " + response );
    }
}