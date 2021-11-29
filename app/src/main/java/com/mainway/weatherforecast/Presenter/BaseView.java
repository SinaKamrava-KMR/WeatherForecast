package com.mainway.weatherforecast.Presenter;

import com.mainway.weatherforecast.Model.Weather;

public interface BaseView {
    void showWeatherItem(Weather weather);
    void drawerState();
    void init();
    void onClickButtons();
}
