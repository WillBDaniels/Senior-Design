package webapptest;

import edu.csci.shiftyencryption.ShiftyCipher;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author fritz
 */
public class WebAppTest extends Application {

    private static String proxyAddr = "";
    private static int proxyPort = 0;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("LogInScreen.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static String postToServer(Type type, Action action, String json) {
        ShiftyCipher ciph = new ShiftyCipher();
        String lineHolder = null;
        int code = 0;
        HttpClient httpclient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
        HttpPost httppost;
        String url = "http://localhost:80";
        //String url = "http://www.google.com";
        httppost = new HttpPost(url);
        ProxyHandler ph = new ProxyHandler();
        Map<String, Integer> proxySettings = ph.getProxySettings();
        HttpHost proxyHost = null;
        if (!proxySettings.isEmpty()) {
            System.out.println("Found some proxy settings..?");
            for (String proxy : proxySettings.keySet()) {
                System.out.println("Found a proxy at: " + proxy);
            }
            proxyAddr = proxySettings.keySet().iterator().next();
            proxyPort = proxySettings.get(proxyAddr);
            proxyHost = new HttpHost(proxyAddr, proxyPort);
            System.out.println("proxyAddr: " + proxyAddr + " proxy port: " + proxyPort);

        } else {
            System.out.println("No proxy detected");
        }
        try {
            RequestConfig requestConfig;
            if (proxyHost != null) {
                requestConfig = RequestConfig.custom()
                        .setSocketTimeout(120000)
                        .setConnectTimeout(120000)
                        .setConnectionRequestTimeout(120000)
                        .setRedirectsEnabled(true)
                        .setProxy(proxyHost)
                        .setRelativeRedirectsAllowed(true)
                        .setCircularRedirectsAllowed(true)
                        .setAuthenticationEnabled(true)
                        .build();
            } else {
                requestConfig = RequestConfig.custom()
                        .setSocketTimeout(120000)
                        .setConnectTimeout(120000)
                        .setConnectionRequestTimeout(120000)
                        .setRedirectsEnabled(true)
                        .setCircularRedirectsAllowed(true)
                        .setRelativeRedirectsAllowed(true)
                        .setAuthenticationEnabled(true)
                        .build();
            }
            httppost.setConfig(requestConfig);

            httppost.setHeader("Host", url);
            httppost.setHeader("Cache-Control", "no-cache");
            //httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            List<NameValuePair> params = new ArrayList<>();

            params.add(new BasicNameValuePair(ciph.encrypt(Com.SECRETKEY.getProtocol()), ciph.encrypt(Com.CONNECTIONSECRET.getProtocol())));
            params.add(new BasicNameValuePair(ciph.encrypt(Com.CHOOSEACTION.getProtocol()), ciph.encrypt(type.getActions().get(action))));
            params.add(new BasicNameValuePair(ciph.encrypt(Com.JSONDATA.getProtocol()), ciph.encrypt(json)));

            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            //Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            code = response.getStatusLine().getStatusCode();
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((lineHolder = rd.readLine()) != null) {
                line += lineHolder;
            }
            //line = rd.readLine();
            System.out.println("THis was the line: " + ciph.decrypt(line));

            if (code < 400) {
                line = ciph.decrypt(line);
            } else {
                return "unable to connect, error code: " + code;
            }
            return line;
        } catch (Throwable t) {
            t.printStackTrace(System.err);
            return "unable to connect";
        }
    }

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

}
