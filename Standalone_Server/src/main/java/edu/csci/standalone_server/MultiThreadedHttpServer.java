package edu.csci.standalone_server;

import com.sun.net.httpserver.HttpContext;
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
 *
 * @author William
 */
public class MultiThreadedHttpServer {

    protected static int serverPort = 5555;
    protected HttpServer serverSocket = null;
    protected boolean isStopped = false;
    protected Thread runningThread = null;

    protected ExecutorService threadPool
            = Executors.newFixedThreadPool(80);

    public static void main(String[] args) {
        MultiThreadedHttpServer test = new MultiThreadedHttpServer(serverPort);

    }

    public MultiThreadedHttpServer(int port) {
        MultiThreadedHttpServer.serverPort = port;
        openServerSocket();
    }

    private void openServerSocket() {
        try {
            this.serverSocket = HttpServer.create(new InetSocketAddress(MultiThreadedHttpServer.serverPort), 0);
            serverSocket.setExecutor(threadPool);
            HttpContext context = serverSocket.createContext("/", new MyHandler());
            context.getFilters().add(new ParameterFilter());
            serverSocket.start();
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

    static class MyHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange he) throws IOException {
            System.out.println("Got a connection..." + he.getRequestMethod() + " and the headers: " + he.getRequestHeaders());
            (new Thread((new WorkerRunnable(he)))).start();
        }

    }
}
