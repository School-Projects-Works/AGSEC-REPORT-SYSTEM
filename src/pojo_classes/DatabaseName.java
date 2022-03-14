/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojo_classes;

import java.time.Year;

/**
 *
 * @author emman
 */
public class DatabaseName {
    
    private String database;

    public DatabaseName() {
        this.database=Year.now().toString();
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
    
}
