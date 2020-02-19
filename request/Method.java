package request;

import request.exceptions.InvalidMethodError;
import response.StatusCode;

import java.util.Arrays;
import java.util.List;

public class Method {

    private static List<String> VERBS = Arrays.asList(
            "GET", "HEAD", "POST", "PUT", "DELETE", "TRACE", "OPTIONS", "CONNECT", "PATCH"
    );
    private String verb;

    public Method(String verb) throws InvalidMethodError {
        if(VERBS.contains(verb)) {
            this.verb = verb;
        } else {
            Request.code = new StatusCode("400");
//            throw new InvalidMethodException("\"" + verb + "\" is not a valid verb");
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
