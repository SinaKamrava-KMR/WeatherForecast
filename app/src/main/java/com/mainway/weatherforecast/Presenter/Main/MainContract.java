package com.mainway.weatherforecast.Presenter.Main;

import com.mainway.weatherforecast.Model.Weather;
import com.mainway.weatherforecast.Presenter.BasePresenter;
import com.mainway.weatherforecast.Presenter.BaseView;

import java.util.List;

public interface MainContract {

    interface View extends BaseView {


        void showViewPagerItems(List<Weather> weathers);

        void showSearchResult(Weather weather);

        void showNotificationDialog();

        void showLocationFragment();
        void showWeatherFragment(Weather weather);
    }

    interface Presenter extends BasePresenter<View> {
        void onClickMenuIcon();

        void onClickWeather(Weather weather);

        void onClickViewPagerItems(Weather weather);

        void onSearch(String q);

        void onClickLocationIcon();
        void  onLongClickViewPager(Weather weather);

        void broadcastToTime();

        void setNotification();
        void isDrawerOpen(boolean value);

    }


}
