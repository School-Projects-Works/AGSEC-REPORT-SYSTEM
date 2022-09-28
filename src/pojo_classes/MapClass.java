/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojo_classes;

import javafx.collections.ObservableList;

/**
 *
 * @author emman
 */
public class MapClass {
    ObservableList<Scores> score;
    String position;
    Students student;
    double total;

   
    
    public MapClass() {
       
    }

    public ObservableList<Scores> getScore() {
        return score;
    }

    public void setScore(ObservableList<Scores> score) {
        this.score = score;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Students getStudent() {
        return student;
    }

    public void setStudent(Students student) {
        this.student = student;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public MapClass(ObservableList<Scores> score, String position,Students student, double total) {
        this.score = score;
        this.position = position;
        this.student = student;
        this.total = total;
    }

   

    
    
}
