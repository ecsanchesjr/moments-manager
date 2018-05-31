package com.example.ecsanchesjr.appmoments.Class;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "moment_table")
public class Moment implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String name;
    private String local;
    private Date date;
    private String mainImgPath;
    private String description;

    public Moment(int id, String name, String local, Date date, String mainImgPath, String description) {
        this.id = id;
        this.name = name;
        this.local = local;
        this.date = date;
        this.mainImgPath = mainImgPath;
        this.description = description;
    }

    @Ignore
    public Moment(String name, String local, Date date, String imgPath, String description) {
        this.name = name;
        this.local = local;
        this.date = date;
        this.mainImgPath = imgPath;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMainImgPath() {
        return mainImgPath;
    }

    public void setMainImgPath(String mainImgPath) {
        this.mainImgPath = mainImgPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
