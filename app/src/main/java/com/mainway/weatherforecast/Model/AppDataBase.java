package com.mainway.weatherforecast.Model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(version = 1,exportSchema = false,entities = {Weather.class})
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase appDataBase;
    public static AppDataBase getAppDataBase(Context context){
        if (appDataBase==null)
            appDataBase= Room.databaseBuilder(context,AppDataBase.class,"weather.db")
                    .allowMainThreadQueries()
                    .build();
        return appDataBase;
    }

    public abstract WeatherDao getWeatherDao();
}
