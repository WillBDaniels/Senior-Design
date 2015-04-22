package webapptest;

import edu.csci.shiftyencryption.ShiftyCipher;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author fritz
 */
public class WebAppTest extends Application {

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
        String line = "";

        try {
            ShiftyCipher ciph = new ShiftyCipher();
            String urlParameters = Com.SECRETKEY.getProtocol() + "=" + Com.CONNECTIONSECRET.getProtocol();
            urlParameters += "&" + Com.CHOOSEACTION.getProtocol() + "=" + type.getActions().get(action);
            urlParameters += "&" + Com.JSONDATA.getProtocol() + "=" + json;
            urlParameters = ciph.encrypt(urlParameters);
            //urlParameters = "";
            byte[] postData = urlParameters.getBytes(Charset.forName("UTF-8"));
            int postDataLength = postData.length;
            String request = "http://timeslip.ddrcweb.com/licenseServer";
            URL url = new URL(request);
            HttpURLConnection cox = (HttpURLConnection) url.openConnection();
            cox.setDoOutput(true);
            cox.setDoInput(true);
            cox.setInstanceFollowRedirects(false);
            cox.setRequestMethod("POST");
            cox.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            cox.setRequestProperty("charset", "utf-8");
            cox.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            cox.setUseCaches(false);
            try (DataOutputStream wr = new DataOutputStream(cox.getOutputStream())) {
                wr.write(postData);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(cox.getInputStream()))) {
                String tempLine;
                while ((tempLine = reader.readLine()) != null) {
                    line += tempLine;
                }

                line = ciph.decrypt(line);
            } catch (Throwable t) {
                t.printStackTrace(System.err);
                return "unable to connect";
            }
        } catch (Throwable t) {

            t.printStackTrace(System.err);
            return "unable to connect";
        }
        return line;
    }

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static void setCSS(boolean isInvalid, Node item) {
        if (isInvalid) {
            item.getStyleClass().removeAll("good", "bad");
            item.getStyleClass().add("bad");
        } else {
            item.getStyleClass().removeAll("good", "bad");
            item.getStyleClass().add("good");
        }
    }

}
