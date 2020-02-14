package request;

import configuration.ConfigError;
import configuration.Configuration;
import request.exceptions.InvalidIdentifierException;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Identifier {

    private String id;

    public Identifier(String id) throws InvalidIdentifierException, ConfigError {
//        id = resolveAlias(id);
        if(Configuration.getHttpd().isAlias(id)) {
            this.id = Configuration.getHttpd().getAlias(id);
        } else {
            this.id = Configuration.getHttpd().getDocumentRoot() + id.substring(1);
        }

        if(this.id.endsWith("/")) {
            /* if no file is given then give index.html */
            this.id += Configuration.getHttpd().getDirectoryIndex();
        }

        if(Files.notExists(Paths.get(this.id))){
            throw new InvalidIdentifierException("File \"" + id + "\" does not exist");
        }
    }

    public String getId() {
        return id;
    }
}
