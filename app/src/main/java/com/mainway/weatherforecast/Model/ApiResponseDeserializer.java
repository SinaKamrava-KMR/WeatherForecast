package com.mainway.weatherforecast.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class ApiResponseDeserializer implements JsonDeserializer<Weather> {


    @Override
    public Weather deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Weather weather=new Weather();
        JsonObject jsonObject=json.getAsJsonObject();
        JsonObject coord=jsonObject.getAsJsonObject("coord");
        JsonArray weatherArray=jsonObject.getAsJsonArray("weather");
        JsonObject main=jsonObject.getAsJsonObject("main");
        JsonObject wind=jsonObject.getAsJsonObject("wind");
        JsonObject clouds=jsonObject.getAsJsonObject("clouds");
        JsonObject sys=jsonObject.getAsJsonObject("sys");
        int id =jsonObject.get("id").getAsInt();
        long date =jsonObject.get("dt").getAsLong();
        long timezone =jsonObject.get("timezone").getAsLong();
        String cityName=jsonObject.get("name").getAsString();
        double lat=coord.get("lat").getAsDouble();
        double lon=coord.get("lon").getAsDouble();
        JsonObject weatherObject= weatherArray.get(0).getAsJsonObject();
        String state=weatherObject.get("main").getAsString();
        double temp=main.get("temp").getAsDouble();
        int pressure=main.get("pressure").getAsInt();
        int humidity=main.get("humidity").getAsInt();
        double speed=wind.get("speed").getAsDouble();
        int cloudsValue=clouds.get("all").getAsInt();
        String country=sys.get("country").getAsString();
        long sunrise=sys.get("sunrise").getAsLong();
        long sunset=sys.get("sunset").getAsLong();

        weather.setId(id);
        weather.setDt(date);
        weather.setTimezone(timezone);
        weather.setName(cityName);
        weather.setLat(lat);
        weather.setLon(lon);
        weather.setMain(state);
        weather.setTemp((int)temp);
        weather.setPressure(pressure);
        weather.setHumidity(humidity);
        weather.setSpeed(speed);
        weather.setClouds(cloudsValue);
        weather.setCountry(country);
        weather.setSunrise(sunrise);
        weather.setSunset(sunset);


        return weather;
    }
}
