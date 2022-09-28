/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package global_classes;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pojo_classes.Config;
import pojo_classes.MapClass;
import pojo_classes.Report;
import pojo_classes.Scores;
import pojo_classes.Students;

/**
 *
 * @author emman
 */
public class PDFGeneration {

    MongodbServices MS = new MongodbServices();
    MongoDbAdmin MA = new MongoDbAdmin();
    GlobalFuncions gf = new GlobalFuncions();
    private ObservableList<Scores> scores;
    private ObservableList<Students> students;
    private ObservableList<MapClass> mapClass;

    public void createFiles() throws IOException {
        scores = FXCollections.observableArrayList();
        students = FXCollections.observableArrayList();
        mapClass = FXCollections.observableArrayList();

        scores = MS.getAllScores();
        students = MS.getAllStudents();
        Config cofig = MA.getConfig();
        Path path = Paths.get("Reports");
        Files.createDirectories(path);
        for (Students student : students) {
            ObservableList<Scores> setudentScore = FXCollections.observableArrayList();
            MapClass map = new MapClass();
            double total = 0.0;
            for (Scores score : scores) {
                if (student.getId().equals(score.getStudentID())) {
                    setudentScore.add(score);
                    total += score.getOverAllTotal();
                }
            }
            map.setTotal(total);
            map.setScore(setudentScore);
            map.setStudent(student);
            mapClass.add(map);

        }

        generatePositionPDF(mapClass, cofig);

    }

