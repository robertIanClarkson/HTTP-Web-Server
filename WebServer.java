import configuration.ConfigError;
import configuration.Configuration;
import request.Request;
import request.exceptions.RequestException;
import response.Response;
import response.exception.ResponseErrorException;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class WebServer {

    public static final int DEFAULT_PORT = 8096;

    public static void main(String[] args) throws IOException, ConfigError, RequestException, ResponseErrorException {
        new Configuration("conf/httpd.conf", "conf/mime.types");

        ServerSocket socket = new ServerSocket( Configuration.getHttpd().getListen() );
        Socket client = null;

        while( true ) {
//            try {
                client = socket.accept();
                Request request = new Request(client);
                printRequest(request);
                printStatusCode(request);
                Response response = new Response(client, request);
                sendResponse(client, response);
                printResponse(response);
                client.close();
//            } catch (Exception e) {
//                System.out.println(e);
//            }
        }
    }

    private static void sendResponse(Socket client, Response response) throws IOException {
        /* https://stackoverflow.com/questions/1176135/socket-send-and-receive-byte-array */
        DataOutputStream dOut = new DataOutputStream(client.getOutputStream());
        int length = response.toString().length();
        if(Response.hasBody) {
            length += Integer.parseInt(response.getBody().getLength());
        }
        byte[] res = response.toString().getBytes();
        dOut.writeInt(length); // write length of the message
        dOut.write(res);
        if(Response.hasBody) {
            dOut.write(response.getBody().getBody());
        }
    }

    private static void printRequest(Request request) {
        System.out.println("------------Request-------------");
        System.out.println(request);
        if(Request.hasBody) {
            System.out.println("***+++HAS BODY+++***");
//            String body = new String(request.getBody().getBody(), StandardCharsets.US_ASCII);
//            System.out.println(body);
        }
    }

    private static void printStatusCode(Request request) {
        System.out.println("------------StatusCode-------------");
        System.out.println(request.getId() + " ---> " + request.getCode());
    }

    private static void printResponse(Response response) {
        System.out.println("------------Response------------");
        System.out.println(response);
        if(Response.hasBody) {
            System.out.println("***+++HAS BODY+++***");
//            String body = new String(response.getBody().getBody(), StandardCharsets.US_ASCII);
//            System.out.println(body);
        }
    }
}