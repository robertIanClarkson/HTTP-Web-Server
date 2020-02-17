package request;

public class Query {

    private String query;

    public Query(String query) {
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
