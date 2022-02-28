package pojo_classes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author emman
 */
public class Students {

    String id;
    String studentName;
    String studentClass;
    String studentHouse;
    String studentGender;
    List<String> studentSubjects;
    Instant createdAt;

    public Students(String id, String studentName, String studentClass, String studentHouse, String studentGender, List<String> studentSubjects, Instant createdAt) {
        this.id = id;
        this.studentName = studentName;
        this.studentClass = studentClass;
        this.studentHouse = studentHouse;
        this.studentGender = studentGender;
        this.studentSubjects = studentSubjects;
        this.createdAt = createdAt;
    }

    public Students() {
         // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public String getStudentHouse() {
        return studentHouse;
    }

    public void setStudentHouse(String studentHouse) {
        this.studentHouse = studentHouse;
    }

    public String getStudentGender() {
        return studentGender;
    }

    public void setStudentGender(String studentGender) {
        this.studentGender = studentGender;
    }

    public List<String> getStudentSubjects() {
        return studentSubjects;
    }

    public void setStudentSubjects(List<String> studentSubjects) {
        this.studentSubjects = studentSubjects;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Document toBsonDocs() {
        Document doc = new Document("_id", new ObjectId());
        
        doc.append("id", this.id)
                .append("createdAt", this.createdAt)
                .append("studentClass", this.studentClass)
                .append("studentGender", this.studentGender)
                .append("studentHouse", this.studentHouse)
                .append("studentName", this.studentName)
                .append("studentSubjects",this.studentSubjects);

        return doc;
    }
    
   public Students fromDocument(Document doc){
       Students student=new Students();
        student.createdAt=(Instant) doc.get("");
        student.id=doc.getString("id");
        student.studentClass=doc.getString("studentClass");
        student.studentGender=doc.getString("studentGender");
        student.studentHouse=doc.getString("studentHouse");
        student.studentName=doc.getString("studentName");
        student.studentSubjects=(List<String>)doc.get("studentSubjects");
        return student;
    }
}
