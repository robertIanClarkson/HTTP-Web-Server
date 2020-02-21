package server.request;

public class ReqQuery {

    private String query;

    public ReqQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    @Override
    public String toString() {
        String query = "";
        query += "?";
        query += this.query;
        return query;
    }
}
