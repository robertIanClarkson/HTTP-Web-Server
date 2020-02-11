package request;

import configuration.Configuration;
import request.exceptions.InvalidIdentifierException;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Identifier {

    private String id;

    public Identifier(String id) throws InvalidIdentifierException {
        this.id = Configuration.getHttpd().getDocumentRoot() + id;
        if(Files.notExists(Paths.get(this.id))){
            throw new InvalidIdentifierException("File \"" + id + "\" does not exist");
        }
    }

    public String getId() {
        return id;
    }
}
