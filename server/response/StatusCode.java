package server.response;

public class StatusCode {

    private String code;

    public StatusCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return (code + " ");
    }
}
