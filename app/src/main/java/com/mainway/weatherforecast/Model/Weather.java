package com.mainway.weatherforecast.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_weathers")
public class Weather implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String country;
    private String name;
    private String main;
    private double lat;
    private double lon;
    private int temp;
    private long dt;
    private long timezone;
    private int clouds;
    private double speed;
    private int humidity;
    private int pressure;
    private long sunrise;
    private long sunset;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public long getTimezone() {
        return timezone;
    }

    public void setTimezone(long timezone) {
        this.timezone = timezone;
    }

    public int getClouds() {
        return clouds;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public long getSunrise() {
        return sunrise;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.country);
        dest.writeString(this.name);
        dest.writeString(this.main);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lon);
        dest.writeInt(this.temp);
        dest.writeLong(this.dt);
        dest.writeLong(this.timezone);
        dest.writeInt(this.clouds);
        dest.writeDouble(this.speed);
        dest.writeInt(this.humidity);
        dest.writeInt(this.pressure);
        dest.writeLong(this.sunrise);
        dest.writeLong(this.sunset);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.country = source.readString();
        this.name = source.readString();
        this.main = source.readString();
        this.lat = source.readDouble();
        this.lon = source.readDouble();
        this.temp = source.readInt();
        this.dt = source.readLong();
        this.timezone = source.readLong();
        this.clouds = source.readInt();
        this.speed = source.readDouble();
        this.humidity = source.readInt();
        this.pressure = source.readInt();
        this.sunrise = source.readLong();
        this.sunset = source.readLong();
    }

    public Weather() {
    }

    protected Weather(Parcel in) {
        this.id = in.readInt();
        this.country = in.readString();
        this.name = in.readString();
        this.main = in.readString();
        this.lat = in.readDouble();
        this.lon = in.readDouble();
        this.temp = in.readInt();
        this.dt = in.readLong();
        this.timezone = in.readLong();
        this.clouds = in.readInt();
        this.speed = in.readDouble();
        this.humidity = in.readInt();
        this.pressure = in.readInt();
        this.sunrise = in.readLong();
        this.sunset = in.readLong();
    }

    public static final Parcelable.Creator<Weather> CREATOR = new Parcelable.Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel source) {
            return new Weather(source);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };
}
