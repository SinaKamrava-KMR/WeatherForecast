package com.mainway.weatherforecast.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private RetrofitService retrofitService;
    public final static String BASE_URL = "https://api.openweathermap.org/data/2.5/";

    public ApiService() {

        Gson gson=new GsonBuilder()
                .registerTypeAdapter(Weather.class,new ApiResponseDeserializer())
                .create();

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        retrofitService=retrofit.create(RetrofitService.class);
    }

    public Single<Weather> getWeathersByLatAndLon(double lat , double lon){
        return retrofitService.getWeathersWithLatAndLon(String.valueOf(lat),String.valueOf(lon));
    }

    public Single<Weather> getWeatherByCityName(String cityName){
        return retrofitService.getWeatherByCityName(cityName);
    }

}
