package server.accessCheck;

import server.accessCheck.exceptions.Forbidden;
import server.accessCheck.exceptions.Unauthorized;
import server.configuration.Configuration;
import server.request.Request;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class AccessCheck {

    private HashMap<String, String> directives;
    private HashMap<String, String> username_password;

    public AccessCheck() throws IOException {
        directives = new HashMap<>();
        username_password = new HashMap<>();
        setDirectives();
        setUsernamePasswords();
    }

    public static void check(Request request) throws Unauthorized, Forbidden {
        if (accessExist(request.getId().getURI())) {
            if (authHeader()) {
                if (validUsernamePassword("user")) {

                } else {
                    throw new Forbidden("user");
                }
            } else {
                throw new Unauthorized(request.getId().getURI());
            }
        }
    }

    private static boolean authHeader() {
        return true;
    }

    private static boolean accessExist(String uri) {
        return false;
    }

    private static boolean validUsernamePassword(String user) {
        return true;
    }

    private void setDirectives() throws IOException {
        String line, key, value;
        BufferedReader reader = new BufferedReader(new FileReader(Configuration.getHttpd().getAccessFile()));
        while((line = reader.readLine()) != null) {
            key = line.substring(0, line.indexOf(" ")).trim();
            value = stripQuotes(line.substring(line.indexOf(" ")).trim());
            directives.put(key, value);
        }
    }

    public String getDirective(String key) {
        return directives.get(key);
    }

    private void setUsernamePasswords() throws IOException {
        String line, key, value;
        BufferedReader reader = new BufferedReader(new FileReader(directives.get("AuthUserFile")));
        while((line = reader.readLine()) != null) {
            key = line.substring(0, line.indexOf(":")).trim();
            value = line.substring(line.indexOf(":") + 1).trim();
            username_password.put(key, value);
        }
    }

    private String stripQuotes(String line) {
        if(line.contains("\"")) {
            int x = line.indexOf("\"");
            int y = line.lastIndexOf("\"");
            return line.substring( x+1, y);
        }
        return line;
    }
}
