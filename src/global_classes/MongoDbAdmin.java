/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 *
 * @author emman
 */
public class MongoDbAdmin {

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

    public ArrayList<String> getYears() {
        MongoDatabase sampleTrainingDB = this.databaseConnection().getDatabase("Student_Report");
        MongoCollection<Document> dataCollection = sampleTrainingDB.getCollection("databases");
        ArrayList<String> listOfDataBase = new ArrayList<>();
        MongoCursor<Document> cursor = dataCollection.find().iterator();
        while (cursor.hasNext()) {
            listOfDataBase.add(cursor.next().getString("year"));
        }
        
        return listOfDataBase;
    }
    public ArrayList<String> getElectiveSubjects() {
        MongoDatabase sampleTrainingDB = this.databaseConnection().getDatabase("Student_Report");
        MongoCollection<Document> dataCollection = sampleTrainingDB.getCollection("electives");
        ArrayList<String> listOfSubjects = new ArrayList<>();
        
        MongoCursor<Document> cursor = dataCollection.find().iterator();
        while (cursor.hasNext()) {
            listOfSubjects.add(cursor.next().getString("title"));
        }
        
        return listOfSubjects;
    }
    public void saveElectives(String sub){
       MongoDatabase sampleTrainingDB = this.databaseConnection().getDatabase("Student_Report");
        MongoCollection<Document> dataCollection = sampleTrainingDB.getCollection("electives"); 
        Document newSubject = new Document("_id", new ObjectId());
         short id = (short) sub.toLowerCase().hashCode();
        newSubject.append("id", "Sub"+String.valueOf(id)).append("title",sub);
        dataCollection.insertOne(newSubject);
    }
    
    

}
