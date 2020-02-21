package server.response;

public class ResVersion {
    private String version;

    public ResVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return version + " ";
    }
}
