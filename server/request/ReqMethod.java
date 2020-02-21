package server.request;

import server.request.exceptions.BadRequest;

import java.util.Arrays;
import java.util.List;

public class ReqMethod {

    private static List<String> VERBS = Arrays.asList(
            "GET", "HEAD", "POST", "PUT", "DELETE", "TRACE", "OPTIONS", "CONNECT", "PATCH"
    );
    private String verb;

    public ReqMethod(String verb) throws BadRequest {
        if(VERBS.contains(verb)) {
            this.verb = verb;
        } else {
            throw new BadRequest("Invalid Verb: " + verb);
        }
    }

    public String getVerb() {
        return verb;
    }

    @Override
    public String toString() {
        String method = "";
        method += verb;
        method += " ";
        return method;
    }
}
