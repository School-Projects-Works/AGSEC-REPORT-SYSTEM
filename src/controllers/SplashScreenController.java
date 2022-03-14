
package controllers;

import global_classes.GlobalFuncions;
import global_classes.MongoDbAdmin;
import global_classes.MongodbServices;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author emman
 */
public class SplashScreenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    GlobalFuncions gf=new GlobalFuncions();
    MongoDbAdmin MA=new MongoDbAdmin();
    @FXML
    private Text img;
  
      private void _openDashBoard(Event event) {
        Stage oldStage = (Stage) img.getScene().getWindow();
        oldStage.hide();
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/screens/LoginScreen.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            
            stage.setResizable(false);
            stage.setScene(scene);
            //stage.getIcons().add(new Image("/images/image-removebg-preview.png"));
            stage.show();
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

        } catch (IOException ex) {
            gf.inforAlert("UI Error", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
     private void splashTimer() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(150), new EventHandler<ActionEvent>() {
            private int i = 0;

            @Override
            public void handle(ActionEvent event) {
                i+=5;
                //progress.setProgress(i);
                if (i == 100) {
                    if(MA.databaseConnection()!=null){
                        
                       MA.createConfigurations();
                        _openDashBoard(event);
                    }else{
                        gf.inforAlert("Database Error", "Unable to Connect to Database Client", Alert.AlertType.ERROR);
                    }
                  

                } else {

                }

            }
        }));
        timeline.setCycleCount(20);
        timeline.play();

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       splashTimer();
        RotateTransition rotation = new RotateTransition(Duration.seconds(2), img);
        rotation.setCycleCount(Animation.INDEFINITE);
        rotation.setByAngle(360);
        img.setTranslateZ(img.getBoundsInLocal().getWidth() / 2.0);
        img.setRotationAxis(Rotate.Y_AXIS);
        rotation.play();
    }    
    
}
