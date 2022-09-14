/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import global_classes.ActionButtonTableCell;
import global_classes.GlobalFuncions;
import global_classes.MongoDbAdmin;
import global_classes.MongodbServices;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pojo_classes.DatabaseName;
import pojo_classes.Scores;
import pojo_classes.Students;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class Scores_ScreenController implements Initializable {

    @FXML
    private TextField tf_search;
    @FXML
    private ComboBox<String> cb_subject;
    @FXML
    private ComboBox<String> cb_class;
    @FXML
    private RadioButton rad_termOne;
    @FXML
    private RadioButton rad_termTwo;
    @FXML
    private TableView<Scores> tbv_scores;
    @FXML
    private TableColumn<Scores, String> col_name;
    @FXML
    private TableColumn<Scores, String> col_subject;
    @FXML
    private TableColumn<Scores, Number> col_exercise;
    @FXML
    private TableColumn<Scores, Number> col_midTerm;
    @FXML
    private TableColumn<Scores, Number> col_homeWork;
    @FXML
    private TableColumn<Scores, Number> col_total;
    @FXML
    private TableColumn<Scores, Number> col_classPercent;
    @FXML
    private TableColumn<Scores, Number> col_examsScore;
    @FXML
    private TableColumn<Scores, Number> col_examsPercent;
    @FXML
    private TableColumn<Scores, Number> col_overallPercent;
    @FXML
    private TableColumn<Scores, String> col_gade;
    @FXML
    private TableColumn<Scores, String> col_position;

    MongodbServices MS = new MongodbServices();
    MongoDbAdmin MA = new MongoDbAdmin();
    GlobalFuncions gf = new GlobalFuncions();
    private ObservableList<Scores> allScores;
    @FXML
    private TableColumn<?, ?> col_remarks;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getScores();
        rad_termOne.setSelected(true);
        cb_class.getItems().addAll("SHS 1", "SHS 2", "SHS 3", "Remedials");
        cb_subject.getItems().addAll("English Language", "Mathematics", "Integrated Science", "Social Studies", "Computer ICT");
        cb_subject.getItems().addAll(MA.getElectiveSubjects());
    }

    @FXML
    private void searchTyping(KeyEvent event) {
    }

    @FXML
    private void handleSUbjectFilter(ActionEvent event) {
    }

    @FXML
    private void handleClassFilter(ActionEvent event) {
    }

    @FXML
    private void handleTermOneSelect(ActionEvent event) {
        rad_termOne.setSelected(true);
        rad_termTwo.setSelected(false);
    }

    @FXML
    private void handleTermTwoSelect(ActionEvent event) {
        rad_termOne.setSelected(false);
        rad_termTwo.setSelected(true);
    }

    @FXML
    private void handleNewScoreClick(ActionEvent event) {
    }

    public void getScores() {
        allScores = FXCollections.observableArrayList();
        Service<ObservableList<Scores>> service = new Service<ObservableList<Scores>>() {
            @Override
            protected Task<ObservableList<Scores>> createTask() {
                return new Task<ObservableList<Scores>>() {
                    @Override
                    protected ObservableList<Scores> call() throws Exception {
                        return null;
                    }
                };
            }
        };
        service.setOnSucceeded((WorkerStateEvent event) -> {
            allScores = service.getValue();
//            searchList = new FilteredList<>(allStudents);
            Stage stage = (Stage) tbv_scores.getScene().getWindow();
            col_subject.setCellValueFactory(new PropertyValueFactory<>("subject"));
            col_name.setCellValueFactory(new PropertyValueFactory<>("st_name"));
            col_exercise.setCellValueFactory(new PropertyValueFactory<>("studentGender"));
            col_midTerm.setCellValueFactory(new PropertyValueFactory<>("midTerms"));
            col_homeWork.setCellValueFactory(new PropertyValueFactory<>("homeWork"));
            col_total.setCellValueFactory(new PropertyValueFactory<>("classTotal"));
            col_classPercent.setCellValueFactory(new PropertyValueFactory<>("classPercent"));
            col_examsScore.setCellValueFactory(new PropertyValueFactory<>("examsScore"));
            col_examsPercent.setCellValueFactory(new PropertyValueFactory<>("examsPercent"));
            col_overallPercent.setCellValueFactory(new PropertyValueFactory<>("overAllTotal"));
            col_gade.setCellValueFactory(new PropertyValueFactory<>("grade"));
            col_position.setCellValueFactory(new PropertyValueFactory<>("position"));
            col_remarks.setCellValueFactory(new PropertyValueFactory<>("position"));

            tbv_scores.setItems(null);
            tbv_scores.setItems(allScores);
            tbv_scores.getSelectionModel().clearSelection();

        });
        service.start();
    }

}
