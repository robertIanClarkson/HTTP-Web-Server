package server.request;

public class Identifier {

    private String uri;
    private Query query;
    private boolean hasQuery;

    public Identifier(String uri) {
        hasQuery = false;
        this.uri = uri;
    }

    public String getURI() {
        return uri;
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
