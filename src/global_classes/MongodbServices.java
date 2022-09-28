package global_classes;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.io.File;
import java.time.Year;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.Document;
import pojo_classes.Report;
import pojo_classes.Scores;
import pojo_classes.Students;

/**
 *
 * @author emman
 */
public class MongodbServices {

    MongoDbAdmin MA = new MongoDbAdmin();

    public MongoClient databaseConnection() {
        String connetionPath = "mongodb://localhost:27017/?readPreference=primary&appname=MongoDB%20Compass&directConnection=true&ssl=false";
        return MongoClients.create(connetionPath);

    }

    public ObservableList<Students> saveStudent(Students student) {
        MongoDatabase database = this.databaseConnection().getDatabase(MA.getDatabasename());
        MongoCollection<Document> dataCollection = database.getCollection("Students");
        Document getStudent = dataCollection.find(new Document("id", student.getId())).first();
        if (getStudent != null && !getStudent.isEmpty()) {
            dataCollection.findOneAndReplace(new Document("id", student.getId()), student.toBsonDocs());
        } else {
            dataCollection.insertOne(student.toBsonDocs());
        }

        ArrayList<Students> listOfStudents = new ArrayList<>();
        MongoCursor<Document> cursor = dataCollection.find().iterator();
        while (cursor.hasNext()) {
            listOfStudents.add(new Students().fromDocument(cursor.next()));
        }
        return FXCollections.observableArrayList(listOfStudents);
    }

    public ObservableList<Students> getAllStudents() {
        MongoDatabase database = this.databaseConnection().getDatabase(MA.getDatabasename());
        MongoCollection<Document> dataCollection = database.getCollection("Students");
        ArrayList<Students> listOfStudents = new ArrayList<>();
        MongoCursor<Document> cursor = dataCollection.find().iterator();
        while (cursor.hasNext()) {
            listOfStudents.add(new Students().fromDocument(cursor.next()));
        }
        return FXCollections.observableArrayList(listOfStudents);
    }

    public ObservableList<Report> getReports() {
        MongoDatabase database = this.databaseConnection().getDatabase(MA.getDatabasename());
        MongoCollection<Document> dataCollection = database.getCollection("Reports");
        ArrayList<Report> listOfStudents = new ArrayList<>();
        MongoCursor<Document> cursor = dataCollection.find().iterator();
        while (cursor.hasNext()) {
            listOfStudents.add(new Report().fromDocument(cursor.next()));
        }
        return FXCollections.observableArrayList(listOfStudents);
        // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Document deleteStudent(String id) {
        MongoDatabase database = this.databaseConnection().getDatabase(MA.getDatabasename());
        MongoCollection<Document> dataCollection = database.getCollection("Students");
        return dataCollection.findOneAndDelete(new Document("id", id));
    }

    public Document deleteReport(Report report) {
        File file = new File(report.getPath());
        if (file.exists()) {
            file.delete();
        }
        MongoDatabase database = this.databaseConnection().getDatabase(MA.getDatabasename());
        MongoCollection<Document> dataCollection = database.getCollection("Reports");
        return dataCollection.findOneAndDelete(new Document("id", report.getId()));
    }

    public Document deleteScore(String id) {
        MongoDatabase database = this.databaseConnection().getDatabase(MA.getDatabasename());
        MongoCollection<Document> dataCollection = database.getCollection("Scores");
        return dataCollection.findOneAndDelete(new Document("id", id));
    }

    public ObservableList<Scores> getAllScores() {
        ArrayList<Scores> listOfScores = new ArrayList<>();
        try {
            MongoDatabase database = this.databaseConnection().getDatabase(MA.getDatabasename());
            MongoCollection<Document> dataCollection = database.getCollection("Scores");

            MongoCursor<Document> cursor = dataCollection.find().iterator();
            while (cursor.hasNext()) {
                listOfScores.add(new Scores().fromDocument(cursor.next()));
            }
        } catch (Exception error) {
            System.out.println("error=-=================================" + error.toString());

        }
        return FXCollections.observableArrayList(listOfScores);

    }

    public ObservableList<Scores> getScoresBySubject(String subject, String term) {
        MongoDatabase database = this.databaseConnection().getDatabase(MA.getDatabasename());
        MongoCollection<Document> dataCollection = database.getCollection("Scores");
        ArrayList<Scores> listOfScores = new ArrayList<>();
        BasicDBObject criteria = new BasicDBObject();
        criteria.append("subject", subject);
        criteria.append("term", term);
        MongoCursor<Document> cursor = dataCollection.find(criteria).iterator();
        while (cursor.hasNext()) {
            listOfScores.add(new Scores().fromDocument(cursor.next()));
        }
        return FXCollections.observableArrayList(listOfScores);

    }

    public boolean saveScore(Scores score) {
        MongoDatabase database = this.databaseConnection().getDatabase(MA.getDatabasename());
        MongoCollection<Document> student = database.getCollection("Students");
        MongoCollection<Document> dataCollection = database.getCollection("Scores");
        Document data = dataCollection.find(new Document("id", score.getId())).first();
        Document studentdata = student.find(new Document("studentName", score.getSt_name())).first();
        if (studentdata != null && !studentdata.isEmpty()) {
            if (data == null || data.isEmpty()) {
                dataCollection.insertOne(score.toBsonDocs());
            }
            return true;
        } else {
            return false;
        }

    }

}
