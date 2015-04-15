package webapptest;

import com.google.gson.Gson;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author fritz
 */
public class LogInScreenController {

    @FXML
    private TextField loginName;
    @FXML
    private TextField loginPassword;

    @FXML
    private HBox hb_button_container;

    public static DataPOJO completeInfo = null;

    public void initialize() {
        double columnWidth = 100;
        hb_button_container.getChildren().add(new TextFlow(
                new Text("Forgot Password? "), new Hyperlink("Click here")));
    }

    //Runs the login process
    @FXML
    public void logInPressed(ActionEvent event) throws IOException {

        DataPOJO responsePojo = new DataPOJO();
        Gson gson = new Gson();
        //Test passsword should be: ShiftyTestPassword
        //System.out.println("This was the response: " + WebAppTest.postToServer(Type.SHIFTY, Action.GETMOBILEINFO, ""));
        responsePojo.setUsername(loginName.getText());
        responsePojo.setPasswordHash(WebAppTest.MD5(loginPassword.getText()));
        responsePojo = gson.fromJson(WebAppTest.postToServer(Type.SHIFTY, Action.GETMOBILEINFO, gson.toJson(responsePojo, DataPOJO.class)), DataPOJO.class);
        if (responsePojo.isLastActionSuccess()) {
            completeInfo = responsePojo;

            Stage stageTheLabelBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent webapp = FXMLLoader.load(getClass().getResource("PrimaryWindow.fxml"));
            Scene scene = new Scene(webapp);
            stageTheLabelBelongs.setScene(scene);
            stageTheLabelBelongs.show();
        } else {
            //TODO Display Some errors
            System.out.println("It didn't work.. here's the message: " + responsePojo.getReturnMessage());
        }
    }

    //Verifies if the password and username were correct
    public int passwordVerification() {
        String password = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            password = (Hex.encodeHexString(md.digest(loginPassword.getText().getBytes())));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(System.err);
        }
        DataPOJO checkLogin = new DataPOJO();
        checkLogin.setUsername(loginName.getText());
        checkLogin.setPasswordHash(password);
        Gson gson = new Gson();
        String response = WebAppTest.postToServer(Type.SHIFTY, Action.GETMOBILEINFO, gson.toJson(checkLogin, DataPOJO.class));

        switch (response) {
            case "Verification_Success|Done":
                return 1;
            case "Invalid_Password|Done":
                return 2;
            case "Invalid_Username|Done":
                return 3;
            default:
                System.out.println(response);
                return 4;
        }
    }

}
