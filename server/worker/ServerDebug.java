package server.worker;

import server.request.Request;
import server.response.Response;

public class ServerDebug {
    static void printRequest(Request request) {
        System.out.println("------------Request-------------");
        System.out.println(request);
        if(Request.hasBody) {
            System.out.println("***+++HAS BODY+++***");
//            String body = new String(server.request.getBody().getBody(), StandardCharsets.US_ASCII);
//            System.out.println(body);
        }
    }

    static void printStatusCode(Request request, Response response) {
        System.out.println("------------Code-------------");
        System.out.printf("%-3s---> %s\n", response.getCode(), request.getId());
    }

    static void printResponse(Response response) {
        System.out.println("------------Response------------");
        System.out.println(response);
        if(response.hasBody()) {
//            System.out.println("***+++HAS BODY+++***");
//            String body = new String(server.response.getBody().getBody(), StandardCharsets.US_ASCII);
//            System.out.println(body);
        }
    }
}
