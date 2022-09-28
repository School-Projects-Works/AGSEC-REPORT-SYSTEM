/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojo_classes;

import java.time.Instant;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author emman
 */
public class Config {
    
    String id;
    String year;
    String term;
    String adminId;
    String adminPassword;
    String adminName;
    String schoolName;
    String schoolAddress;
    String schoolPhone;
    String schoolLogo;
    String signature;
    String yearTermPair;
    String secretQuestion;
    String secretAnswer;
    Instant createdAt;
    List<String> subjects;



    public Config() {
    }

    public Config(String id, String year, String term, String adminId, String adminPassword, String adminName, String schoolName, String schoolAddress, String schoolPhone, String schoolLogo, String signature, String yearTermPair, String secretQuestion, String secretAnswer, Instant createdAt, List<String> subjects) {
        this.id = id;
        this.year = year;
        this.term = term;
        this.adminId = adminId;
        this.adminPassword = adminPassword;
        this.adminName = adminName;
        this.schoolName = schoolName;
        this.schoolAddress = schoolAddress;
        this.schoolPhone = schoolPhone;
        this.schoolLogo = schoolLogo;
        this.signature = signature;
        this.yearTermPair = yearTermPair;
        this.secretQuestion = secretQuestion;
        this.secretAnswer = secretAnswer;
        this.createdAt = createdAt;
        this.subjects = subjects;
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolAddress() {
        return schoolAddress;
    }

    public void setSchoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
    }

    public String getSchoolPhone() {
        return schoolPhone;
    }

    public void setSchoolPhone(String schoolPhone) {
        this.schoolPhone = schoolPhone;
    }

    public String getSchoolLogo() {
        return schoolLogo;
    }

    public void setSchoolLogo(String schoolLogo) {
        this.schoolLogo = schoolLogo;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getYearTermPair() {
        return yearTermPair;
    }

    public void setYearTermPair(String yearTermPair) {
        this.yearTermPair = yearTermPair;
    }

    public String getSecretQuestion() {
        return secretQuestion;
    }

    public void setSecretQuestion(String secretQuestion) {
        this.secretQuestion = secretQuestion;
    }

    public String getSecretAnswer() {
        return secretAnswer;
    }

    public void setSecretAnswer(String secretAnswer) {
        this.secretAnswer = secretAnswer;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }


    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }


    public Document toDocument() {
        Document doc = new Document();
        doc.append("id", id);
        doc.append("year", year);
        doc.append("term", term);
        doc.append("adminId", adminId);
        doc.append("adminPassword", adminPassword);
        doc.append("adminName", adminName);
        doc.append("schoolName", schoolName);
        doc.append("schoolAddress", schoolAddress);
        doc.append("schoolPhone", schoolPhone);
        doc.append("schoolLogo", schoolLogo);
        doc.append("signature", signature);
        doc.append("yearTermPair", yearTermPair);
        doc.append("secretQuestion", secretQuestion);
        doc.append("secretAnswer", secretAnswer);
        doc.append("createdAt", createdAt);
        doc.append("subjects", subjects);
        return doc;
    }


    public  Config fromDocument(Document doc) {
        Config config = new Config();
        config.setId(doc.getString("id"));
        config.setYear(doc.getString("year"));
        config.setTerm(doc.getString("term"));
        config.setAdminId(doc.getString("adminId"));
        config.setAdminPassword(doc.getString("adminPassword"));
        config.setAdminName(doc.getString("adminName"));
        config.setSchoolName(doc.getString("schoolName"));
        config.setSchoolAddress(doc.getString("schoolAddress"));
        config.setSchoolPhone(doc.getString("schoolPhone"));
        config.setSchoolLogo(doc.getString("schoolLogo"));
        config.setSignature(doc.getString("signature"));
        config.setYearTermPair(doc.getString("yearTermPair"));
        config.setSecretQuestion(doc.getString("secretQuestion"));
        config.setSecretAnswer(doc.getString("secretAnswer"));
        config.setCreatedAt(doc.getDate("createdAt").toInstant());
        config.setSubjects((List<String>) doc.get("subjects"));
        return config;
    }


    
    
    

}
