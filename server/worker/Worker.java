package server.worker;

import server.access_check.AccessCheck;
import server.access_check.exceptions.Forbidden;
import server.access_check.exceptions.Unauthorized;
import server.logs.Log;
import server.request.Request;
import server.request.exceptions.BadRequest;
import server.resource.Resource;
import server.response.Response;
import server.response.exception.InternalServerError;
import server.response.exception.NotFound;
import server.response.exception.NotModified;

import java.io.IOException;
import java.net.Socket;

public class Worker extends Thread implements Runnable {
    private Socket client;

    public Worker(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        Request request = null;
        Response response = null;
        Resource resource = null;
        AccessCheck accessCheck = null;
        Sender sender = null;
        try {
            request = new Request(client);
            resource = new Resource();
            resource.handleURI(request);
            accessCheck = new AccessCheck();
            accessCheck.check(request);
            response = new Response(request);
        } catch (IOException | InternalServerError e) {
            System.out.println(e);
            response = new Response("500");
        } catch (BadRequest e) {
            System.out.println(e);
            response = new Response("400");
        } catch (Unauthorized e) {
            System.out.println(e);
            response = new Response("401");
        } catch (Forbidden e) {
            System.out.println(e);
            response = new Response("403");
        } catch (NotFound e) {
            System.out.println(e);
            response = new Response("404");
        } catch (NotModified e) {
            System.out.println(e);
            response = new Response("304");
        } finally {
            try {
                sender = new Sender();
                sender.sendResponse(client, response);
            } catch (IOException | NullPointerException e) {
                System.out.println("Error: Worker.run.sendResponse --> " + e);
            }
            try {
                client.close();
            } catch (IOException e) {
                System.out.println("Error: Worker.run.close --> " + e);
            }
            try {
                Log.newLog(request, response);
            } catch (Exception e) {
                System.out.println("Logging Error : " + e);
            }
//            ServerDebug.printRequest(request);
//            ServerDebug.printStatusCode(request, response);
//            ServerDebug.printResponse(response);
        }
    }


}
