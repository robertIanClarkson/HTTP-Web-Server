package server.response;

public class Phrase {

    private String phrase;

    public Phrase(String phrase) {
        this.phrase = phrase;
    }

    @Override
    public String toString() {
        return (phrase + "\r\n");
    }
}
