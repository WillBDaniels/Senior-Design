package edu.csci.standalone_server;

import com.sun.net.httpserver.HttpExchange;
import edu.csci.shiftyencryption.ShiftyCipher;
import edu.csci.standalone_server.enums.Data;
import edu.csci.standalone_server.enums.State;
import edu.csci.standalone_server.jsonhandler.JSONHandler;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * This class handles all of the interaction between itself and the various
 * programs that might query our server. Later this will be optimized to remove
 * some of the logic out of this class and into another one for easier re-use.
 */
public class WorkerRunnable implements Runnable {

    protected HttpExchange clientSocket = null;
    private String dataToSend = "";

    public WorkerRunnable(HttpExchange clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     *
     * This is the standard 'run()' method of the Runnable interface, and the
     * primary event loop for this thread. Once this method finished, the client
     * connection is dismissed.
     */
    @Override
    public void run() {
        boolean foundMatch = false;
        Map<String, Object> params = (Map<String, Object>) clientSocket.getAttribute("parameters");
        System.out.println("I'm here, getting params..");
        if (params == null) {
            System.out.println("Params was null, returning");
            showUnknownMessage();
            clientSocket.close();
            return;
        }
        if ((String) params.get("secretkey") != null && ((String) params.get("secretkey")).equals(Data.CONNECTIONSECRET.getData())) {
            System.out.println("Got the secretKey correctly");
            String json = (String) params.get("data");
            for (State item : State.values()) {
                if (item.getCheckString().equals(params.get("action"))) {
                    foundMatch = true;
                    try {
                        System.out.println("This is the json: " + json);
                        JSONHandler handler = item.getHandler().getDeclaredConstructor(String.class, HttpExchange.class).newInstance(json, clientSocket);
                        sendJSONResponse(handler.getJSONResponse());
                    } catch (NoSuchMethodException | SecurityException ex) {
                        ex.printStackTrace(System.err);
                    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        ex.printStackTrace(System.err);
                    }
                }
            }
            if (!foundMatch) {
                showUnknownMessage();
            }

        } else {
            showUnknownMessage();
        }
        clientSocket.close();
    }

    /**
     * This method simply sends a basic JSON response which is encrypted. Only
     * difference is that this method is always expecting to send json
     *
     * @param json the (unencrypted) JSON string to be sent back to the client.
     */
    private void sendJSONResponse(String json) {
        ShiftyCipher dc = new ShiftyCipher();
        try {
            try (OutputStream output = new DataOutputStream(clientSocket.getResponseBody())) {
                json = dc.encrypt(json);
                clientSocket.sendResponseHeaders(200, json.length());
                output.write(json.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     * This is the message that gets sent to the client if they send something
     * that we don't recognize. This one is pretty self-explanatory.
     *
     */
    private void showUnknownMessage() {
        try {
            try (OutputStream output = new DataOutputStream(clientSocket.getResponseBody())) {
                dataToSend = "Welcome to Team Shifty's Senior Design Server! This web end-point isn't really "
                        + "any use to browsing, so all there is is this message, sorry!";
                clientSocket.sendResponseHeaders(200, dataToSend.length());
                output.write(dataToSend.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}
