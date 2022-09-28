/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import global_classes.GlobalFuncions;
import global_classes.MongoDbAdmin;
import global_classes.MongodbServices;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Year;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;
import pojo_classes.Config;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class SettingsPageController implements Initializable {

    @FXML
    private ComboBox<String> cb_year;
    @FXML
    private ComboBox<String> cb_term;
    @FXML
    private Text txt_error;
    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_userPassword;
    @FXML
    private TextField tf_schoolName;
    @FXML
    private TextField tf_schoolAddress;
    @FXML
    private TextField tf_schoolPhone;
    @FXML
    private ListView<String> tbv_subject;
    @FXML
    private TextField tf_subject;
    @FXML
    private Button btn_add;
    @FXML
    private Button btn_save;
    @FXML
    private ComboBox<String> cb_question;
    @FXML
    private TextField tf_answer;

    /**
     * Initializes the controller class.
     */
    MongoDbAdmin MA = new MongoDbAdmin();
    MongodbServices MS = new MongodbServices();
    GlobalFuncions GF = new GlobalFuncions();
    Config config = new Config();
    FileChooser fileChooser = new FileChooser();
    String location = "";
    double[] xOffset = {0}, yOffset = {0};
    final ContextMenu contextMenu = new ContextMenu();
    MenuItem delete = new MenuItem("Remove");
    @FXML
    private ImageView img_logo;
    @FXML
    private ImageView img_sign;
    @FXML
    private Button btn_clse;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

       
        ArrayList<String> years = new ArrayList();
        Year year = Year.now();
        years.add(year.toString());
        for (int i = 1; i < 11; i++) {
            years.add(year.plusYears(i).toString());
        }
        cb_year.getItems().addAll(years);
        cb_term.getItems().addAll("Term One", "Term Two", "Term Three");
        contextMenu.getItems().add(delete);
        tbv_subject.setContextMenu(contextMenu);
        delete.setOnAction((ActionEvent event) -> {
            if (!tbv_subject.getSelectionModel().isEmpty()) {
                tbv_subject.getItems().remove(tbv_subject.getSelectionModel().getSelectedIndex());
            }

        });

        cb_question.getItems().addAll("What is your date of birth?", "What’s your favorite movie?", "What was your first car?");

        loadConfig();
    }

    private void loadConfig() {
        config = MA.getConfig();
        
        if (config.getId() != null) {
            cb_year.setValue(config.getYear());
            cb_term.setValue(config.getTerm());
            tf_username.setText(config.getAdminName());
            tf_userPassword.setText(config.getAdminPassword());
            tf_schoolName.setText(config.getSchoolName());
            tf_schoolAddress.setText(config.getSchoolAddress());
            tf_schoolPhone.setText(config.getSchoolPhone());
            cb_question.setValue(config.getSecretQuestion());
            tf_answer.setText(config.getSecretAnswer());
            tbv_subject.getItems().addAll(config.getSubjects());
            if (config.getSchoolLogo() != null && !config.getSchoolLogo().isEmpty()) {
                if (new File("Config/schoolLogo.png").exists()) {
                    Image img = new Image(config.getSchoolLogo());
                    img_logo.setImage(img);
                }
            }
            if (config.getSignature() != null && !config.getSignature().isEmpty()) {
                if (new File("Config/headMasterSign.png").exists()) {
                    Image img = new Image(config.getSignature());
                    img_sign.setImage(img);
                }
            }
        }
    }

    @FXML
    private void handleLogoUpload(MouseEvent event) {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png"),
                new FileChooser.ExtensionFilter("Image Files", "*.jpg"),
                new FileChooser.ExtensionFilter("Image Files", "*.jpeg")
        );
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile.exists()) {
            try {
                BufferedImage buffer = ImageIO.read(selectedFile);
                File outputfile = new File("Config/schoolLogo.png");
                ImageIO.write(buffer, "png", outputfile);
                Image img = new Image(outputfile.toURI().toString());
                img_logo.setImage(img);
                config.setSchoolLogo(outputfile.toURI().toString());

            } catch (IOException ex) {
                Logger.getLogger(SettingsPageController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @FXML
    private void handleSignatureUpload(MouseEvent event) {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png"),
                new FileChooser.ExtensionFilter("Image Files", "*.jpg"),
                new FileChooser.ExtensionFilter("Image Files", "*.jpeg")
        );
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile.exists()) {
            try {
                BufferedImage buffer = ImageIO.read(selectedFile);
                File outputfile = new File("Config/headMasterSign.png");
                ImageIO.write(buffer, "png", outputfile);
                Image img = new Image(outputfile.toURI().toString());
                img_sign.setImage(img);
                config.setSignature(outputfile.toURI().toString());

            } catch (IOException ex) {
                Logger.getLogger(SettingsPageController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @FXML
    private void onSaveConfig(ActionEvent event) {
        Stage stage = (Stage) btn_save.getScene().getWindow();
        if (cb_year.getValue() != null) {

            config.setYear(cb_year.getValue());
            if (cb_term.getValue() != null) {
                config.setTerm(cb_term.getValue());
                config.setYearTermPair(String.valueOf((cb_term.getValue() + cb_term.getValue()).hashCode()));
                if (!tf_username.getText().trim().isEmpty()) {
                    config.setAdminId(String.valueOf(tf_username.getText().hashCode()));
                    config.setAdminName(tf_username.getText());
                    if (!tf_userPassword.getText().trim().isEmpty()&&tf_userPassword.getText().length()>5) {
                        config.setAdminPassword(tf_userPassword.getText());
                        if (cb_question.getValue() != null) {
                            config.setSecretQuestion(cb_question.getValue());
                            if (!tf_answer.getText().trim().isEmpty()) {
                                config.setSecretAnswer(tf_answer.getText());
                                if (!tf_schoolName.getText().trim().isEmpty()) {
                                    config.setSchoolName(tf_schoolName.getText().toUpperCase());
                                    if (!tf_schoolAddress.getText().trim().isEmpty()) {
                                        config.setSchoolAddress(GF.toTitleCase(tf_schoolAddress.getText()));
                                        if (!tf_schoolPhone.getText().trim().isEmpty()) {
                                            config.setSchoolPhone(tf_schoolPhone.getText());

                                            if (!tbv_subject.getItems().isEmpty()) {
                                                config.setSubjects(tbv_subject.getItems());
                                                ButtonType submit = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
                                                ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to save Configurations", submit, cancel);
                                                alert.initStyle(StageStyle.UNDECORATED);
                                                alert.setHeaderText(null);
                                                alert.getDialogPane().getStylesheets().add("/styles/dialog.css");
                                                Optional<ButtonType> result = alert.showAndWait();
                                                if (result.orElse(cancel) == submit) {
                                                    alert.close();
                                                    if (MA.saveConfig(config)) {
                                                        GF.inforAlert("Success", "Configuration Saved Successfully, Login and go to settings to edit", AlertType.INFORMATION);
                                                        loadConfig();
                                                        if (!this.location.isEmpty()) {
                                                            _openDashBoard();
                                                        }
                                                    } else {
                                                        GF.inforAlert("Error", "Error Saving Configuration", AlertType.INFORMATION);
                                                    }
                                                }
//                                           
                                            } else {
                                                GF.inforAlert("Error", "At lease One subject Should be added", AlertType.INFORMATION);
                                            }
                                        } else {
                                            GF.inforAlert("Error", "Please Enter School Phone Number", AlertType.INFORMATION);
                                        }
                                    } else {
                                        GF.inforAlert("Error", "Please Enter School Address (Postal/location)", AlertType.INFORMATION);
                                    }
                                } else {
                                    GF.inforAlert("Error", "Please Enter School Name", AlertType.INFORMATION);
                                }
                            } else {
                                GF.inforAlert("Error", "Please Enter Secret Answer", AlertType.INFORMATION);
                            }
                        } else {
                            GF.inforAlert("Error", "Please Select Secret Question", AlertType.INFORMATION);
                        }
                    } else {
                        GF.inforAlert("Error", "Admin Password co not be less than 6", AlertType.INFORMATION);
                    }
                } else {
                    GF.inforAlert("Error", "Please Enter Admin Username", AlertType.INFORMATION);
                }
            } else {
                GF.inforAlert("Error", "Please Select current Term", AlertType.INFORMATION);
            }

        } else {

            GF.inforAlert("Value Error", "Please Select Year", AlertType.INFORMATION);
        }

    }

    @FXML
    private void onAddSubject(ActionEvent event
    ) {
        if (!tf_subject.getText().trim().isEmpty()) {
            boolean exist = false;
            for (String subject : tbv_subject.getItems()) {
                if (subject.equals(GF.toTitleCase(tf_subject.getText()))) {
                    exist = true;
                }
            }
            if (exist) {
                txt_error.setText("Subject Already Added");
            } else {
                txt_error.setText("");
                tbv_subject.getItems().add(GF.toTitleCase(tf_subject.getText()));
                tf_subject.setText("");
            }

        }
    }

    @FXML
    private void handleAddSubjectKeyPressed(KeyEvent event
    ) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            if (!tf_subject.getText().trim().isEmpty()) {
                boolean exist = false;
                for (String subject : tbv_subject.getItems()) {
                    if (subject.equals(GF.toTitleCase(tf_subject.getText()))) {
                        exist = true;
                    }
                }
                if (exist) {
                    txt_error.setText("Subject Already Added");
                } else {
                    txt_error.setText("");
                    tbv_subject.getItems().add(GF.toTitleCase(tf_subject.getText()));
                    tf_subject.setText("");
                }

            }
        }
    }

    void setLocation(String new_Stage) {
        this.location = new_Stage;
         if (this.location!=null&&!this.location.isEmpty()) {
            btn_clse.setVisible(true);
        }
    }

    private void _openDashBoard() {
        Stage oldStage = (Stage) img_logo.getScene().getWindow();
        oldStage.hide();
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/screens/LoginScreen.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);

            stage.setResizable(false);
            stage.setScene(scene);
            //stage.getIcons().add(new Image("/images/image-removebg-preview.png"));
            stage.show();
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

        } catch (IOException ex) {
            GF.inforAlert("UI Error", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleMouseDrag(MouseEvent event) {
        if (!this.location.isEmpty()) {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            if (stage != null) {
                stage.setX(event.getScreenX() + xOffset[0]);
                stage.setY(event.getScreenY() + yOffset[0]);
            }
        }
    }

    @FXML
    private void handleMousePress(MouseEvent event) {
        if (!this.location.isEmpty()) {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            if (stage != null) {
                xOffset[0] = stage.getX() - event.getScreenX();
                yOffset[0] = stage.getY() - event.getScreenY();
            }
        }
    }

    @FXML
    private void handleClose(ActionEvent event) {
        GF.closeWindow(event);
    }

}
