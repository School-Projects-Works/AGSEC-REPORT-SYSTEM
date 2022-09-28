/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package global_classes;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import pojo_classes.Config;

/**
 *
 * @author emman
 */
public class MongoDbAdmin {

    GlobalFuncions GF = new GlobalFuncions();

    public MongoClient databaseConnection() {
        String connetionPath = "mongodb://localhost:27017/?readPreference=primary&appname=MongoDB%20Compass&directConnection=true&ssl=false";
        return MongoClients.create(connetionPath);

    }



    public boolean loginAdmin(String userName, String password) {
        MongoDatabase sampleTrainingDB = this.databaseConnection().getDatabase(this.getDatabasename());
        MongoCollection<Document> adminCollection = sampleTrainingDB.getCollection("Config");
        BasicDBObject criteria = new BasicDBObject();
        criteria.append("adminName", userName);
        criteria.append("adminPassword", password);
        Document admin = adminCollection.find(criteria).first();
        return admin != null && !admin.isEmpty();

    }


    public Config getConfig() {
        Config configuration = new Config();
        String folder = "Config";
        File f = new File(folder + "/configuration.txt");
        if (f.exists() && !f.isDirectory()) {
            try {
                String databaseName;
                try ( Scanner myReader = new Scanner(f)) {
                    databaseName = "";
                    while (myReader.hasNextLine()) {
                        databaseName = myReader.nextLine();
                    }
                }

                if (!databaseName.isEmpty()) {
                    MongoDatabase sampleTrainingDB = this.databaseConnection().getDatabase(databaseName);
                    MongoCollection<Document> adminCollection = sampleTrainingDB.getCollection("Config");
                    Document config = adminCollection.find(new Document("id", this.getDatabasename())).first();
                    if (config != null || !config.isEmpty()) {
                        configuration = new Config().fromDocument(config);
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MongoDbAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                Path path = Paths.get(folder);
                Files.createDirectories(path);

            } catch (IOException ex) {
                Logger.getLogger(MongoDbAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return configuration;
    }

    public String getDatabasename() {
        String folder = "Config";
        File f = new File(folder + "/configuration.txt");
        String databaseName = "";
        if (f.exists()) {

            try ( Scanner myReader = new Scanner(f)) {

                while (myReader.hasNextLine()) {
                    databaseName = myReader.nextLine();
                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(MongoDbAdmin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return databaseName;
    }

    public boolean saveConfig(Config config) {
        try {
            String databaseName;
            if (getDatabasename().isEmpty()) {
                String folder = "Config";
                File f = new File(folder + "/configuration.txt");
                f.createNewFile();
                databaseName = String.valueOf(config.getSchoolName().trim().hashCode());
                try ( FileWriter myWriter = new FileWriter(folder + "/configuration.txt")) {
                    myWriter.write(databaseName);
                }
            } else {
                databaseName = getDatabasename();
            }
            config.setId(databaseName);
            config.setCreatedAt(Instant.now());
            MongoDatabase sampleTrainingDB = this.databaseConnection().getDatabase(databaseName);
            MongoCollection<Document> adminCollection = sampleTrainingDB.getCollection("Config");
            adminCollection.findOneAndDelete(new Document("id", config.getId()));
            adminCollection.insertOne(config.toDocument());
            return true;
        } catch (IOException ex) {
            Logger.getLogger(MongoDbAdmin.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
    }

}
