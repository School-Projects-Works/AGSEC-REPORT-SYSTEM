/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import global_classes.GlobalFuncions;
import global_classes.MongodbServices;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pojo_classes.ElectiveSubjects;
import pojo_classes.Students;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class Add_ElectivesController implements Initializable {

    @FXML
    private TextField tf_subject;
    @FXML
    private Button btn_add;
    @FXML
    private TableView<ElectiveSubjects> tbv_subject;
    @FXML
    private TableColumn<ElectiveSubjects, String> col_id;
    @FXML
    private TableColumn<ElectiveSubjects, String> col_subject;

    MongodbServices MS = new MongodbServices();
    GlobalFuncions gf = new GlobalFuncions();
    double[] xOffset = {0}, yOffset = {0};
    private ObservableList<ElectiveSubjects> subjects;
    @FXML
    private Text txt_error;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleCloseButton(MouseEvent event) {
        gf.closeWindow(event);
    }

    @FXML
    private void handleOnMouseDrag(MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        if (stage != null) {
            stage.setX(event.getScreenX() + xOffset[0]);
            stage.setY(event.getScreenY() + yOffset[0]);
        }
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
    private void onAction(ActionEvent event) {
        if (!tf_subject.getText().trim().isEmpty()) {
            boolean exist = false;
            short id = (short) tf_subject.getText().toLowerCase().hashCode();
            for (ElectiveSubjects subject : tbv_subject.getItems()) {
                if (subject.getId().equals("SUB" + id)) {
                    exist = true;
                }
            }
            if (exist) {
                txt_error.setText("Subject Already Added");
            } else {
                txt_error.setText("");
                tbv_subject.getItems().add(new ElectiveSubjects("", gf.toTitleCase(tf_subject.getText())));
                tf_subject.setText("");
            }

        }
    }

    @FXML
    private void handleEnterKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            if (!tf_subject.getText().trim().isEmpty()) {
                boolean exist = false;
                short id = (short) tf_subject.getText().toLowerCase().hashCode();
                for (ElectiveSubjects subject : tbv_subject.getItems()) {
                    if (subject.getId().equals("SUB" + id)) {
                        exist = true;
                    }
                }
                if (exist) {
                    txt_error.setText("Subject Already Added");
                } else {
                    txt_error.setText("");
                    tbv_subject.getItems().add(new ElectiveSubjects("", gf.toTitleCase(tf_subject.getText())));
                    tf_subject.setText("");
                }

            }
        }
    }

    @FXML
    private void onSave(ActionEvent event) {
        if (tbv_subject.getItems() != null) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            ButtonType submit = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to save this Subjects?", submit, cancel);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setHeaderText(null);
            alert.getDialogPane().getStylesheets().add("/styles/dialog.css");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.orElse(cancel) == submit) {
                for (ElectiveSubjects subject : tbv_subject.getItems()) {
                    MS.saveElectiveSubject(subject);
                }

            }
            tf_subject.setText("");
            gf.showToast("Sudent saved Succefully", stage);
        }
    }

}
