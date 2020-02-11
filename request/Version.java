package request;

public class Version {

    private String version;

    public Version(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return version + " ";
    }
}
