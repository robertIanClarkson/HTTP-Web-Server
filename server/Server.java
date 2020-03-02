package server;

import server.access_check.AccessCheck;
import server.configuration.ConfigError;
import server.configuration.Configuration;
import server.logs.Log;
import server.resource.Resource;
import server.worker.Worker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket socket;
    private Socket client;

    public Server() throws IOException, ConfigError {
        new Configuration("conf/httpd.conf", "conf/mime.types");
        socket = new ServerSocket( Configuration.getHttpd().getListen() );
    }

    public void start() {
        while( true ) {
            try {
                client = socket.accept();
                Worker worker = new Worker(client);
                worker.start();
//                System.out.println("Worker Name : " + worker.getName());
            } catch (IOException e) {
                System.out.println("Error: Server.start --> " + e);
            }
        }
    }
}
