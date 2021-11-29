package com.mainway.weatherforecast.Model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface WeatherDao {

    
    @Query("SELECT * FROM FAVORITE_WEATHERS")
    List<Weather> getFavoriteWeathers();

    @Insert
    long addFavoriteWeather(Weather weather);

    @Delete
    int deleteWeather(Weather weather);

    @Update
    int update(Weather weather);

}
