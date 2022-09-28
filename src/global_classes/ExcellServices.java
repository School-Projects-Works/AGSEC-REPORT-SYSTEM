/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package global_classes;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.StageStyle;
import javax.swing.JFileChooser;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;
import pojo_classes.Config;
import pojo_classes.Scores;
import pojo_classes.Students;

/**
 *
 * @author emman
 */
public class ExcellServices {

    MongodbServices MS = new MongodbServices();

    private int count = 1;
    FileOutputStream fileOut;
    private ProgressIndicator p;
    Region veil = new Region();
    GlobalFuncions GF = new GlobalFuncions();
    MongoDbAdmin MA = new MongoDbAdmin();

    public void ExportStudents(String subject) {
        final String folder = new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "/ARG SCORESHEET";
        Path path = Paths.get(folder);
        try {
            Files.createDirectories(path);
        } catch (IOException ex) {
            Logger.getLogger(ExcellServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        final String filename = folder + "/" + subject + ".xlsx";
        XSSFWorkbook workbook = new XSSFWorkbook();
        count = 1;
        try {
            File file = new File(filename);
            if (file.exists()) {
                file.delete();
            }
            ObservableList<Students> listOfScores = MS.getAllStudents();

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setLocked(true);
            XSSFFont font = workbook.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short) 10);
            font.setBold(true);
            headerStyle.setFont(font);

            CellStyle unlockedCellStyle = workbook.createCellStyle();
            unlockedCellStyle.setLocked(false);
            CellStyle lockedCellStyle = workbook.createCellStyle();
            lockedCellStyle.setLocked(true);

            XSSFSheet sheet = workbook.createSheet("FirstSheet");
            sheet.protectSheet("");
            XSSFRow rowhead = sheet.createRow((short) 0);
            int width = 25;
            sheet.setDefaultColumnWidth(width);

            Cell headerCell1 = rowhead.createCell(0);
            headerCell1.setCellValue("STUDENT ID");
            headerCell1.setCellStyle(headerStyle);

            Cell headerCell2 = rowhead.createCell(1);
            headerCell2.setCellValue("STUDENT NAME");
            headerCell2.setCellStyle(headerStyle);

            Cell headerCell3 = rowhead.createCell(2);
            headerCell3.setCellValue("CLASS");
            headerCell3.setCellStyle(headerStyle);

            Cell headerCell4 = rowhead.createCell(3);
            headerCell4.setCellValue("SUBJECT");
            headerCell4.setCellStyle(headerStyle);

            Cell headerCell5 = rowhead.createCell(4);
            headerCell5.setCellValue("CLASS EXERCISE (40)");
            headerCell5.setCellStyle(headerStyle);

            Cell headerCell6 = rowhead.createCell(5);
            headerCell6.setCellValue("MID TERM/TEST(40)");
            headerCell6.setCellStyle(headerStyle);

            Cell headerCell7 = rowhead.createCell(6);
            headerCell7.setCellValue("HOME WORK(20)");
            headerCell7.setCellStyle(headerStyle);

            Cell headerCell8 = rowhead.createCell(7);
            headerCell8.setCellValue("EXAMS (100)");
            headerCell8.setCellStyle(headerStyle);

            Cell headerCell9 = rowhead.createCell(8);
            headerCell9.setCellValue("TEACHER REMARKS");
            headerCell9.setCellStyle(headerStyle);

            if (!listOfScores.isEmpty()) {
                listOfScores.forEach((data1) -> {
                    if (data1.getStudentSubjects().contains(subject)) {
                        XSSFRow row = sheet.createRow((short) count);

                        Cell cell1 = row.createCell(0);
                        cell1.setCellValue(data1.getId());
                        cell1.setCellStyle(lockedCellStyle);

                        Cell cell2 = row.createCell(1);
                        cell2.setCellValue(data1.getStudentName());
                        cell2.setCellStyle(lockedCellStyle);

                        Cell cell3 = row.createCell(2);
                        cell3.setCellValue(data1.getStudentClass());
                        cell3.setCellStyle(lockedCellStyle);

                        Cell cell4 = row.createCell(3);
                        cell4.setCellValue(subject);
                        cell4.setCellStyle(lockedCellStyle);

                        Cell cell5 = row.createCell(4);
                        cell5.setCellValue("");
                        cell5.setCellStyle(unlockedCellStyle);

                        Cell cell6 = row.createCell(5);
                        cell6.setCellValue("");
                        cell6.setCellStyle(unlockedCellStyle);

                        Cell cell7 = row.createCell(6);
                        cell7.setCellValue("");
                        cell7.setCellStyle(unlockedCellStyle);

                        Cell cell8 = row.createCell(7);
                        cell8.setCellValue("");
                        cell8.setCellStyle(unlockedCellStyle);

                        Cell cell9 = row.createCell(8);
                        cell9.setCellValue("");
                        cell9.setCellStyle(unlockedCellStyle);

                        count++;
                    }
                }
                );

            }

            fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);

        } catch (IOException ex) {
            GF.inforAlert("Error", ex.getMessage(), Alert.AlertType.ERROR);
        } finally {
            try {
                fileOut.close();
                workbook.close();
            } catch (IOException x) {

            }
        }

    }

    public void LoadScores(File selectedFile, StackPane container) {
        Calculations calc = new Calculations();
        ButtonType submit = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to save this Scores?", submit, cancel);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText(null);
        alert.getDialogPane().getStylesheets().add("/styles/dialog.css");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.orElse(cancel) == submit) {
            p = new ProgressIndicator();
            p.setMaxSize(250, 250);
            veil.setStyle(
                    "-fx-background-color: rgba(255, 255, 255, 0.5);");
            veil.setPrefSize(container.getWidth(), container.getHeight());
            container.getChildren()
                    .addAll(veil, p);
            try {
                FileInputStream inputStream = new FileInputStream(selectedFile);
                Workbook workbook = new XSSFWorkbook(inputStream);

                Sheet sheet = workbook.getSheetAt(0);
                Row courseTitleRow = sheet.getRow(0);
                if (GF.verifyScoreFile(courseTitleRow)) {

                    Iterator<Row> iterator1 = sheet.iterator();
                    ObservableList<Scores> scoreData = FXCollections.observableArrayList();
                    while (iterator1.hasNext()) {
                        Row nextRow = iterator1.next();
                        Iterator<Cell> cellIterator = nextRow.iterator();
                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();
                            cell.setCellType(CellType.STRING);
                        }
                        if (nextRow != courseTitleRow) {
                            String one = "BLANK";
                            String two = "BLANK";
                            String three = "BLANK";
                            String four = "BLANK";
                            String five = "BLANK";
                            String six = "BLANK";
                            String seven = "BLANK";
                            String eight = "BLANK";
                            String nine = "BLANK";

                            if (nextRow.getCell(0) != null) {
                                one = nextRow.getCell(0) != null || nextRow.getCell(0).getCellType() != CellType.BLANK
                                        ? nextRow.getCell(0).getStringCellValue()
                                        : "Blank";
                            }
                            if (nextRow.getCell(1) != null) {
                                two = nextRow.getCell(1) != null || nextRow.getCell(1).getCellType() != CellType.BLANK
                                        ? nextRow.getCell(1).getStringCellValue()
                                        : "Blank";
                            }
                            if (nextRow.getCell(2) != null) {
                                three = nextRow.getCell(2) != null || nextRow.getCell(2).getCellType() != CellType.BLANK
                                        ? nextRow.getCell(2).getStringCellValue()
                                        : "Blank";
                            }
                            if (nextRow.getCell(3) != null) {
                                four = nextRow.getCell(3) != null || nextRow.getCell(3).getCellType() != CellType.BLANK
                                        ? nextRow.getCell(3).getStringCellValue()
                                        : "Blank";
                            }
                            if (nextRow.getCell(4) != null) {
                                five = nextRow.getCell(4) != null || nextRow.getCell(4).getCellType() != CellType.BLANK
                                        ? nextRow.getCell(4).getStringCellValue()
                                        : "Blank";
                            }
                            if (nextRow.getCell(5) != null) {
                                six = nextRow.getCell(5) != null || nextRow.getCell(5).getCellType() != CellType.BLANK
                                        ? nextRow.getCell(5).getStringCellValue()
                                        : "Blank";
                            }
                            if (nextRow.getCell(6) != null) {
                                seven = nextRow.getCell(6) != null || nextRow.getCell(6).getCellType() != CellType.BLANK
                                        ? nextRow.getCell(6).getStringCellValue()
                                        : "Blank";
                            }
                            if (nextRow.getCell(7) != null) {
                                eight = nextRow.getCell(7) != null || nextRow.getCell(7).getCellType() != CellType.BLANK
                                        ? nextRow.getCell(7).getStringCellValue()
                                        : "Blank";
                            }

                            if (nextRow.getCell(8) != null) {
                                nine = nextRow.getCell(8) != null || nextRow.getCell(8).getCellType() != CellType.BLANK
                                        ? nextRow.getCell(8).getStringCellValue()
                                        : "Blank";
                            }

                            Scores score = new Scores();
                            score.setStudentID(one);
                            score.setSt_name(two);
                            score.setSt_class(three);
                            score.setSubject(four);
                            score.setExercise(Double.parseDouble(five));
                            score.setMidTerms(Double.parseDouble(six));
                            score.setHomeWork(Double.parseDouble(seven));
                            score.setExamsScore(Double.parseDouble(eight));
                            score.setRemarks(nine);
                            score.setClassTotal(calc.getClassTotal(score.getExercise(), score.getMidTerms(), score.getHomeWork()));
                            score.setClassPercent(calc.getThirtyPercent(score.getClassTotal()));
                            score.setExamsPercent(calc.getSeventyPercent(score.getExamsScore()));
                            score.setOverAllTotal(calc.getTotalGrade(score.getExamsPercent(), score.getClassPercent()));
                            score.setGrade(calc.getLetterGrade(score.getOverAllTotal()));
                            score.setId(String.valueOf((score.getSt_name() + score.getSubject()).hashCode()));
                            scoreData.add(score);

                        }

                    }

                    ObservableList<Scores> newList = calc.getPosition(scoreData);
                    SaveScoreToDataBase(newList);
                    container.getChildren().removeAll(veil, p);
                    GF.inforAlert("Save", "Scores Saved Successfully",
                            Alert.AlertType.INFORMATION);

                } else {
                    GF.inforAlert("Invalid File", "The selected Excel file do not match stated parameters",
                            Alert.AlertType.ERROR);
                }

            } catch (FileNotFoundException ex) {

                GF.inforAlert("File Error", ex.getMessage(), Alert.AlertType.ERROR);

            } catch (IOException ex) {

                GF.inforAlert("File Error", ex.getMessage(), Alert.AlertType.ERROR);
            }

        }

    }

    private ArrayList<Scores> SaveScoreToDataBase(ObservableList<Scores> scoreData) {
        ArrayList<Scores> doNotExist = new ArrayList<>();
        for (Scores score : scoreData) {
            if (MS.saveScore(score)) {

            } else {
                doNotExist.add(score);
            }
        }
        return doNotExist;
    }

    public void createStudentExcelTemplate(StackPane container) {
        final String filename = new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "/studentList.xlsx";
        XSSFWorkbook workbook = new XSSFWorkbook();
        Service<File> service = new Service<File>() {
            @Override
            protected Task<File> createTask() {
                return new Task<File>() {
                    @Override
                    protected File call() throws Exception {
                        try {
                            File file = new File(filename);
                            if (file.exists()) {
                                file.delete();
                            }

                            XSSFSheet sheet = workbook.createSheet("FirstSheet");

                            XSSFRow rowhead = sheet.createRow((short) 0);
                            XSSFCellStyle style = workbook.createCellStyle();
                            XSSFFont font = workbook.createFont();
                            font.setFontName(HSSFFont.FONT_ARIAL);
                            font.setFontHeightInPoints((short) 10);
                            style.setFillBackgroundColor(IndexedColors.DARK_BLUE.getIndex());
                            font.setBold(true);
                            style.setFont(font);
                            int width = 25;
                            sheet.setDefaultColumnWidth(width);
                            rowhead.createCell(0).setCellValue("STUDENT NAME");
                            rowhead.createCell(1).setCellValue("GENDER");
                            rowhead.createCell(2).setCellValue("CLASS");
                            rowhead.createCell(3).setCellValue("GUARDIAN EMAIL(OPTIONAL)");
                            rowhead.createCell(4).setCellValue("SUBJECTS(Seperate with (,))");

                            for (int x = 0; x < 5; x++) {
                                rowhead.getCell(x).setCellStyle(style);
                            }

                            fileOut = new FileOutputStream(filename);
                            workbook.write(fileOut);

                        } catch (IOException ex) {
                            GF.inforAlert("Error", ex.getMessage(), Alert.AlertType.ERROR);
                        } finally {
                            try {
                                fileOut.close();
                                workbook.close();
                            } catch (IOException x) {

                            }
                        }
                        return null;
                    }
                };
            }
        };

        p = new ProgressIndicator();

        p.setMaxSize(
                250, 250);
        veil.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.5);");
        veil.setPrefSize(container.getWidth(), container.getHeight());
        p.progressProperty()
                .bind(service.progressProperty());
        container.getChildren()
                .addAll(veil, p);
        service.setOnSucceeded(
                (WorkerStateEvent event) -> {
                    container.getChildren().removeAll(veil, p);
                    GF.inforAlert("Dowloading Completed", "File downloaded Successfull,\nLocate file (studentList.xlsx) in My Document folder", Alert.AlertType.INFORMATION);
                    Desktop desktop = Desktop.getDesktop();
                    File export = new File(filename);
                    if (export.exists()) {
                        try {
                            desktop.open(export);
                        } catch (IOException ex) {

                        }
                    }
                }
        );
        service.start();

    }

    public void LoadStudents(File selectedFile, StackPane container) {
        ButtonType submit = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to save this Students ?", submit, cancel);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText(null);
        alert.getDialogPane().getStylesheets().add("/styles/dialog.css");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.orElse(cancel) == submit) {

            FileInputStream inputStream = null;
            Config config = MA.getConfig();
            try {
                inputStream = new FileInputStream(selectedFile);
                Workbook newBook = new XSSFWorkbook(inputStream);
                Sheet sheet = newBook.getSheetAt(0);
                Row courseTitleRow = sheet.getRow(0);
                if (GF.verifyStudentFile(courseTitleRow)) {
                    Iterator<Row> iterator1 = sheet.iterator();
                    ObservableList<Students> studentData = FXCollections.observableArrayList();
                    while (iterator1.hasNext()) {
                        Row nextRow = iterator1.next();
                        Iterator<Cell> cellIterator = nextRow.iterator();
                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();
                            cell.setCellType(CellType.STRING);
                        }
                        if (nextRow != courseTitleRow) {
                            String titleFour = "";
                            String titleFive = "";
                            String titleOne = "";
                            String titleTwo = "";
                            String titleThree = "";
                            if (nextRow.getCell(0) != null) {
                                titleOne = nextRow.getCell(0) != null || nextRow.getCell(0).getCellType() != CellType.BLANK
                                        ? nextRow.getCell(0).getStringCellValue()
                                        : "Blank";
                            }
                            if (nextRow.getCell(1) != null) {
                                titleTwo = nextRow.getCell(1) != null || nextRow.getCell(1).getCellType() != CellType.BLANK
                                        ? nextRow.getCell(1).getStringCellValue()
                                        : "Blank";
                            }
                            if (nextRow.getCell(2) != null) {
                                titleThree = nextRow.getCell(2) != null || nextRow.getCell(2).getCellType() != CellType.BLANK
                                        ? nextRow.getCell(2).getStringCellValue()
                                        : "Blank";
                            }
                            if (nextRow.getCell(3) != null) {
                                titleFour = nextRow.getCell(3) != null || nextRow.getCell(3).getCellType() != CellType.BLANK
                                        ? nextRow.getCell(3).getStringCellValue()
                                        : "Blank";
                            }
                            if (nextRow.getCell(4) != null) {
                                titleFive = nextRow.getCell(4) != null || nextRow.getCell(4).getCellType() != CellType.BLANK
                                        ? nextRow.getCell(4).getStringCellValue()
                                        : "Blank";
                            }

                            Students student = new Students();
                            student.setCreatedAt(Instant.now());

                            student.setStudentName(titleOne);
                            student.setStudentGender(titleTwo);
                            student.setStudentClass(titleThree);
                            student.setGuardianEmail(titleFour);
                            List<String> subjectsList = new ArrayList<>();
                            for (String sub : titleFive.split(",")) {
                                if (GF.containsCaseInsensitive(sub, config.getSubjects())) {
                                    subjectsList.add(GF.toTitleCase(sub));
                                }
                            }
                            student.setStudentSubjects(subjectsList);
                            studentData.add(student);
                        }

                    }

                    SaveStudents(studentData);
                    GF.inforAlert("Save", studentData.size() + " Students Saved Successfully",
                            Alert.AlertType.INFORMATION);

                } else {
                    GF.inforAlert("Invalid File", "The selected Excel file do not match the student template",
                            Alert.AlertType.ERROR);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ExcellServices.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ExcellServices.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(ExcellServices.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    private void SaveStudents(ObservableList<Students> studentData) {
        MongoDatabase database = MA.databaseConnection().getDatabase(MA.getDatabasename());
        MongoCollection<Document> dataCollection = database.getCollection("Students");
        for (Students student : studentData) {
            short id = (short) student.getStudentName().toLowerCase().hashCode();
            student.setId("ARG" + String.valueOf(id));
            if (StudentAlreadyExist(student.getId())) {
                dataCollection.insertOne(student.toBsonDocs());
            }
        }
    }

    private boolean StudentAlreadyExist(String id) {
        MongoDatabase database = MS.databaseConnection().getDatabase(MA.getDatabasename());
        MongoCollection<Document> dataCollection = database.getCollection("Students");
        Document data = dataCollection.find(new Document("id", id)).first();
        return data == null || data.isEmpty();
    }
}
