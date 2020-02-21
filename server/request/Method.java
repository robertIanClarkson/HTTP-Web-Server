package server.request;

import server.request.exceptions.BadRequest;

import java.util.Arrays;
import java.util.List;

public class Method {

    private static List<String> VERBS = Arrays.asList(
            "GET", "HEAD", "POST", "PUT", "DELETE", "TRACE", "OPTIONS", "CONNECT", "PATCH"
    );
    private String verb;

    public Method(String verb) throws BadRequest {
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
