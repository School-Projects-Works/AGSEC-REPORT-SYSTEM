/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controllers;

import global_classes.ActionButtonTableCell;
import global_classes.ExcellServices;
import global_classes.GlobalFuncions;
import global_classes.MongoDbAdmin;
import global_classes.MongodbServices;
import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pojo_classes.Config;
import pojo_classes.Scores;

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
    @FXML
    private TableColumn<Scores, String> col_class;

    MongodbServices MS = new MongodbServices();
    MongoDbAdmin MA = new MongoDbAdmin();
    GlobalFuncions gf = new GlobalFuncions();
    private ObservableList<Scores> allScores;
    FilteredList<Scores> filteredList;
    FileChooser fileChooser = new FileChooser();
    GlobalFuncions GF = new GlobalFuncions();
    ExcellServices ES = new ExcellServices();
    @FXML
    private TableColumn<Scores, String> col_remarks;
    @FXML
    private StackPane container;
    @FXML
    private TableColumn<Scores, Button> col_delete;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getScores();
        cb_class.getItems().addAll("All", "SHS 1", "SHS 2", "SHS 3", "Remedials");
        Config config = MA.getConfig();
        cb_subject.getItems().add("All");
        cb_subject.getItems().addAll(config.getSubjects());
        cb_subject.setValue("All");
        cb_class.setValue("All");
    }

    @FXML
    private void searchTyping(KeyEvent event) {
        String search = tf_search.getText().toLowerCase();
        if (search.isEmpty()) {
            filteredList.setPredicate((Scores score) -> true);

        } else {
            cb_subject.setValue("All");
            cb_class.setValue("All");
            filteredList.setPredicate((Scores score) -> score.getSt_name().toLowerCase().contains(search));

        }
    }

    @FXML
    private void handleSUbjectFilter(ActionEvent event) {
        String val = cb_subject.getValue();
        String stClass = cb_class.getValue();
        if (!"All".equals(val)) {
            if (stClass.equals("All")) {
                filteredList.setPredicate((Scores score) -> score.getSubject().equals(val));
            } else {
                filteredList.setPredicate((Scores score) -> score.getSubject().equals(val) && score.getSt_class().equals(stClass));
            }
        } else {
            if (stClass.equals("All")) {
                filteredList.setPredicate((Scores score) -> true);
            } else {
                filteredList.setPredicate((Scores score) -> score.getSt_class().equals(stClass));
            }

        }

    }

    @FXML
    private void handleClassFilter(ActionEvent event) {
        String subject = cb_subject.getValue();
        String stClass = cb_class.getValue();
        if (!"All".equals(stClass)) {
            if (subject.equals("All")) {
                filteredList.setPredicate((Scores score) -> score.getSt_class().equals(stClass));
            } else {
                filteredList.setPredicate((Scores score) -> score.getSubject().equals(subject) && score.getSt_class().equals(stClass));
            }
        } else {
            if (subject.equals("All")) {
                filteredList.setPredicate((Scores score) -> true);
            } else {
                filteredList.setPredicate((Scores score) -> score.getSubject().equals(subject));
            }

        }
    }

    @FXML
    private void handleNewScoreClick(ActionEvent event) {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"),
                new FileChooser.ExtensionFilter("Excel Files", "*.xls")
        );
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null && selectedFile.exists()) {
            ES.LoadScores(selectedFile, container);
            getScores();
        }
    }

    public void getScores() {

        allScores = FXCollections.observableArrayList();
        Service<ObservableList<Scores>> service = new Service<ObservableList<Scores>>() {
            @Override
            protected Task<ObservableList<Scores>> createTask() {
                return new Task<ObservableList<Scores>>() {
                    @Override
                    protected ObservableList<Scores> call() throws Exception {
                        return MS.getAllScores();
                    }
                };
            }
        };
        service.setOnSucceeded((WorkerStateEvent event) -> {
            allScores = service.getValue();
            filteredList = new FilteredList<>(allScores);
            Stage stage = (Stage) tbv_scores.getScene().getWindow();
            col_subject.setCellValueFactory(new PropertyValueFactory<>("subject"));
            col_name.setCellValueFactory(new PropertyValueFactory<>("st_name"));
            col_exercise.setCellValueFactory(new PropertyValueFactory<>("exercise"));
            col_midTerm.setCellValueFactory(new PropertyValueFactory<>("midTerms"));
            col_homeWork.setCellValueFactory(new PropertyValueFactory<>("homeWork"));
            col_total.setCellValueFactory(new PropertyValueFactory<>("classTotal"));
            col_classPercent.setCellValueFactory(new PropertyValueFactory<>("classPercent"));
            col_examsScore.setCellValueFactory(new PropertyValueFactory<>("examsScore"));
            col_examsPercent.setCellValueFactory(new PropertyValueFactory<>("examsPercent"));
            col_overallPercent.setCellValueFactory(new PropertyValueFactory<>("overAllTotal"));
            col_gade.setCellValueFactory(new PropertyValueFactory<>("grade"));
            col_position.setCellValueFactory(new PropertyValueFactory<>("position"));
            col_remarks.setCellValueFactory(new PropertyValueFactory<>("remarks"));
            col_class.setCellValueFactory(new PropertyValueFactory<>("st_class"));

    
            String deleteStyle = "/styles/delete_button_style.css";
            String deletePathe = "/images/delete.png";
    
        

            col_delete.setCellFactory(ActionButtonTableCell.<Scores>forTableColumn(deletePathe, deleteStyle, (Scores p) -> {

                ButtonType submit = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Student Score?", submit, cancel);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setHeaderText(null);
                alert.getDialogPane().getStylesheets().add("/styles/dialog.css");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.orElse(cancel) == submit) {
                    if (MS.deleteScore(p.getId()) != null) {
                        getScores();
                        gf.showToast("Sudent score deleted Succefully", stage);
                    } else {
                        gf.showToast("Faild to delete Student score", stage);
                    }
                }
                return p;
            }));

            tbv_scores.setItems(null);
            tbv_scores.setItems(filteredList);
            tbv_scores.getSelectionModel().clearSelection();

        });
        service.start();
    }

    @FXML
    private void handlerRefresh(MouseEvent event) {
        getScores();
    }

}
