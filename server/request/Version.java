package server.request;

import server.response.StatusCode;

import java.util.Arrays;
import java.util.List;

public class Version {

    private static List<String> VERSIONS = Arrays.asList(
            "HTTP/1.1"
    );

    private String version;

    public Version(String version) {
        if(VERSIONS.contains(version)) {
            this.version = version;
        } else {
            Request.code = new StatusCode("400");
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
