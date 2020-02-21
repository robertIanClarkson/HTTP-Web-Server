package server;

import server.accessCheck.AccessCheck;
import server.accessCheck.exceptions.Forbidden;
import server.accessCheck.exceptions.Unauthorized;
import server.configuration.ConfigError;
import server.configuration.Configuration;
import server.logs.Log;
import server.request.Request;
import server.request.exceptions.BadRequest;
import server.resource.Resource;
import server.response.Response;
import server.response.exception.InternalServerError;
import server.response.exception.NotFound;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket socket;
    private Socket client;

    public Server() throws IOException, ConfigError {
        new Configuration("conf/httpd.conf", "conf/mime.types");
        new Resource();
        new AccessCheck();
        new Log();
        socket = new ServerSocket( Configuration.getHttpd().getListen() );
        client = null;
    }

    public void start() throws  ConfigError, BadRequest {
        Request request = null;
        Response response = null;
        while( true ) {
            try {
                client = socket.accept();
                request = new Request(client);
                Resource.handleURI(request);
                AccessCheck.check(request);
                response = new Response(request);
            } catch(IOException | InternalServerError e) {
                System.out.println(e);
                response = new Response("500");
            } catch(BadRequest e) {
                System.out.println(e);
                response = new Response("400");
            }  catch(Unauthorized e) {
                System.out.println(e);
                response = new Response("401");
            } catch(Forbidden e) {
                System.out.println(e);
                response = new Response("403");
            } catch(NotFound e) {
                System.out.println(e);
                response = new Response("404");
            }  finally {
                try {
                    sendResponse(client, response);
                    client.close();
//                Log.newLog(request, response);
//                printRequest(request);
//                printStatusCode(request);
//                printResponse(response);
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
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
