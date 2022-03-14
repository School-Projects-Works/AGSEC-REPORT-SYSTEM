/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojo_classes;


import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author emman
 */
public class ElectiveSubjects {

    private String id;
    private String subject;

    public ElectiveSubjects(String id, String subject) {
        this.id = id;
        this.subject = subject;
    }

  

    public ElectiveSubjects() {
         // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

  

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Document toBsonDocs() {
        Document doc = new Document("_id", new ObjectId());
        doc.append("id", this.id)
                .append("subject", this.subject);
        return doc;
    }

    public ElectiveSubjects fromDocument(Document doc) {
        ElectiveSubjects subject = new ElectiveSubjects();
        subject.subject = doc.getString("subject");
        subject.id = doc.getString("id");

        return subject;
    }

}
