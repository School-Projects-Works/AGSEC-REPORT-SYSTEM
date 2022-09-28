/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import global_classes.ExcellServices;
import global_classes.GlobalFuncions;
import global_classes.MongoDbAdmin;
import global_classes.MongodbServices;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pojo_classes.Config;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class ExportDataController implements Initializable {

    @FXML
    private StackPane container;
    @FXML
    private JFXComboBox<String> cmb_subject;
    @FXML
    private JFXButton btn_export;

    /**
     * Initializes the controller class.
     */
    MongodbServices MS = new MongodbServices();
    MongoDbAdmin MA = new MongoDbAdmin();
    GlobalFuncions GF = new GlobalFuncions();
    ExcellServices ES = new ExcellServices();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Config config=MA.getConfig();
        cmb_subject.getItems().addAll(config.getSubjects());
       
    }

    @FXML
    private void handleExport(ActionEvent event) {
//        ES.ExportStudents(container, cmb_subject.getValue());
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleSubjectSelection(ActionEvent event) {
        if (cmb_subject.getValue() != null) {
            btn_export.setVisible(true);
        }
    }

}
