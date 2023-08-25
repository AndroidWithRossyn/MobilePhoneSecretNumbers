package com.phonesecretcode.mobilelocator.coder.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.phonesecretcode.mobilelocator.coder.R;
import com.phonesecretcode.mobilelocator.coder.models.CodesModel;

@Database(entities = {CodesModel.class}, exportSchema = false, version = 1)
public abstract class CodesDatabase extends RoomDatabase {

    private static CodesDatabase instance;

    public static synchronized CodesDatabase getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context, CodesDatabase.class, context.getString(R.string.combine_app_name))
                    .fallbackToDestructiveMigration()
                    .build();

        return instance;
    }

    public abstract CodesDao codesDao();
}
