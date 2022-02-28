package controllers;

import com.jfoenix.controls.JFXButton;
import global_classes.ActionButtonTableCell;
import global_classes.GlobalFuncions;
import global_classes.MongodbServices;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pojo_classes.Students;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class Students_screenController implements Initializable {

    @FXML
    private JFXButton btn_newStudent;
    @FXML
    private TextField tf_search;

    /**
     * Initializes the controller class.
     */
    MongodbServices MS = new MongodbServices();
    GlobalFuncions gf = new GlobalFuncions();
    private ObservableList<Students> allStudents;
       FilteredList<Students> searchList;

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
    private TableColumn<Students, Button> col_update;
    @FXML
    private TableColumn<Students, String> col_house;
    @FXML
    private TableColumn<Students, Button> col_delete;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getStudents();

    }

    @FXML
    private void handleNewStudentClick(ActionEvent event) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/screens/NewStudent.fxml"));
            Parent parent = fxmlLoader.load();
            NewStudentController controller = fxmlLoader.<NewStudentController>getController();
            controller.setStudentsObservableList(allStudents);
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
             searchList = new FilteredList<>(allStudents);
            Stage stage = (Stage) tbv_students.getScene().getWindow();
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_name.setCellValueFactory(new PropertyValueFactory<>("studentName"));
            col_gender.setCellValueFactory(new PropertyValueFactory<>("studentGender"));
            col_house.setCellValueFactory(new PropertyValueFactory<>("studentHouse"));
            col_class.setCellValueFactory(new PropertyValueFactory<>("studentClass"));
            String viewStyle = "/styles/view_buttons_style.css";
            String updateStyle = "/styles/update_button_style.css";
            String recordsStyle = "/styles/records_button_style.css";
            String deleteStyle = "/styles/delete_button_style.css";
            col_view.setCellFactory(ActionButtonTableCell.<Students>forTableColumn("View", viewStyle, (Students p) -> {
                return p;
            }));
            col_update.setCellFactory(ActionButtonTableCell.<Students>forTableColumn("Update", updateStyle, (Students p) -> {

                return p;
            }));
            col_records.setCellFactory(ActionButtonTableCell.<Students>forTableColumn("Report", recordsStyle, (Students p) -> {

                return p;
            }));
            col_delete.setCellFactory(ActionButtonTableCell.<Students>forTableColumn("Delete", deleteStyle, (Students p) -> {

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
            tbv_students.setItems(searchList);
            tbv_students.getSelectionModel().clearSelection();
        });
        service.start();
    }

    @FXML
    private void searchTyping(KeyEvent event) {
     
        if (tf_search.getText().isEmpty()) {
            searchList.setPredicate(null);
           
        } else {
           
           // allStudents=allStudents.filtered(stu->stu.getId().toLowerCase().contains(""));
           Predicate<Students> filter
                    = i -> i.getId().toLowerCase().contains(tf_search.getText().toLowerCase())
                    || i.getId().toLowerCase().contains(tf_search.getText().toLowerCase())
                    || i.getStudentClass().toLowerCase().contains(tf_search.getText().toLowerCase())
                    || i.getStudentGender().toLowerCase().equalsIgnoreCase(tf_search.getText().toLowerCase())
                    || i.getStudentName().toLowerCase().contains(tf_search.getText().toLowerCase())
                    || i.getStudentSubjects().contains(tf_search.getText().toLowerCase())
                    || i.getStudentHouse().toLowerCase().contains(tf_search.getText().toLowerCase());

            searchList.setPredicate(filter);
            //allStudents = searchList;
        }
    }

}
