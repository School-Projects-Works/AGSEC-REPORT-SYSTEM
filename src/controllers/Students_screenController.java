package controllers;

import global_classes.ActionButtonTableCell;
import global_classes.ExcellServices;
import global_classes.GlobalFuncions;
import global_classes.MongoDbAdmin;
import global_classes.MongodbServices;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JFileChooser;
import pojo_classes.Config;
import pojo_classes.Report;
import pojo_classes.Scores;
import pojo_classes.Students;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class Students_screenController implements Initializable {

    @FXML
    private TextField tf_search;

    /**
     * Initializes the controller class.
     */
    MongodbServices MS = new MongodbServices();
    GlobalFuncions gf = new GlobalFuncions();
    MongoDbAdmin MA = new MongoDbAdmin();
    ExcellServices ES = new ExcellServices();
    private ObservableList<Students> allStudents;
    FilteredList<Students> filteredList;

    @FXML
    private TableView<Students> tbv_students;
    @FXML
    private TableColumn<Students, String> col_id;
    @FXML
    private TableColumn<Students, String> col_name;
    @FXML
    private TableColumn<Students, String> col_gender;
    @FXML
    private TableColumn<Students, String> col_class;
    @FXML
    private TableColumn<Students, Button> col_view;
    @FXML
    private TableColumn<Students, Button> col_records;
    @FXML
    private TableColumn<Students, String> col_house;
    @FXML
    private TableColumn<Students, Button> col_delete;
    @FXML
    private StackPane container;

    private ObservableList<Report> reportData;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        reportData = FXCollections.observableArrayList();
        reportData = MS.getReports();
        getStudents();
        tf_search.textProperty().addListener((observable, oldValue, newValue) -> {
            if (tf_search.getText().isEmpty()) {
                filteredList.setPredicate(null);
            } else {
                Predicate<Students> filter
                        = i -> i.getId().toLowerCase().contains(tf_search.getText().toLowerCase())
                        || i.getId().toLowerCase().contains(tf_search.getText().toLowerCase())
                        || i.getStudentClass().toLowerCase().contains(tf_search.getText().toLowerCase())
                        || i.getStudentGender().toLowerCase().equalsIgnoreCase(tf_search.getText().toLowerCase())
                        || i.getStudentName().toLowerCase().contains(tf_search.getText().toLowerCase())
                        || i.getStudentSubjects().contains(tf_search.getText().toLowerCase())
                        || i.getGuardianEmail().toLowerCase().contains(tf_search.getText().toLowerCase());

                filteredList.setPredicate(filter);

            }
        });
    }

    @FXML
    private void handleNewStudentClick(ActionEvent event) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/screens/NewStudent.fxml"));
            Parent parent = fxmlLoader.load();
            NewStudentController controller = fxmlLoader.<NewStudentController>getController();
            controller.setStudentsObservableList(allStudents, null);
            gf.openNewWindow(parent);
        } catch (IOException ex) {
            gf.inforAlert("UI Error", "Unable to open Form", Alert.AlertType.ERROR);
        }
    }

    public void getStudents() {
        allStudents = FXCollections.observableArrayList();
        Service<ObservableList<Students>> service = new Service<ObservableList<Students>>() {
            @Override
            protected Task<ObservableList<Students>> createTask() {
                return new Task<ObservableList<Students>>() {
                    @Override
                    protected ObservableList<Students> call() throws Exception {
                        return MS.getAllStudents();
                    }
                };
            }
        };
        service.setOnSucceeded((WorkerStateEvent event) -> {
            allStudents = service.getValue();
            filteredList = new FilteredList<>(allStudents);
            Stage stage = (Stage) tbv_students.getScene().getWindow();
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_name.setCellValueFactory(new PropertyValueFactory<>("studentName"));
            col_gender.setCellValueFactory(new PropertyValueFactory<>("studentGender"));
            col_house.setCellValueFactory(new PropertyValueFactory<>("guardianEmail"));
            col_class.setCellValueFactory(new PropertyValueFactory<>("studentClass"));
            String viewStyle = "/styles/view_buttons_style.css";
            String recordsStyle = "/styles/records_button_style.css";
            String deleteStyle = "/styles/delete_button_style.css";
            String deletePathe = "/images/delete.png";
            String editPathe = "/images/edit.png";
            String reportPathe = "/images/report.png";

            col_view.setCellFactory(ActionButtonTableCell.<Students>forTableColumn(editPathe, viewStyle, (Students p) -> {
                try {

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/screens/NewStudent.fxml"));
                    Parent parent = fxmlLoader.load();
                    NewStudentController controller = fxmlLoader.<NewStudentController>getController();
                    controller.setStudentsObservableList(allStudents, p);
                    gf.openNewWindow(parent);
                } catch (IOException ex) {
                    gf.inforAlert("UI Error", "Unable to open Form", Alert.AlertType.ERROR);
                }

                return p;
            }));

            col_records.setCellFactory(ActionButtonTableCell.<Students>forTableColumn(reportPathe, recordsStyle, (Students p) -> {
                File file = null;
                for (Report report : reportData) {
                    if (report.getId() == null ? p.getId() == null : report.getId().equals(p.getId())) {
                        file = new File(report.getPath());

                    }
                }
                if (file != null && file.exists()) {
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().open(file);
                        } catch (IOException ex) {
                            Logger.getLogger(ReportScreenController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        gf.inforAlert("File Opeing", "File can not open on this desktop", Alert.AlertType.INFORMATION);
                    }
                } else {
                    gf.inforAlert("Rrpot generation", "Report for this student not generated yet", Alert.AlertType.INFORMATION);
                }
                return p;
            }));
            col_delete.setCellFactory(ActionButtonTableCell.<Students>forTableColumn(deletePathe, deleteStyle, (Students p) -> {

                ButtonType submit = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Student?", submit, cancel);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setHeaderText(null);
                alert.getDialogPane().getStylesheets().add("/styles/dialog.css");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.orElse(cancel) == submit) {
                    if (MS.deleteStudent(p.getId()) != null) {
                        getStudents();
                        gf.showToast("Sudent deleted Succefully", stage);
                    } else {
                        gf.showToast("Faild to delete Student", stage);
                    }
                }
                return p;
            }));

            tbv_students.setItems(null);
            tbv_students.setItems(filteredList);
            tbv_students.getSelectionModel().clearSelection();

        });
        service.start();
    }

    public void updateList() {
        allStudents = MS.getAllStudents();
    }

    @FXML
    private void searchTyping(KeyEvent event) {
    }

    @FXML
    private void handlerRefresh(MouseEvent event) {
        getStudents();
    }

    @FXML
    private void handleExport(ActionEvent event) {
        final String folder = new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "/ARG SCORESHEET";
        Config config = MA.getConfig();
        ButtonType submit = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to Export Sudents Data?", submit, cancel);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText(null);
        alert.getDialogPane().getStylesheets().add("/styles/dialog.css");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.orElse(cancel) == submit) {
            Service<File> service = new Service<File>() {
                @Override
                protected Task<File> createTask() {
                    return new Task<File>() {
                        @Override
                        protected File call() throws Exception {
                            for (String sub : config.getSubjects()) {
                                ES.ExportStudents(sub);
                            }
                            return null;
                        }
                    };
                }
            };

            service.setOnSucceeded((WorkerStateEvent eve) -> {
                try {

                    gf.inforAlert("Export Completed", "Data Export Successfull,\nLocate AGR SCORESHEET in My Document folder", Alert.AlertType.INFORMATION);
                    Desktop.getDesktop().open(new File(folder));
                } catch (IOException ex) {
                    Logger.getLogger(Students_screenController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            );
            service.start();

        }

    }

}
