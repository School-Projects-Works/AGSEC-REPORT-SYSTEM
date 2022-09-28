package controllers;

import global_classes.ActionButtonTableCell;
import global_classes.GlobalFuncions;
import global_classes.MongodbServices;
import global_classes.PDFGeneration;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pojo_classes.Report;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class ReportScreenController implements Initializable {

    @FXML
    private TextField tf_search;

    @FXML
    private StackPane container;
    @FXML
    private TableView<Report> tbv_report;
    @FXML
    private TableColumn<Report, String> col_name;
    @FXML
    private TableColumn<Report, String> col_class;
    @FXML
    private TableColumn<Report, String> col_subjects;
    @FXML
    private TableColumn<Report, String> col_email;
    @FXML
    private TableColumn<Report, Button> col_view;
    @FXML
    private TableColumn<Report, Button> col_delete;
    @FXML
    private TableColumn<Report, Button> col_print;
    @FXML
    private TableColumn<Report, Button> col_send;

    /**
     * Initializes the controller class.
     */
    MongodbServices MS = new MongodbServices();
    GlobalFuncions GF = new GlobalFuncions();
    private ObservableList<Report> reportData;
    PDFGeneration Pdfgenerate = new PDFGeneration();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        getReports();
    }

    @FXML
    private void searchTyping(KeyEvent event) {
        
    }

    @FXML
    private void handleGenerate(ActionEvent event) {
        ButtonType submit = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to generate Student Report?", submit, cancel);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText(null);
        alert.getDialogPane().getStylesheets().add("/styles/dialog.css");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.orElse(cancel) == submit) {
            try {
                Pdfgenerate.createFiles();
                getReports();
            } catch (IOException ex) {
                Logger.getLogger(ReportScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    private void handlerRefresh(MouseEvent event) {
          getReports();
        
    }

    @FXML
    private void handlePrint(ActionEvent event) {
          GF.inforAlert("Printing", "No Printer connected", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleSendToMail(ActionEvent event) {
    }

    public void getReports() {
        reportData = FXCollections.observableArrayList();
        Service<ObservableList<Report>> service = new Service<ObservableList<Report>>() {
            @Override
            protected Task<ObservableList<Report>> createTask() {
                return new Task<ObservableList<Report>>() {
                    @Override
                    protected ObservableList<Report> call() throws Exception {
                        return MS.getReports();
                    }
                };
            }
        };
        service.setOnSucceeded((WorkerStateEvent event) -> {
            reportData = service.getValue();
//            searchList = new FilteredList<>(allStudents);
            Stage stage = (Stage) tbv_report.getScene().getWindow();
            col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
            col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
            col_class.setCellValueFactory(new PropertyValueFactory<>("studentClass"));
            col_subjects.setCellValueFactory(new PropertyValueFactory<>("numOfSubjects"));
            String viewStyle = "/styles/view_buttons_style.css";
            String printStyle = "/styles/records_button_style.css";
            String deleteStyle = "/styles/delete_button_style.css";
            String deletePathe = "/images/delete.png";
            String viewPathe = "/images/view.png";
            String printPathe = "/images/print.png";
            String sendPathe = "/images/send.png";

            col_view.setCellFactory(ActionButtonTableCell.<Report>forTableColumn(viewPathe, viewStyle, (Report p) -> {
                File file=new File(p.getPath());
                if(file.exists()){
                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().open(file);
                        } catch (IOException ex) {
                            Logger.getLogger(ReportScreenController.class.getName()).log(Level.SEVERE, null, ex);
                        }
			} else {
				GF.inforAlert("File Opening", "File can not open on this desktop", Alert.AlertType.INFORMATION);
			}
                }
                return p;
            }));
            col_print.setCellFactory(ActionButtonTableCell.<Report>forTableColumn(printPathe, printStyle, (Report p) -> {
                GF.inforAlert("Printing", "No Printer connected", Alert.AlertType.INFORMATION);
                return p;
            }));
            col_send.setCellFactory(ActionButtonTableCell.<Report>forTableColumn(sendPathe, viewStyle, (Report p) -> {
                if(p.getEmail()!=null&&!p.getEmail().isEmpty()){
                    
                }else{
                    GF.inforAlert("Email Not Found", "Student guardian email not found", Alert.AlertType.INFORMATION);
                }
                return p;
            }));

            col_delete.setCellFactory(ActionButtonTableCell.<Report>forTableColumn(deletePathe, deleteStyle, (Report p) -> {
                ButtonType submit = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
                ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Student Report?", submit, cancel);
                alert.initStyle(StageStyle.UNDECORATED);
                alert.setHeaderText(null);
                alert.getDialogPane().getStylesheets().add("/styles/dialog.css");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.orElse(cancel) == submit) {
                    if (MS.deleteReport(p) != null) {
                       
                        getReports();
                        GF.showToast("Sudent Report deleted Succefully", stage);
                    } else {
                        GF.showToast("Faild to delete Student Report", stage);
                    }
                }
                return p;
            }));

            tbv_report.setItems(null);
            tbv_report.setItems(reportData);
            tbv_report.getSelectionModel().clearSelection();

        });
        service.start();
    }

}
