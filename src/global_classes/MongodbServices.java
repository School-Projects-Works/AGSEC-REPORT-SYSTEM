package global_classes;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import java.time.Instant;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
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

    public void createConfigurations() {
        MongoDatabase sampleTrainingDB = this.databaseConnection().getDatabase("Student_Report");
        MongoCollection<Document> adminCollection = sampleTrainingDB.getCollection("admin");
        Document admin = adminCollection.find(new Document("id", "admin".hashCode())).first();
        if (admin == null || admin.isEmpty()) {
            Document newAdmin = new Document("_id", new ObjectId());
            newAdmin.append("userName", "admin")
                    .append("password", "admin")
                    .append("id", "admin".hashCode())
                    .append("createAt", Instant.now());
            adminCollection.insertOne(newAdmin);
        }
    }

    public boolean loginAdmin(String userName, String password) {
        MongoDatabase sampleTrainingDB = this.databaseConnection().getDatabase("Student_Report");
        MongoCollection<Document> adminCollection = sampleTrainingDB.getCollection("admin");
        BasicDBObject criteria = new BasicDBObject();
        criteria.append("userName", userName);
        criteria.append("password", password);
        Document admin = adminCollection.find(criteria).first();
        return admin != null && !admin.isEmpty();

    }

    public Document updateDocument(String collection, Bson filter, Bson updates) {
        MongoDatabase sampleTrainingDB = this.databaseConnection().getDatabase("Student_Report");
        MongoCollection<Document> dataCollection = sampleTrainingDB.getCollection(collection);
        FindOneAndUpdateOptions optionAfter = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);
        return dataCollection.findOneAndUpdate(filter, updates, optionAfter);
    }

    public ObservableList<Students> saveStudent(Students data) {
        MongoDatabase database = this.databaseConnection().getDatabase("Student_Report");
        MongoCollection<Document> dataCollection = database.getCollection("students");
        dataCollection.insertOne(data.toBsonDocs());

        ArrayList<Students> listOfStudents = new ArrayList<>();
        MongoCursor<Document> cursor = dataCollection.find().iterator();
        while (cursor.hasNext()) {
            listOfStudents.add(new Students().fromDocument(cursor.next()));
        }
        return FXCollections.observableArrayList(listOfStudents);
    }

    public ObservableList<Students> getAllStudents() {
        MongoDatabase database = this.databaseConnection().getDatabase("Student_Report");
        MongoCollection<Document> dataCollection = database.getCollection("students");
        ArrayList<Students> listOfStudents = new ArrayList<>();
        MongoCursor<Document> cursor = dataCollection.find().iterator();
        while (cursor.hasNext()) {
            listOfStudents.add(new Students().fromDocument(cursor.next()));
        }
        return FXCollections.observableArrayList(listOfStudents);
        // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Document deleteStudent(String id) {
        MongoDatabase database = this.databaseConnection().getDatabase("Student_Report");
        MongoCollection<Document> dataCollection = database.getCollection("students");
       return dataCollection.findOneAndDelete(new Document("id", id));
    }

}
