package server.accessCheck;

import server.request.Request;

public class AccessCheck {

    public AccessCheck() {}

    public static void check(Request request) {
        if (accessExist(request.getId().getURI())) {

        }
    }

    private static boolean accessExist(String uri) {
        return false;
    }
}
