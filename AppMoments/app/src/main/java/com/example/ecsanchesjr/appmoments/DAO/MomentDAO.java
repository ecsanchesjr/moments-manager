package com.example.ecsanchesjr.appmoments.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.ecsanchesjr.appmoments.Class.Moment;

import java.util.List;

@Dao
public interface MomentDAO {

    @Insert
    void insert(Moment moment);

    @Delete
    void delete(Moment moment);

    @Update
    void update(Moment moment);

    @Query("SELECT * FROM moment_table ORDER BY date ASC")
    List<Moment> getAllMoments();
}
