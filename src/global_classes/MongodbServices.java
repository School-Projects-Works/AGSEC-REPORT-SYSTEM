package global_classes;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.Document;
import pojo_classes.ElectiveSubjects;
import pojo_classes.Scores;
import pojo_classes.Students;

/**
 *
 * @author emman
 */
public class MongodbServices {

    public MongoClient databaseConnection() {
        String connetionPath = "mongodb://localhost:27017/?readPreference=primary&appname=MongoDB%20Compass&directConnection=true&ssl=false";
        return MongoClients.create(connetionPath);

    }

    public MongoDatabase getDataBase(String year){
      return this.databaseConnection().getDatabase(year);  
    }
   

    public ObservableList<Students> saveStudent(String dataBase,Students data) {
        MongoDatabase database = this.databaseConnection().getDatabase(dataBase);
        MongoCollection<Document> dataCollection = database.getCollection("students");
        dataCollection.insertOne(data.toBsonDocs());
        ArrayList<Students> listOfStudents = new ArrayList<>();
        MongoCursor<Document> cursor = dataCollection.find().iterator();
        while (cursor.hasNext()) {
            listOfStudents.add(new Students().fromDocument(cursor.next()));
        }
        return FXCollections.observableArrayList(listOfStudents);
    }

    public ObservableList<Students> getAllStudents(String databaseName) {
        MongoDatabase database = this.databaseConnection().getDatabase(databaseName);
        MongoCollection<Document> dataCollection = database.getCollection("students");
        ArrayList<Students> listOfStudents = new ArrayList<>();
        MongoCursor<Document> cursor = dataCollection.find().iterator();
        while (cursor.hasNext()) {
            listOfStudents.add(new Students().fromDocument(cursor.next()));
        }
        return FXCollections.observableArrayList(listOfStudents);
        // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Document deleteStudent(String databaseName,String id) {
        MongoDatabase database = this.databaseConnection().getDatabase(databaseName);
        MongoCollection<Document> dataCollection = database.getCollection("students");
       return dataCollection.findOneAndDelete(new Document("id", id));
    }
    
    public ObservableList<Scores> getAllScores(String databaseName,String term) {
        MongoDatabase database = this.databaseConnection().getDatabase(databaseName);
        MongoCollection<Document> dataCollection = database.getCollection("scores");
        ArrayList<Scores> listOfScores = new ArrayList<>();
        MongoCursor<Document> cursor = dataCollection.find(new BasicDBObject("term",term) ).iterator();
        while (cursor.hasNext()) {
            listOfScores.add(new Scores().fromDocument(cursor.next()));
        }
        return FXCollections.observableArrayList(listOfScores);
       
    }
    
     public ObservableList<Scores> getScoresBySubject(String databaseName,String subject,String term) {
        MongoDatabase database = this.databaseConnection().getDatabase(databaseName);
        MongoCollection<Document> dataCollection = database.getCollection("scores");
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
    
    
    public String saveElectiveSubject(ElectiveSubjects data) {
        MongoDatabase database = this.databaseConnection().getDatabase("Student_Report");
        MongoCollection<Document> dataCollection = database.getCollection("electives");
        Document subject = dataCollection.find(new Document("id", data.getId())).first();
        if (subject == null || subject.isEmpty()) {
        dataCollection.insertOne(data.toBsonDocs());
        return "Subject saved successfully";
        }else{
            return "Subject alredy Exist";
        }
        
    }

}


