package pojo_classes;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author emman
 */
public class Scores {

    String id;
    String st_name;
    String subject;
    String st_class;
    String term;
    double exercise;
    double midTerms;
    double homeWork;
    double classTotal;
    double classPercent;
    double examsScore;
    double examsPercent;
    double overAllTotal;
    String grade;
    String position;
    String remarks;
    String studentID;

    public Scores() {
    }

    public Scores(String id, String studentID, String st_name, String subject, String st_class, String term, double exercise, double midTerms, double homeWork, double classTotal, double classPercent, double examsScore, double examsPercent, double overAllTotal, String grade, String position, String remarks) {
        this.id = id;
        this.st_name = st_name;
        this.subject = subject;
        this.st_class = st_class;
        this.term = term;
        this.exercise = exercise;
        this.midTerms = midTerms;
        this.homeWork = homeWork;
        this.classTotal = classTotal;
        this.classPercent = classPercent;
        this.examsScore = examsScore;
        this.examsPercent = examsPercent;
        this.overAllTotal = overAllTotal;
        this.grade = grade;
        this.position = position;
        this.remarks = remarks;
        this.studentID = studentID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSt_name() {
        return st_name;
    }

    public void setSt_name(String st_name) {
        this.st_name = st_name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSt_class() {
        return st_class;
    }

    public void setSt_class(String st_class) {
        this.st_class = st_class;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public double getExercise() {
        return exercise;
    }

    public void setExercise(double exercise) {
        this.exercise = exercise;
    }

    public double getMidTerms() {
        return midTerms;
    }

    public void setMidTerms(double midTerms) {
        this.midTerms = midTerms;
    }

    public double getHomeWork() {
        return homeWork;
    }

    public void setHomeWork(double homeWork) {
        this.homeWork = homeWork;
    }

    public double getClassTotal() {
        return classTotal;
    }

    public void setClassTotal(double classTotal) {
        this.classTotal = classTotal;
    }

    public double getClassPercent() {
        return classPercent;
    }

    public void setClassPercent(double classPercent) {
        this.classPercent = classPercent;
    }

    public double getExamsScore() {
        return examsScore;
    }

    public void setExamsScore(double examsScore) {
        this.examsScore = examsScore;
    }

    public double getExamsPercent() {
        return examsPercent;
    }

    public void setExamsPercent(double examsPercent) {
        this.examsPercent = examsPercent;
    }

    public double getOverAllTotal() {
        return overAllTotal;
    }

    public void setOverAllTotal(double overAllTotal) {
        this.overAllTotal = overAllTotal;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public Document toBsonDocs() {
        Document doc = new Document("_id", new ObjectId());
        doc.append("st_name", this.st_name)
                .append("subject", this.subject)
                .append("st_class", this.st_class)
                .append("term", this.term)
                .append("exercise", this.exercise)
                .append("homeWork", this.homeWork)
                .append("midTerms", this.midTerms)
                .append("classPercent", this.classPercent)
                .append("examsPercent", this.examsPercent)
                .append("overAllTotal", this.overAllTotal)
                .append("grade", this.grade)
                .append("position", this.position)
                .append("remarks", this.remarks)
                .append("classTotal", this.classTotal)
                .append("id", this.id)
                .append("examsScore", this.examsScore)
                .append("studentID", this.studentID);

        return doc;
    }

    public Scores fromDocument(Document doc) {
        Scores score = new Scores();
        score.classPercent = doc.getDouble("classPercent");
        score.classTotal = doc.getDouble("classTotal");
        score.examsPercent = doc.getDouble("examsPercent");
        score.exercise = doc.getDouble("exercise");
        score.examsScore = doc.getDouble("examsScore");
        score.grade = doc.getString("grade");
        score.homeWork = doc.getDouble("homeWork");
        score.midTerms = doc.getDouble("midTerms");
        score.overAllTotal = doc.getDouble("overAllTotal");
        score.position = doc.getString("position");
        score.remarks = doc.getString("remarks");
        score.st_class = doc.getString("st_class");
        score.st_name = doc.getString("st_name");
        score.subject = doc.getString("subject");
        score.term = doc.getString("term");
        score.id = doc.getString("id");
        score.studentID=doc.getString("studentID");
        return score;
    }

}
