/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojo_classes;

import org.bson.Document;

/**
 *
 * @author emman
 */
public class Report {
    String name;
    String email;
    String numOfSubjects;
    String studentClass;
    String id;
    String path;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumOfSubjects() {
        return numOfSubjects;
    }

    public void setNumOfSubjects(String numOfSubjects) {
        this.numOfSubjects = numOfSubjects;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Report() {
    }
  

    public Document toBsonDocs() {
        Document doc = new Document();
        doc.append("name", this.name);
        doc.append("email", this.email);
        doc.append("numOfSubjects", this.numOfSubjects);
        doc.append("studentClass", this.studentClass);
        doc.append("id", id);
        doc.append("path", this.path);
        return doc;
    }

    public Report fromDocument(Document doc) {
        this.name = doc.getString("name");
        this.email = doc.getString("email");
        this.numOfSubjects = doc.getString("numOfSubjects");
        this.studentClass = doc.getString("studentClass");
        this.id=doc.getString("id");
        this.path=doc.getString("path");
        return this;
    }
   
}
