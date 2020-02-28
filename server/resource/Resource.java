package server.resource;

import server.configuration.ConfigError;
import server.configuration.Configuration;
import server.request.ReqQuery;
import server.request.Request;

public class Resource {

    public Resource() { }

    public void handleURI(Request request) throws ConfigError {
        ReqQuery query;
        String uri;
        try {
            uri = request.getId().getURI();
        } catch (NullPointerException e) {
            System.out.println(e);
            return;
        }
        if(uri.contains("?")) {
            query = new ReqQuery( uri.substring(uri.lastIndexOf("?") + 1) );
            uri = uri.substring(0, uri.indexOf("?"));
            request.getId().setQuery(query);

        }

        if(Configuration.getHttpd().isAlias(uri)) {
            uri = Configuration.getHttpd().getAlias(uri);
        } else if(Configuration.getHttpd().isScriptAlias(uri)) {
            uri = Configuration.getHttpd().getScriptAlias(uri);
            Request.hasScriptAlias = true;
        } else {
            uri = Configuration.getHttpd().getDocumentRoot() + uri.substring(1);
        }

        if(uri.endsWith("/")) {
            uri += Configuration.getHttpd().getDirectoryIndex();
        }

        request.getId().setURI(uri);
    }
}
