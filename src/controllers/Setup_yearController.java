/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import global_classes.GlobalFuncions;
import global_classes.MongoDbAdmin;
import global_classes.MongodbServices;
import java.io.IOException;
import java.net.URL;
import java.time.Year;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class Setup_yearController implements Initializable {

    @FXML
    private TextField tf_subject;
    @FXML
    private ListView<String> lv_elective;
    @FXML
    private ComboBox<String> cb_year;

    MongoDbAdmin MA = new MongoDbAdmin();
     MongodbServices MS = new MongodbServices();
    GlobalFuncions gf = new GlobalFuncions();
    double[] xOffset = {0}, yOffset = {0};

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<String> years = new ArrayList();
        Year year = Year.now();
        years.add(year.toString());
        for (int i = 1; i < 11; i++) {
            years.add(year.plusYears(i).toString());
        }
        cb_year.getItems().addAll(years);
        lv_elective.getItems().addAll(MA.getElectiveSubjects());
    }

    @FXML
    private void handleSaveSettings(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (cb_year.getValue() != null) {
            Alert alert;
            ButtonType submit = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            if (lv_elective.getItems().isEmpty()) {
                alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to create new Year without any electives?", submit, cancel);
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to create New Year? ", submit, cancel);
            }

            alert.initStyle(StageStyle.UNDECORATED);
            alert.setHeaderText(null);
            alert.getDialogPane().getStylesheets().add("/styles/dialog.css");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.orElse(cancel) == submit) {               
                    MS.getDataBase(cb_year.getValue());
                    if(MA.getElectiveSubjects()!=lv_elective.getItems()){
                        for (String sub:lv_elective.getItems()) {
                            MA.saveElectives(sub);
                        }
                    }
                    Screen screen = Screen.getPrimary();
                    Rectangle2D bounds = screen.getVisualBounds();
                    Parent root = FXMLLoader.load(getClass().getResource("/screens/home_page.fxml"));
                    stage.setX(bounds.getMinX());
                    stage.setY(bounds.getMinY());
                    stage.setWidth(bounds.getWidth());
                    stage.setHeight(bounds.getHeight());
                    Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                    stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
                    stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                
            }
        } else {
            gf.showToast("Please Select New Year", stage);
        }
    }

    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            if (!tf_subject.getText().trim().isEmpty()) {
                lv_elective.getItems().add(gf.toTitleCase(tf_subject.getText()));
                tf_subject.setText("");
            }
        }
    }

    @FXML
    private void handleAddSubject(ActionEvent event) {
        if (!tf_subject.getText().trim().isEmpty()) {
            lv_elective.getItems().add(gf.toTitleCase(tf_subject.getText()));
            tf_subject.setText("");

        }
    }

    @FXML
    private void handleCloseButton(MouseEvent event) {
        gf.closeWindow(event);
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
    private void handleMousePress(MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        if (stage != null) {
            xOffset[0] = stage.getX() - event.getScreenX();
            yOffset[0] = stage.getY() - event.getScreenY();
        }
    }

}
