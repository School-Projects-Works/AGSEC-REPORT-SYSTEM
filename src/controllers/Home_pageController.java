package controllers;

import global_classes.GlobalFuncions;
import global_classes.MongoDbAdmin;
import java.io.IOException;
import java.net.URL;
import java.time.Year;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import pojo_classes.DatabaseName;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class Home_pageController implements Initializable {

    @FXML
    private BorderPane main_page;
    @FXML
    private AnchorPane top_pane;
    @FXML
    private ImageView btn_close;
    @FXML
    private VBox left_pane;
    @FXML
    private StackPane center_container;
    /**
     * Initializes the controller class.
     */
    
    String selectedPage="Student";
    double[] xOffset = {0}, yOffset = {0};
    @FXML
    private ImageView btn_maximize;
    @FXML
    private ImageView btn_minimize;

    
    GlobalFuncions gf=new GlobalFuncions();
     MongoDbAdmin MA = new MongoDbAdmin();

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {    
        selectedPage="Student";
          gf.sideBarClick(center_container, "/screens/Students_screen.fxml"); 
         new DatabaseName().setDatabase(Year.now().toString());
        
        
         
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
    private void handleMaximizeButton(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        if (stage.getWidth() < bounds.getWidth()) {
           
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
        } else {
            stage.setWidth(900);
            stage.setHeight(580);        
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        }

    }

    @FXML
    private void handleMinimizeButton(MouseEvent event) {
        ((Stage) (((ImageView) event.getSource()).getScene().getWindow())).setIconified(true);
    }

    @FXML
    private void handleStudentClick(ActionEvent event) throws IOException {
        if(!"Student".equals(selectedPage)){
             gf.sideBarClick(center_container, "/screens/Students_screen.fxml");
            selectedPage= "Student";
        }                   
    }


    @FXML
    private void handleScoreClick(ActionEvent event) {
         if(!"Scores".equals(selectedPage)){
             gf.sideBarClick(center_container, "/screens/Scores_Screen.fxml");
             selectedPage="Scores";
        }  
    }

    @FXML
    private void handleReportClick(ActionEvent event) {
    }

    @FXML
    private void handleReportFileClick(ActionEvent event) {
    }

    @FXML
    private void handleBackupClick(ActionEvent event) {
    }

    @FXML
    private void handleSettingsClick(ActionEvent event) {
    }

}
