package server.access_check;

import server.access_check.exceptions.Forbidden;
import server.access_check.exceptions.Unauthorized;
import server.configuration.Configuration;
import server.request.Request;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Base64;
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

    public void check(Request request) throws Unauthorized, Forbidden {
        if(accessExist(request)) {
            if(hasAuthHeader(request)) {
                if(validUsernamePassword(request)) {
                    System.out.println("******ACCESS-GRANTED******");
                } else {
                    throw new Forbidden("Invalid user/pass");
                }
            } else {
                throw new Unauthorized("New Authorization Request");
            }
        }
    }

    private boolean hasAuthHeader(Request request) {
        return request.getRequestHeaders().hasHeader("Authorization");
    }

    private boolean accessExist(Request request) {
        String mockAccessFile = request.getId().getURI();
        mockAccessFile = mockAccessFile.substring(0, mockAccessFile.lastIndexOf("/"));
        mockAccessFile += "/.htaccess"; // += / + Configuration.accessFile
        return Files.exists(Paths.get(mockAccessFile));
    }

    private boolean validUsernamePassword(Request request) throws Forbidden {
        String[] requestTokens = getRequestSHA(request);
        if(username_password.containsKey(requestTokens[0])) {
            String password = username_password.get(requestTokens[0]);
            return requestTokens[1].equals(password);
        }
        return false;
    }
    private String[] getRequestSHA(Request request) throws Forbidden {
        String authInfo = request.getRequestHeaders().getHeader("Authorization");
        authInfo = authInfo.substring(authInfo.indexOf(":") + 7).trim();
        String credentials = new String(
                Base64.getDecoder().decode( authInfo ),
                Charset.forName( "UTF-8" )
        );
        String[] tokens = credentials.split( ":" );
        if(tokens.length == 2) {
            tokens[1] = encryptClearPassword(tokens[1]);
        } else {
            throw new Forbidden("Missing credential");
        }
        return tokens;
    }

    private String encryptClearPassword( String password ) {
        // Encrypt the cleartext password (that was decoded from the Base64 String
        // provided by the client) using the SHA-1 encryption algorithm
        try {
            MessageDigest mDigest = MessageDigest.getInstance( "SHA-1" );
            byte[] result = mDigest.digest( password.getBytes() );

            return Base64.getEncoder().encodeToString( result );
        } catch( Exception e ) {
            return "";
        }
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
            value = line.substring(line.indexOf(":") + 6).trim();
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