    private void generatePDFFlie(ObservableList<Scores> setudentScore, Config config, Students student, String position) {

        String filename = "Reports/" + setudentScore.get(0).getSt_name() + ".pdf";
        Document document = new Document(PageSize.A4, 10f, 10f, 50f, 50f);
        Image img = null;
        Font headerFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD, new BaseColor(15, 52, 96));
        Font headerFont2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, Font.NORMAL, new BaseColor(15, 52, 96));
        Font contactFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 11, Font.NORMAL, new BaseColor(15, 52, 96));
        Font studentInfoStyle = FontFactory.getFont(FontFactory.TIMES_BOLD, 11, Font.NORMAL, BaseColor.GRAY);
        Font studentInfoValue = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, Font.BOLD, BaseColor.BLACK);
        Font resultsInfo = FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.BOLD, BaseColor.BLACK);
        Font resultsValue = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK);
        Font remarks = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8, Font.NORMAL, BaseColor.BLACK);

        Font classPosition = FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.NORMAL, new BaseColor(15, 52, 96));
        Font classPositionVal = FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, Font.BOLD, new BaseColor(15, 52, 96));
        PdfPTable header;
        PdfPTable contact;
        PdfPTable studentInfo;
        PdfPTable mainInfo;
        PdfPTable positioTable;
        PdfPTable resultsSection;
        try {
            PdfPCell blanck = new PdfPCell(new Phrase(Chunk.NEWLINE));
            blanck.setBorder(0);
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();
            List<Chunk> headerText = new ArrayList<>();
            headerText.add(new Chunk(config.getSchoolName().toUpperCase(), headerFont));
            headerText.add(Chunk.NEWLINE);
            headerText.add(new Chunk("END OF " + config.getTerm().toUpperCase() + " REPORT", headerFont2));
            if (config.getSchoolLogo() != null && !config.getSchoolLogo().isEmpty()) {
                if (new File("Config/schoolLogo.png").exists()) {
                    Path path = Paths.get("Config/schoolLogo.png");
                    img = Image.getInstance(path.toAbsolutePath().toString());
                    float width = img.getWidth() * 0.2f;
                    float height = img.getHeight() * 0.2f;
                    img.scaleToFit(width, height);
                    img.scaleAbsolute(width, height);
                }
            }

            if (img != null) {
                float[] columnWidths = new float[]{5f, 50f, 5f};
                header = new PdfPTable(3);
                header.setWidths(columnWidths);
                for (int k = 0; k < 3; k++) {
                    if (k == 0) {
                        for (int i = 0; i < 3; i++) {
                            PdfPCell headerCell = new PdfPCell();
                            headerCell.setBorder(0);
                            if (i == 1) {
                                headerCell.setPhrase(new Phrase(headerText.get(0)));
                                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            } else {
                                headerCell.setImage(img);
                            }
                            header.addCell(headerCell);
                        }
                    } else if (k == 2) {
                        for (int i = 0; i < 3; i++) {
                            PdfPCell headerCell = new PdfPCell();
                            headerCell.setBorder(0);
                            if (i == 1) {
                                headerCell.setPhrase(new Phrase(headerText.get(2)));
                                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);

                            } else {
//                                headerCell.setImage(img);
                            }
                            header.addCell(headerCell);
                        }

                    }
                }

            } else {
                header = new PdfPTable(1);

                PdfPCell headerCell = new PdfPCell();
                PdfPCell headerCell2 = new PdfPCell();
                PdfPCell headerCell3 = new PdfPCell();

                headerCell.setPhrase(new Phrase(headerText.get(0)));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell2.setPhrase(new Phrase(headerText.get(1)));
                headerCell3.setPhrase(new Phrase(headerText.get(2)));

                header.addCell(headerCell);
                header.addCell(headerCell2);
                header.addCell(headerCell3);
            }
            document.add(header);

            //create contact address=====================================================
            contact = new PdfPTable(1);
            PdfPCell contactCell = new PdfPCell(new Phrase(new Chunk(config.getSchoolAddress() + " Tel:" + config.getSchoolPhone(), contactFont)));
            contactCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            contact.addCell(contactCell);
            document.add(contact);

            //Student in formation
            studentInfo = new PdfPTable(4);
            positioTable = new PdfPTable(1);
            float[] mainInfoColumn = new float[]{65f, 20f};
            mainInfo = new PdfPTable(2);
            mainInfo.setWidths(mainInfoColumn);

            PdfPCell infoKey1 = new PdfPCell(new Phrase(new Chunk("Student ID: ", studentInfoStyle)));
            infoKey1.setBorder(0);
            infoKey1.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell infoValue1 = new PdfPCell(new Phrase(new Chunk(student.getId(), studentInfoValue)));
            infoValue1.setBorder(0);
            infoValue1.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell infoKey2 = new PdfPCell(new Phrase(new Chunk("Student Name: ", studentInfoStyle)));
            infoKey2.setBorder(0);
            infoKey2.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell infoValue2 = new PdfPCell(new Phrase(new Chunk(student.getStudentName(), studentInfoValue)));
            infoValue2.setBorder(0);
            infoValue2.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell infoKey3 = new PdfPCell(new Phrase(new Chunk("Class: ", studentInfoStyle)));
            infoKey3.setBorder(0);
            infoKey3.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell infoValue3 = new PdfPCell(new Phrase(new Chunk(student.getStudentClass(), studentInfoValue)));
            infoValue3.setBorder(0);
            infoValue3.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell infoKey4 = new PdfPCell(new Phrase(new Chunk("Gender: ", studentInfoStyle)));
            infoKey4.setBorder(0);
            infoKey4.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell infoValue4 = new PdfPCell(new Phrase(new Chunk(student.getStudentGender(), studentInfoValue)));
            infoValue4.setBorder(0);
            infoValue3.setHorizontalAlignment(Element.ALIGN_LEFT);
            studentInfo.addCell(blanck);
            studentInfo.addCell(blanck);
            studentInfo.addCell(blanck);
            studentInfo.addCell(blanck);
            studentInfo.addCell(infoKey1);
            studentInfo.addCell(infoValue1);
            studentInfo.addCell(infoKey4);
            studentInfo.addCell(infoValue4);
            studentInfo.addCell(blanck);
            studentInfo.addCell(blanck);
            studentInfo.addCell(blanck);
            studentInfo.addCell(blanck);
            studentInfo.addCell(infoKey2);
            studentInfo.addCell(infoValue2);
            studentInfo.addCell(infoKey3);
            studentInfo.addCell(infoValue3);

            PdfPCell positionInfo = new PdfPCell(new Phrase(new Chunk("Class Position", classPosition)));
            PdfPCell positionVal = new PdfPCell(new Phrase(new Chunk(position, classPositionVal)));
            positionVal.setBorder(0);
            positionInfo.setBorder(0);
            positionInfo.setHorizontalAlignment(Element.ALIGN_CENTER);
            positionVal.setHorizontalAlignment(Element.ALIGN_CENTER);
            positioTable.addCell(positionInfo);

            positioTable.addCell(positionVal);

            PdfPCell detailCell = new PdfPCell(studentInfo);
            detailCell.setBorder(0);
            PdfPCell positionCell = new PdfPCell(positioTable);
            positionCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            mainInfo.addCell(blanck);
            mainInfo.addCell(blanck);

            mainInfo.addCell(detailCell);
            mainInfo.addCell(positionCell);

            document.add(mainInfo);
            //Results section================================================
            resultsSection = new PdfPTable(9);
            float[] resultsSectionColumns = new float[]{50f, 15f, 15f, 15f, 15f, 15f, 15f, 20f, 50f};
            resultsSection.setWidths(resultsSectionColumns);
            PdfPCell resultsHeader1 = new PdfPCell(new Phrase(new Chunk("Subject", resultsInfo)));
            PdfPCell resultsHeader2 = new PdfPCell(new Phrase(new Chunk("CA", resultsInfo)));
            resultsHeader2.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell resultsHeader3 = new PdfPCell(new Phrase(new Chunk("30%", resultsInfo)));
            resultsHeader3.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell resultsHeader4 = new PdfPCell(new Phrase(new Chunk("Exam", resultsInfo)));
            resultsHeader4.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell resultsHeader5 = new PdfPCell(new Phrase(new Chunk("70%", resultsInfo)));
            resultsHeader5.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell resultsHeader6 = new PdfPCell(new Phrase(new Chunk("Total", resultsInfo)));
            resultsHeader6.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell resultsHeader7 = new PdfPCell(new Phrase(new Chunk("Grade", resultsInfo)));
            resultsHeader7.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell resultsHeader8 = new PdfPCell(new Phrase(new Chunk("Position", resultsInfo)));
            resultsHeader8.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell resultsHeader9 = new PdfPCell(new Phrase(new Chunk("Remark", resultsInfo)));

            resultsSection.addCell(blanck);
            resultsSection.addCell(blanck);
            resultsSection.addCell(blanck);
            resultsSection.addCell(blanck);
            resultsSection.addCell(blanck);
            resultsSection.addCell(blanck);
            resultsSection.addCell(blanck);
            resultsSection.addCell(blanck);
            resultsSection.addCell(blanck);

            resultsSection.addCell(resultsHeader1);
            resultsSection.addCell(resultsHeader2);
            resultsSection.addCell(resultsHeader3);
            resultsSection.addCell(resultsHeader4);
            resultsSection.addCell(resultsHeader5);
            resultsSection.addCell(resultsHeader6);
            resultsSection.addCell(resultsHeader7);
            resultsSection.addCell(resultsHeader8);
            resultsSection.addCell(resultsHeader9);

            for (Scores score : setudentScore) {
                PdfPCell resultsCell1 = new PdfPCell(new Phrase(new Chunk(score.getSubject(), resultsValue)));
                PdfPCell resultsCell2 = new PdfPCell(new Phrase(new Chunk(String.valueOf(score.getClassTotal()), resultsValue)));
                resultsCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell resultsCell3 = new PdfPCell(new Phrase(new Chunk(String.valueOf(score.getClassPercent()), resultsValue)));
                resultsCell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell resultsCell4 = new PdfPCell(new Phrase(new Chunk(String.valueOf(score.getExamsScore()), resultsValue)));
                resultsCell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell resultsCell5 = new PdfPCell(new Phrase(new Chunk(String.valueOf(score.getExamsPercent()), resultsValue)));
                resultsCell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell resultsCell6 = new PdfPCell(new Phrase(new Chunk(String.valueOf(score.getOverAllTotal()), resultsValue)));
                resultsCell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell resultsCell7 = new PdfPCell(new Phrase(new Chunk(score.getGrade(), resultsValue)));
                resultsCell7.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell resultsCell8 = new PdfPCell(new Phrase(new Chunk(score.getPosition(), resultsValue)));
                resultsCell8.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell resultsCell9 = new PdfPCell(new Phrase(new Chunk(score.getRemarks(), remarks)));
                resultsSection.addCell(resultsCell1);
                resultsSection.addCell(resultsCell2);
                resultsSection.addCell(resultsCell3);
                resultsSection.addCell(resultsCell4);
                resultsSection.addCell(resultsCell5);
                resultsSection.addCell(resultsCell6);
                resultsSection.addCell(resultsCell7);
                resultsSection.addCell(resultsCell8);
                resultsSection.addCell(resultsCell9);

            }
            document.add(resultsSection);
            //headmater sign=====================================================

            PdfPTable headmater = new PdfPTable(1);
            headmater.addCell(blanck);
            headmater.addCell(blanck);
            headmater.addCell(blanck);
            headmater.addCell(blanck);
            headmater.addCell(blanck);
            PdfPCell signature = new PdfPCell(new Phrase(new Chunk("....................................................")));
            signature.setHorizontalAlignment(Element.ALIGN_RIGHT);
            signature.setBorder(0);
            PdfPCell name = new PdfPCell(new Phrase(new Chunk("Name:....................................................")));
            name.setHorizontalAlignment(Element.ALIGN_RIGHT);
            name.setBorder(0);
            PdfPCell title = new PdfPCell(new Phrase(new Chunk("(HEADMASTER) "+" "+" ")));
            title.setHorizontalAlignment(Element.ALIGN_RIGHT);
            title.setBorder(0);

            headmater.addCell(signature);
            headmater.addCell(blanck);
            headmater.addCell(name);
            headmater.addCell(title);
            document.add(headmater);

            document.close();
            
            Report report=new Report();
            report.setEmail(student.getGuardianEmail());
            report.setName(student.getStudentName());
            report.setId(student.getId());
            report.setNumOfSubjects(String.valueOf(setudentScore.size()));
            report.setPath(filename);
            report.setStudentClass(student.getStudentClass());
            
            saveReport(report);
                      

        } catch (FileNotFoundException | DocumentException ex) {
            Logger.getLogger(PDFGeneration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PDFGeneration.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void generatePositionPDF(ObservableList<MapClass> mapClass, Config cofig) {
        Collections.sort(mapClass, (MapClass o1, MapClass o2) -> Integer.valueOf(round(o1.getTotal())).compareTo(round(o2.getTotal())));
        FXCollections.reverse(mapClass);
        for (int i = 0; i < mapClass.size(); i++) {
            String position = String.valueOf(i + 1) + " th";
            String n = String.valueOf(i + 1);
            switch (n.charAt(n.length() - 1)) {
                case '1':
                    position = n + " st";
                    break;
                case '2':
                    position = n + " nd";
                    break;
                case '3':
                    position = n + " rd";
                    break;
                default:
                    break;
            }
            if (n.length() > 1 && n.charAt(0) == '1') {
                position = n + " th";
            }
            mapClass.get(i).setPosition(position);
        }
        for (MapClass map : mapClass) {
            generatePDFFlie(map.getScore(), cofig, map.getStudent(), map.getPosition());
        }

    }

    private int round(double d) {
        double dAbs = Math.abs(d);
        int i = (int) dAbs;
        double result = dAbs - (double) i;
        if (result < 0.5) {
            return d < 0 ? -i : i;
        } else {
            return d < 0 ? -(i + 1) : i + 1;
        }
    }

    private void saveReport(Report report) {       
        MongoDatabase database = MS.databaseConnection().getDatabase(MA.getDatabasename());
        MongoCollection<org.bson.Document> student = database.getCollection("Reports");      
        org.bson.Document data = student.find(new org.bson.Document("id", report.getId())).first();    
            if (data == null || data.isEmpty()) {
                student.insertOne(report.toBsonDocs());
            }else{
                student.findOneAndDelete(new org.bson.Document("id", report.getId()));
                student.insertOne(report.toBsonDocs());
            }
         

    }
   

}
