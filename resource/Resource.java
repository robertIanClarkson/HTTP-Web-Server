package resource;

import server.configuration.ConfigError;
import server.configuration.Configuration;
import server.request.Query;
import server.request.Request;
import response.StatusCode;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Resource {

    public Resource() {

    }

    public static void handleURI(Request request) throws ConfigError {
        Query query;
        String uri = request.getId().getURI();
        if(uri.contains("?")) {
            query = new Query( uri.substring(uri.lastIndexOf("?") + 1) );
            uri = uri.substring(0, uri.indexOf("?"));
            request.getId().setQuery(query);

        }
        if(Configuration.getHttpd().isAlias(uri)) {
            uri = Configuration.getHttpd().getAlias(uri);
        } else {
            uri = Configuration.getHttpd().getDocumentRoot() + uri.substring(1);
        }
        if(uri.endsWith("/")) {
            uri += Configuration.getHttpd().getDirectoryIndex();
        }
        if(Files.notExists(Paths.get(uri))){
            Request.code = new StatusCode("404");
        }
        request.getId().setURI(uri);
    }
}
