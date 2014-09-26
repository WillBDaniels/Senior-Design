package edu.csci.standalone_server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * 09-2014 
 * @author William
 */
public class MultiThreadedHttpServer {

    protected int          serverPort   = 80;
    protected HttpServer serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;

    protected ExecutorService threadPool =
            Executors.newFixedThreadPool(80);
    public static void main(String[] args){
	   MultiThreadedHttpServer test = new MultiThreadedHttpServer(80);

    }

    public MultiThreadedHttpServer(int port){
        this.serverPort = port;
        openServerSocket();
    }


    private void openServerSocket() {
        try {
            this.serverSocket = HttpServer.create(new InetSocketAddress(this.serverPort), 0);
            serverSocket.setExecutor(threadPool);
            serverSocket.createContext("/", new MyHandler());
            serverSocket.start();
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

    
    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange he) throws IOException {
            (new Thread((new WorkerRunnable(he)))).start();
        }
        
    }
}
