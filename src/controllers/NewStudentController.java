package controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import global_classes.GlobalFuncions;
import global_classes.MongoDbAdmin;
import global_classes.MongodbServices;
import java.net.URL;
import java.time.Instant;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.bson.Document;
import pojo_classes.DatabaseName;
import pojo_classes.Students;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class NewStudentController implements Initializable {

    @FXML
    private TextField tf_id;
    @FXML
    private TextField tf_name;
    @FXML
    private ComboBox<String> cb_gender;
    @FXML
    private ComboBox<String> cb_class;
    @FXML
    private TextField tf_house;

    @FXML
    private Text txt_id;
    @FXML
    private Text txt_name;
    @FXML
    private Text txt_gender;
    @FXML
    private Text txt_class;
    @FXML
    private Text txt_house;
    @FXML
    private ListView<String> lv_core;
    @FXML
    private ListView<String> lv_elective;

    /**
     * Initializes the controller class.
     */
    MongodbServices MS = new MongodbServices();
    MongoDbAdmin MA = new MongoDbAdmin();
    GlobalFuncions gf = new GlobalFuncions();
    List<String> subjects = new ArrayList();
    private ObservableList<Students> studentsList;
    double[] xOffset = {0}, yOffset = {0};
    final ContextMenu contextMenu = new ContextMenu();
    MenuItem delete = new MenuItem("Remove");
    @FXML
    private ComboBox<String> cb_subjects;
    @FXML
    private ComboBox<String> cb_year;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//       
        ArrayList<String> years = new ArrayList();
        Year year = Year.now();
        years.add(year.toString());
        for (int i = 1; i < 11; i++) {
            years.add(year.plusYears(i).toString());
        }
        cb_year.getItems().addAll(years);
        cb_year.setValue(year.toString());
       
        cb_subjects.getItems().addAll(MA.getElectiveSubjects());
        cb_gender.getItems().addAll("Male", "Female");
        cb_class.getItems().addAll("SHS 1", "SHS 2", "SHS 3", "Remedials");
        lv_core.getItems().addAll("English Language", "Mathematics", "Integrated Science", "Social Studies", "Computer ICT");
        contextMenu.getItems().add(delete);
        lv_elective.setContextMenu(contextMenu);
        delete.setOnAction((ActionEvent event) -> {
            if (!lv_elective.getSelectionModel().isEmpty()) {
                lv_elective.getItems().remove(lv_elective.getSelectionModel().getSelectedIndex());
            }

        });

        cb_gender.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (cb_gender.getValue() != null) {
                txt_gender.setText(cb_gender.getValue());
            }
        });
        cb_class.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (cb_class.getValue() != null) {
                txt_class.setText(cb_class.getValue());
            }
        });
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

    @FXML
    private void handleAddSubject(ActionEvent event) {
        if (!cb_subjects.getValue().trim().isEmpty()) {
            lv_elective.getItems().add(gf.toTitleCase(cb_subjects.getValue()));
            cb_subjects.setValue(null);

        }
    }

    @FXML
    private void handleSubmit(ActionEvent event) {
        if (lv_elective.getItems().isEmpty()) {
            gf.inforAlert("Required value", "Student should have at least an Elective Subject", Alert.AlertType.ERROR);
        } else if (tf_name.getText().isEmpty()) {
            gf.inforAlert("Required value", "Student Name is required", Alert.AlertType.ERROR);

        } else if (cb_gender.getValue().isEmpty()) {
            gf.inforAlert("Required value", "Student Gender is required", Alert.AlertType.ERROR);

        } else if (cb_class.getValue().isEmpty()) {
            gf.inforAlert("Required value", "Student class is required", Alert.AlertType.ERROR);

        } else {
            subjects.addAll(lv_core.getItems());
            subjects.addAll(lv_elective.getItems());
            Students student = new Students(
                    tf_id.getText(),
                    gf.toTitleCase(tf_name.getText()),
                    cb_class.getValue(),
                    txt_house.getText(),
                    cb_gender.getValue(),
                    subjects, Instant.now()
            );
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            ButtonType submit = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to save this Student?", submit, cancel);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setHeaderText(null);
            alert.getDialogPane().getStylesheets().add("/styles/dialog.css");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.orElse(cancel) == submit) {
                MongoDatabase database = MS.databaseConnection().getDatabase(cb_year.getValue());
                MongoCollection<Document> dataCollection = database.getCollection("students");
                Document data = dataCollection.find(new Document("id", student.getId())).first();
                if (data == null || data.isEmpty()) {
                    studentsList = MS.saveStudent(cb_year.getValue(), student);
                    if (studentsList != null && !studentsList.isEmpty()) {
                        tf_name.setText("");
                        tf_id.setText("");
                        tf_house.setText("");
                        txt_name.setText("");
                        txt_id.setText("");
                        txt_house.setText("");
                        txt_gender.setText("");
                        txt_class.setText("");
                        lv_elective.getItems().clear();
                        cb_gender.setValue(null);
                        cb_class.setValue(null);
                        gf.showToast("Sudent saved Succefully", stage);
                    } else {
                        gf.showToast("Failed to save student..try again", stage);
                    }
                } else {
                    gf.showToast("Student already Exist", stage);
                }

            }
        }

    }

    public void setStudentsObservableList(ObservableList<Students> allStudents) {
        this.studentsList = allStudents; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @FXML
    private void onNameTyping(KeyEvent event) {
        if (tf_name.getText().isEmpty()) {
            txt_name.setText("");
            tf_id.setText("");
            txt_id.setText("");
        } else {
            txt_name.setText(gf.toTitleCase(tf_name.getText()));
            short id = (short) tf_name.getText().toLowerCase().hashCode();
            tf_id.setText("AGS" + String.valueOf(id));
            txt_id.setText("AGS" + String.valueOf(id));
        }
    }

    @FXML
    private void onHouseTyping(KeyEvent event) {
        if (tf_house.getText().isEmpty()) {
            txt_house.setText("");
        } else {
            txt_house.setText(gf.toTitleCase(tf_house.getText()));
        }
    }

}
