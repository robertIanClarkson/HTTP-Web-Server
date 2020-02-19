package server.request.exceptions;

public class InvalidVersionError extends RequestError {
    public InvalidVersionError(String message) {
        super(message);
    }
}
