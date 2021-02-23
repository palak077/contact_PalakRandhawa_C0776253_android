package com.example.contact_palakrandhawa_c0776254_android;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Person.class}, version = 1, exportSchema = false)
public abstract class PersonRoomDB extends RoomDatabase {

    private static final String DB_NAME = "contact_database";

    public abstract PersonDao personDao();

    private static volatile PersonRoomDB INSTANCE;

    public static PersonRoomDB getINSTANCE(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), PersonRoomDB.class, DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }

        return INSTANCE;

    }
}
