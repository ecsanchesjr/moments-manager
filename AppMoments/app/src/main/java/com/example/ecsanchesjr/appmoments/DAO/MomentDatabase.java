package com.example.ecsanchesjr.appmoments.DAO;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.ecsanchesjr.appmoments.Class.Moment;

@Database(entities = {Moment.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class MomentDatabase extends RoomDatabase {
    public abstract MomentDAO momentDao();

    private static MomentDatabase INSTANCE;

    public static MomentDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MomentDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MomentDatabase.class, "Moments_Database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
