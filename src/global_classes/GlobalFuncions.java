package global_classes;

import controllers.NewStudentController;
import controllers.ToastController;
import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author Fuse Koda
 */
public class GlobalFuncions {

    private static double xOffset = 0;
    private static double yOffset = 0;

    public String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String arr1 : arr) {
            sb.append(Character.toUpperCase(arr1.charAt(0))).append(arr1.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    public void inforAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText(null);
        alert.getDialogPane().getStylesheets().add("/styles/dialog.css");
        alert.getDialogPane().setMinSize(400, 120);
        alert.setContentText(message);
        alert.show();
    }

    public void closeWindow(Event event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to Close the Window?", ButtonType.YES, ButtonType.NO);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText(null);
        alert.getDialogPane().getStylesheets().add("/styles/dialog.css");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
        } else {
            alert.close();
        }
    }

    public void sideBarClick(StackPane container, String path) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            container.getChildren().clear();
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setBottomAnchor(root, 0.0);
            container.getChildren().add(root);
        } catch (IOException ex) {
            inforAlert("UI Error", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void openNewWindow(Parent parent) {
        Scene scene;
        scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        parent.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        parent.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        //stage.getIcons().add(new Image("/images/image-removebg-preview.png"));
        stage.showAndWait();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    public void showToast(String message, Stage parentState) {
        final double midX = (parentState.getX() + parentState.getWidth()) / 2;
        final double midY = (parentState.getY() + parentState.getHeight()) / 2;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/screens/toast.fxml"));
            Parent parent = fxmlLoader.load();
              ToastController controller = fxmlLoader.<ToastController>getController();
            controller.setMessage(message);
            Stage dialog = new Stage();
            Scene scene = new Scene(parent);
            scene.setFill(Color.TRANSPARENT);
            dialog.setScene(scene);
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.initStyle(StageStyle.TRANSPARENT);
            dialog.setResizable(false);
            dialog.widthProperty().addListener((observable, oldValue, newValue) -> {
                dialog.setX(midX - newValue.intValue() / 2);
            });

            dialog.heightProperty().addListener((observable, oldValue, newValue) -> {
                dialog.setY(midY - newValue.intValue() / 2);

            });
            dialog.show();
            new Timeline(new KeyFrame(
                    Duration.millis(1500),
                    ae -> {
                        dialog.close();
                    })).play();
        } catch (IOException ex) {
            inforAlert("UI Error", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

}
