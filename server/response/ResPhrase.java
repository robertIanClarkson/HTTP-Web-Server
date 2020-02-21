package server.response;

public class ResPhrase {

    private String phrase;

    public ResPhrase(String phrase) {
        this.phrase = phrase;
    }

    @Override
    public String toString() {
        return (phrase + "\r\n");
    }
}
