package global_classes;


import controllers.ToastController;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.List;
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
import javax.imageio.ImageIO;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.poi.ss.usermodel.Row;

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
        alert.showAndWait();
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

    public boolean verifyScoreFile(Row courseTitleRow) {

        String col1 = courseTitleRow.getCell(0).getStringCellValue().toUpperCase();
        String col2 = courseTitleRow.getCell(1).getStringCellValue().toUpperCase();
        String col3 = courseTitleRow.getCell(2).getStringCellValue().toUpperCase();
        String col4 = courseTitleRow.getCell(3).getStringCellValue().toUpperCase();
        String col5 = courseTitleRow.getCell(4).getStringCellValue().toUpperCase();
        String col6 = courseTitleRow.getCell(5).getStringCellValue().toUpperCase();
        String col7 = courseTitleRow.getCell(6).getStringCellValue().toUpperCase();
        String col8 = courseTitleRow.getCell(7).getStringCellValue().toUpperCase();
        String col9 = courseTitleRow.getCell(8).getStringCellValue().toUpperCase();
        return col1.contains("STUDENT ID") && col2.contains("STUDENT NAME") && col3.contains("CLASS") && col4.contains("SUBJECT")
                && col5.contains("CLASS EXERCISE (40)") && col6.contains("MID TERM/TEST(40)")
                && col7.contains("HOME WORK(20)") && col8.contains("EXAMS (100)")
                && col9.contains("TEACHER REMARKS");

    }

    public boolean isValidEmailAddress(String email) {
        boolean allowLocal = true;
        return EmailValidator.getInstance(allowLocal).isValid(email);
    }

    boolean verifyStudentFile(Row courseTitleRow) {
        String col1 = courseTitleRow.getCell(0).getStringCellValue().toUpperCase();
        String col2 = courseTitleRow.getCell(1).getStringCellValue().toUpperCase();
        String col3 = courseTitleRow.getCell(2).getStringCellValue().toUpperCase();
        String col4 = courseTitleRow.getCell(3).getStringCellValue().toUpperCase();
        String col5 = courseTitleRow.getCell(4).getStringCellValue().toUpperCase();
        System.out.println(col1 + " " + col2 + " " + col3 + " " + col4 + " " + col5);
        return col1.contains("STUDENT NAME") && col2.contains("GENDER")
                && col3.contains("CLASS") && col4.contains("GUARDIAN EMAIL(OPTIONAL)") && col5.contains("SUBJECTS(SEPERATE WITH (,))");
    }

    public boolean containsCaseInsensitive(String s, List<String> l) {
        for (String string : l) {
            if (string.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }
    
    public File resize(File inputFile,
            String outputImagePath, int scaledWidth, int scaledHeight)
            throws IOException {
       
        BufferedImage inputImage = ImageIO.read(inputFile);
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());
 
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);
        File outPut =new File(outputImagePath);
        ImageIO.write(outputImage, formatName, outPut);
        
        return outPut;
    }
    
    
    
    public void PrintFile(String path){
          PrinterJob pj = PrinterJob.getPrinterJob();
        if (pj.printDialog()) {
            PageFormat pf = pj.defaultPage();
            Paper paper = pf.getPaper();    
            double width = fromCMToPPI(3.5);
            double height = fromCMToPPI(8.8);    
            paper.setSize(width, height);
            paper.setImageableArea(
                            fromCMToPPI(0.25), 
                            fromCMToPPI(0.5), 
                            width - fromCMToPPI(0.35), 
                            height - fromCMToPPI(1));                
            System.out.println("Before- " + dump(paper));    
            pf.setOrientation(PageFormat.PORTRAIT);
            pf.setPaper(paper);    
            System.out.println("After- " + dump(paper));
            System.out.println("After- " + dump(pf));                
            dump(pf);    
            PageFormat validatePage = pj.validatePage(pf);
            System.out.println("Valid- " + dump(validatePage));                        
            pj.setPrintable(new MyPrintable(), pf);
            try {
                pj.print();
            } catch (PrinterException ex) {
                ex.printStackTrace();
            }    
        }  
    }
 protected static double fromCMToPPI(double cm) {            
        return toPPI(cm * 0.393700787);            
    }

    protected static double toPPI(double inch) {            
        return inch * 72d;            
    }

    protected static String dump(Paper paper) {            
        StringBuilder sb = new StringBuilder(64);
        sb.append(paper.getWidth()).append("x").append(paper.getHeight())
           .append("/").append(paper.getImageableX()).append("x").
           append(paper.getImageableY()).append(" - ").append(paper
       .getImageableWidth()).append("x").append(paper.getImageableHeight());            
        return sb.toString();            
    }

    protected static String dump(PageFormat pf) {    
        Paper paper = pf.getPaper();            
        return dump(paper);    
    }

    public static class MyPrintable implements Printable {

        @Override
        public int print(Graphics graphics, PageFormat pageFormat, 
            int pageIndex) throws PrinterException {    
            System.out.println(pageIndex);                
            int result = NO_SUCH_PAGE;    
            if (pageIndex < 2) {                    
                Graphics2D g2d = (Graphics2D) graphics;                    
                System.out.println("[Print] " + dump(pageFormat));                    
                double width = pageFormat.getImageableWidth();
                double height = pageFormat.getImageableHeight();    
                g2d.translate((int) pageFormat.getImageableX(), 
                    (int) pageFormat.getImageableY());                    
                g2d.draw(new java.awt.geom.Rectangle2D.Double(1, 1, width - 1, height - 1));                    
                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString("0x0", 0, fm.getAscent());    
                result = PAGE_EXISTS;    
            }    
            return result;    
        }
    }
}
