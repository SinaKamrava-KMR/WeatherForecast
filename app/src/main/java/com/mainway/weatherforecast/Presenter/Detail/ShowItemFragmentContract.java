package com.mainway.weatherforecast.Presenter.Detail;

import android.os.Bundle;

import com.mainway.weatherforecast.Model.Weather;
import com.mainway.weatherforecast.Presenter.BasePresenter;
import com.mainway.weatherforecast.Presenter.BaseView;
import com.mainway.weatherforecast.R;

public interface ShowItemFragmentContract {

    interface View extends BaseView {
        void isAnimation(boolean value);
        void showWeatherDetail(Weather weather,String address);
        void likeIconState(boolean isFavorite);

    }

    interface Presenter extends BasePresenter<View> {

        void onClickLike(Weather weather);

        void checkArguments(Weather weather);


    }
}
