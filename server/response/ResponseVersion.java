package server.response;

public class ResponseVersion {
    private String version;

    public ResponseVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return version + " ";
    }
}
