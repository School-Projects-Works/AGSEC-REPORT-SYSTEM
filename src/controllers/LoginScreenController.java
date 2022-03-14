package controllers;

import com.jfoenix.controls.JFXButton;
import global_classes.GlobalFuncions;
import global_classes.MongoDbAdmin;
import global_classes.MongodbServices;
import java.io.IOException;
import java.net.URL;
import java.time.Year;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class LoginScreenController implements Initializable {

    @FXML
    private TextField tf_username;
    @FXML
    private PasswordField tf_userPassword;
    @FXML
    private BorderPane main_pane;
    @FXML
    private AnchorPane left_pane;
    @FXML
    private AnchorPane right_pane;
    @FXML
    private JFXButton btn_login;
    @FXML
    private ImageView btn_close;

    /**
     * Initializes the controller class.
     */
    double[] xOffset = {0}, yOffset = {0};

    @FXML
    private Text txt_error;

    MongoDbAdmin MA = new MongoDbAdmin();
    GlobalFuncions gf = new GlobalFuncions();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txt_error.setText("");
    }

    @FXML
    private void logUserIn(ActionEvent event) throws IOException {

        if (!tf_username.getText().isEmpty() && !tf_userPassword.getText().isEmpty()) {
            if (MA.loginAdmin(tf_username.getText(), tf_userPassword.getText())) {
                if (!"admin".equals(tf_userPassword.getText())) {
                    txt_error.setText("");
                    Screen screen = Screen.getPrimary();
                    Rectangle2D bounds = screen.getVisualBounds();
                    Parent root;
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    if (!MA.getYears().isEmpty() && MA.getYears().contains(Year.now().toString())) {
                        root = FXMLLoader.load(getClass().getResource("/screens/home_page.fxml"));
                        stage.setX(bounds.getMinX());
                        stage.setY(bounds.getMinY());
                        stage.setWidth(bounds.getWidth());
                        stage.setHeight(bounds.getHeight());
                        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
                    } else {
                        root = FXMLLoader.load(getClass().getResource("/screens/Setup_year.fxml"));
                    }

                    Scene scene = new Scene(root);
                    stage.setScene(scene);

                } else {
                    Parent root = FXMLLoader.load(getClass().getResource("/screens/NewPassword.fxml"));
                    gf.openNewWindow(root);

                }
            } else {
                txt_error.setText("Incorrect UserName or Password");
            }
        } else {
            txt_error.setText("Fields Can not be Empty");
        }
//        Screen screen = Screen.getPrimary();
//        Rectangle2D bounds = screen.getVisualBounds();
//        Parent root = FXMLLoader.load(getClass().getResource("/screens/home_page.fxml"));
//        Scene scene = new Scene(root);
//        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        stage.setScene(scene);
//        stage.setX(bounds.getMinX());
//        stage.setY(bounds.getMinY());
//        stage.setWidth(bounds.getWidth());
//        stage.setHeight(bounds.getHeight());
//        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
//        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
//        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    @FXML
    private void closeWindow(MouseEvent event) {

        gf.closeWindow(event);

    }

    @FXML
    private void handleOnMousePress(MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        if (stage != null) {
            xOffset[0] = stage.getX() - event.getScreenX();
            yOffset[0] = stage.getY() - event.getScreenY();
        }
    }

    @FXML
    private void handleMouseDrag(MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        if (stage != null) {
            stage.setX(event.getScreenX() + xOffset[0]);
            stage.setY(event.getScreenY() + yOffset[0]);
        }
    }

    @FXML
    private void handleKeyPresses(KeyEvent event) throws IOException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            if (!tf_username.getText().isEmpty() && !tf_userPassword.getText().isEmpty()) {
                if (MA.loginAdmin(tf_username.getText(), tf_userPassword.getText())) {
                    if (!"admin".equals(tf_userPassword.getText())) {
                        txt_error.setText("");
                        Screen screen = Screen.getPrimary();
                        Rectangle2D bounds = screen.getVisualBounds();
                        Parent root = FXMLLoader.load(getClass().getResource("/screens/home_page.fxml"));
                        Scene scene = new Scene(root);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(scene);
                        stage.setX(bounds.getMinX());
                        stage.setY(bounds.getMinY());
                        stage.setWidth(bounds.getWidth());
                        stage.setHeight(bounds.getHeight());
                        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
                    } else {

                        Parent root = FXMLLoader.load(getClass().getResource("/screens/NewPassword.fxml"));
                        gf.openNewWindow(root);

                    }
                } else {
                    txt_error.setText("Incorrect UserName or Password");
                }
            } else {
                txt_error.setText("Fields Can not be Empty");
            }
        }
    }

}
