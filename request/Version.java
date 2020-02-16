package request;

import request.exceptions.InvalidVersionException;
import response.StatusCode;

import java.util.Arrays;
import java.util.List;

public class Version {

    private static List<String> VERSIONS = Arrays.asList(
            "HTTP/1.1"
    );

    private String version;

    public Version(String version) throws InvalidVersionException {
        if(VERSIONS.contains(version)) {
            this.version = version;
        } else {
            Request.code = new StatusCode("400");
//            throw new InvalidVersionException("\"" + version + "\" is not a valid version of HTTP");
        }
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return version + " ";
    }
}
