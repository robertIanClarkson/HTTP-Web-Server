package server.access_check.exceptions;

public class Unauthorized extends Exception {
    public Unauthorized(String message) {
        super(message);
    }
}
