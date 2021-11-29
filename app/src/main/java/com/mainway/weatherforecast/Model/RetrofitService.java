package com.mainway.weatherforecast.Model;



import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("weather?appid=c1ba56bf40bd4736029abb60f0b571f1&")
    Single<Weather> getWeathersWithLatAndLon(@Query("lat") String Latitude , @Query("lon") String Longitude);

    @GET("weather?appid=c1ba56bf40bd4736029abb60f0b571f1&")
    Single<Weather> getWeatherByCityName(@Query("q") String city);
}
