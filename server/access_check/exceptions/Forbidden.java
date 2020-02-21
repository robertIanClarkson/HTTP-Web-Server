package server.access_check.exceptions;

public class Forbidden extends Exception {
    public Forbidden(String message) {
        super(message);
    }
}
