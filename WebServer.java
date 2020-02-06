import request.Request;

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
            Request request = new Request(client);

//            sendResponse( client );
            client.close();
//        }
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