package server;

import server.accessCheck.AccessCheck;
import server.configuration.ConfigError;
import server.configuration.Configuration;
import server.logs.Log;
import server.request.Request;
import server.resource.Resource;
import server.response.Response;
import server.response.exception.ResponseError;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public Server() {

    }

    public void start() {
        try {
            new Configuration("conf/httpd.conf", "conf/mime.types");
            new Resource();
            new AccessCheck();
            new Log();
            ServerSocket socket = new ServerSocket( Configuration.getHttpd().getListen() );
            Socket client = null;

            while( true ) {

                client = socket.accept();
                Request request = new Request(client);
                Resource.handleURI(request);
                AccessCheck.check(request);
                Response response = new Response(request);
                sendResponse(client, response);
                client.close();
                Log.newLog(request, response);
                printRequest(request);
                printStatusCode(request);
                printResponse(response);
            }
        } catch (IOException e) {
            System.out.println("SocketError ---> " + e);
        } catch (ConfigError e) {
            System.out.println("ConfigError ---> " + e);
        }
    }

    private static void sendResponse(Socket client, Response response) throws IOException {
        /* https://stackoverflow.com/questions/1176135/socket-send-and-receive-byte-array */
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        int length = response.toString().length();
        if(Response.hasBody) {
            length += response.getBody().getLength();
        }
        byte[] res = response.toString().getBytes();
        out.writeInt(length); // write length of the message
        out.write(res);
        if(Response.hasBody) {
            out.write(response.getBody().getBody());
            out.close();
        }
    }

    private static void printRequest(Request request) {
        System.out.println("------------Request-------------");
        System.out.println(request);
        if(Request.hasBody) {
            System.out.println("***+++HAS BODY+++***");
//            String body = new String(server.request.getBody().getBody(), StandardCharsets.US_ASCII);
//            System.out.println(body);
        }
    }

    private static void printStatusCode(Request request) {
        System.out.println("------------Code-------------");
        System.out.printf("%-3s---> %s\n", request.getCode(), request.getId());
    }

    private static void printResponse(Response response) {
        System.out.println("------------Response------------");
        System.out.println(response);
        if(Response.hasBody) {
//            System.out.println("***+++HAS BODY+++***");
//            String body = new String(server.response.getBody().getBody(), StandardCharsets.US_ASCII);
//            System.out.println(body);
        }
    }
}
