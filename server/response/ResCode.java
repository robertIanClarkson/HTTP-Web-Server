package server.response;

public class ResCode {

    private String code;

    public ResCode(String code) {
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
