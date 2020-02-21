package server.request;

import server.request.exceptions.BadRequest;

import java.util.Arrays;
import java.util.List;

public class RequestVersion {

    private static List<String> VERSIONS = Arrays.asList(
            "HTTP/1.1"
    );

    private String version;

    public RequestVersion(String version) throws BadRequest {
        if(VERSIONS.contains(version)) {
            this.version = version;
        } else {
            throw new BadRequest("Invalid HTTP Version: " + version);
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
