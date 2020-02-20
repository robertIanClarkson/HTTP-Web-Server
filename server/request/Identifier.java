package server.request;

public class Identifier {

    private String uri, originalURI;
    private Query query;
    private boolean hasQuery;

    public Identifier(String uri) {
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

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
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
