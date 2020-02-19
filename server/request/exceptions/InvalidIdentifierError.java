package server.request.exceptions;

public class InvalidIdentifierError extends RequestError {
    public InvalidIdentifierError(String message) {
        super(message);
    }
}
