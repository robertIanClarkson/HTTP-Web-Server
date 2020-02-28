package server.worker;

import server.response.Response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Sender {
    public void sendResponse(Socket client, Response response) throws IOException {
        /* https://stackoverflow.com/questions/1176135/socket-send-and-receive-byte-array */
        /* https://mkyong.com/java/how-to-send-http-request-getpost-in-java/ */
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        byte[] res = response.toString().getBytes();
        out.write(res);
        if(Response.hasBody) {
            out.write(response.getResponseBody().getBody());
        }
        out.flush();
    }
}
