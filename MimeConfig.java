import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class MimeConfig {

    private HashMap<String, String> mimeTypes;

    MimeConfig(String fileName) {
        BufferedReader reader;
        String line;
        String type;
        String[] extensions;
        mimeTypes = new HashMap<>();
        try {
            reader = new BufferedReader(new FileReader(fileName));
            while ((line = reader.readLine()) != null) {
                if ((!line.contains("#")) && (line.length() != 0) && (line.contains("\t"))) {
                    type = getType(line);
                    extensions = getExtensions(line);
                    for (String extension : extensions) {
                        mimeTypes.put(extension, type);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("MimeConfig --> IOException: " + e);
        }
    }

    public HashMap<String, String> getMimeTypes() {
        return mimeTypes;
    }

    private String getType(String line) {
        return line.substring(0, line.indexOf("\t"));
    }

    private String[] getExtensions(String line) {
        String right = line.substring(line.lastIndexOf("\t") + 1);
        return right.split(" ");
    }
 }
