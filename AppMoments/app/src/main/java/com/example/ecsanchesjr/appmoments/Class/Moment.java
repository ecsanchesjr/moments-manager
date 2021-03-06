package com.example.ecsanchesjr.appmoments.Class;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@Entity(tableName = "moment_table")
public class Moment implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String name;
    private String local;
    private Date date;
    private String mainImgUri;
    private String description;
    private ArrayList<String> gallery;

    public Moment(int id, String name, String local, Date date, String mainImgUri, String description, ArrayList<String> gallery) {
        this.id = id;
        this.name = name;
        this.local = local;
        this.date = date;
        this.mainImgUri = mainImgUri;
        this.description = description;
        this.gallery = gallery;
    }

    @Ignore
    public Moment(String name, String local, Date date, String mainImgUri, String description, ArrayList<String> gallery) {
        this.name = name;
        this.local = local;
        this.date = date;
        this.mainImgUri = mainImgUri;
        this.description = description;
        this.gallery = gallery;
    }

    @Ignore
    public Moment(String name, String local, Date date, String mainImgUri, String description) {
        this.name = name;
        this.local = local;
        this.date = date;
        this.mainImgUri = mainImgUri;
        this.description = description;
        this.gallery = new ArrayList<>();
    }

    @Ignore
    public Moment() {
    }

    public ArrayList<String> getGallery() {
        return gallery;
    }

    public void setGallery(ArrayList<String> gallery) {
        this.gallery = gallery;
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

    public String getMainImgUri() {
        return mainImgUri;
    }

    public void setMainImgUri(String mainImgUri) {
        this.mainImgUri = mainImgUri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
