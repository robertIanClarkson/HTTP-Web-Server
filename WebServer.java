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

//        while( true ) {
            client = socket.accept();
//            outputRequest( client );
            System.out.println( "------------Request-------------" );
            Request request = new Request(client, config);
            Response response = new Response(client, config, request);
            sendResponse(client, response);
//            sendResponse( client );
            client.close();
//        }
    }

    private static void sendResponse(Socket client, Response response) throws IOException {
        PrintWriter out = new PrintWriter( client.getOutputStream(), true );
        out.print(response);
        System.out.println( "------------Response-------------" );
        System.out.print(response);
        System.out.print( "\n------------END-------------" );
//        out.println("HTTP/1.1 200 OK\r\n" +
//                "Content-Length: 11\r\n" +
//                "Content-Type: text/plain\r\n\r\n" +
//                "Hello World");
//        outputLineBreak();
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