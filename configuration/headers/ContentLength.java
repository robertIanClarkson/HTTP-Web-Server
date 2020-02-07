package configuration.headers;

public class ContentLength extends Header {
    private int length;
    public void init(String data) {
        length = Integer.parseInt(data);
    }

    public int getLength() {
        return length;
    }


}
