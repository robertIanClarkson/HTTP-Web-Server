package server.request;

public class ReqIdentifier {

    private String uri, originalURI;
    private ReqQuery query;
    private boolean hasQuery;

    public ReqIdentifier(String uri) {
        originalURI = uri;
        hasQuery = false;
        if(uri.isEmpty()) {
            this.uri = "/";
        } else {
            this.uri = uri;
        }
    }

    public String getURI() {
        return this.uri;
    }

    public void setURI(String uri) {
        this.uri = uri;
    }

    public ReqQuery getQuery() {
        return query;
    }

    public void setQuery(ReqQuery query) {
        this.query = query;
        hasQuery = true;
    }

    public String getOriginalURI() {
        return originalURI;
    }

    @Override
    public String toString() {
        String id = "";
        id += this.uri;
        if(hasQuery) {
            id += this.query;
        }
        id += " ";
        return id;
    }
}
