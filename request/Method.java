package request;

import request.exceptions.InvalidMethodException;

import java.util.Arrays;
import java.util.List;

public class Method {

    private static List<String> VERBS = Arrays.asList(
            "GET", "HEAD", "POST", "PUT", "DELETE", "TRACE", "OPTIONS", "CONNECT", "PATCH"
    );
    private String verb;

    public Method(String verb) throws InvalidMethodException{
        if(VERBS.contains(verb)) {
            this.verb = verb;
        } else {
            throw new InvalidMethodException("\"" + verb + "\" is not a valid verb");
        }
    }

    public String getVerb() {
        return verb;
    }
}
