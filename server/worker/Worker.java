package server.worker;

import server.access_check.AccessCheck;
import server.access_check.exceptions.Forbidden;
import server.access_check.exceptions.Unauthorized;
import server.configuration.ConfigError;
import server.logs.Log;
import server.request.Request;
import server.request.exceptions.BadRequest;
import server.resource.Resource;
import server.response.Response;
import server.response.exception.InternalServerError;
import server.response.exception.NotFound;

import java.io.IOException;
import java.net.Socket;

public class Worker extends Thread implements Runnable {
    private Socket client;

    public Worker (Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        Request request = null;
        Response response = null;
        try {
            request = new Request(client);
            Resource.handleURI(request);
            AccessCheck.check(request);
            response = new Response(request);
        } catch(IOException | InternalServerError | ConfigError e) {
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
                Sender.sendResponse(client, response);
            } catch (IOException | NullPointerException e) {
                System.out.println("sendResponse Error : " + e);
            }
            try {
                client.close();
            } catch (IOException e) {
                System.out.println("client.close Error : " + e);
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
