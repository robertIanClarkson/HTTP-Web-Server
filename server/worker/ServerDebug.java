package server.worker;

import server.request.Request;
import server.response.Response;

public class ServerDebug {
    static void printRequest(Request request) {
        System.out.println("------------Request-------------");
        System.out.println(request);
    }

    static void printStatusCode(Request request, Response response) {
        System.out.println("------------Code-------------");
        System.out.printf("%-3s---> %s\n", response.getCode(), request.getId());
    }

    static void printResponse(Response response) {
        System.out.println("------------Response------------");
        System.out.println(response);
    }
}
