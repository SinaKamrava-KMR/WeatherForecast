package com.mainway.weatherforecast.Presenter.Main;

import android.util.Log;

import androidx.annotation.NonNull;

import com.mainway.weatherforecast.Model.ApiService;
import com.mainway.weatherforecast.Model.Weather;
import com.mainway.weatherforecast.Model.WeatherDao;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View view;
    private WeatherDao weatherDao;
    private ApiService apiService = new ApiService();
    private List<Weather> weathers = new ArrayList<>();
    private List<Weather> favoriteWeathers = new ArrayList<>();
    private CompositeDisposable disposables = new CompositeDisposable();
    public static final String TAG="getWeather";

    public MainPresenter(WeatherDao weatherDao) {
        this.weatherDao = weatherDao;

    }

    @Override
    public void onAttach(MainContract.View view) {
        this.view = view;
        view.init();
        view.onClickButtons();


    }

    @Override
    public void onDetach() {
        if (disposables.size() > 0) {
            disposables.clear();
        }
    }

    @Override
    public void getDataFromServer() {
        favoriteWeathers.clear();
        List<Weather> getWeathers = weatherDao.getFavoriteWeathers();

        for (int i = 0; i < getWeathers.size(); i++) {
            Weather weather = getWeathers.get(i);
            apiService.getWeathersByLatAndLon(weather.getLat(), weather.getLon())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Weather>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            disposables.add(d);
                        }

                        @Override
                        public void onSuccess(@NonNull Weather weather) {
                            favoriteWeathers.add(weather);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }
                    });
        }
        view.showViewPagerItems(favoriteWeathers);
        if (getWeathers.size()>0){
            view.showWeatherItem(getWeathers.get(0));
        }
    }

    @Override
    public void getDataFromRoomDataBase() {
      view.showViewPagerItems(weatherDao.getFavoriteWeathers());
    }

    @Override
    public void onClickMenuIcon() {
        view.drawerState();
    }

    @Override
    public void onClickWeather(Weather weather) {
        view.showWeatherFragment(weather);
    }

    @Override
    public void onClickViewPagerItems(Weather weather) {
        view.showWeatherFragment(weather);
    }

    @Override
    public void onSearch(String q) {
        if (q.length() > 0) {
            apiService.getWeatherByCityName(q)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Weather>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            disposables.add(d);
                        }

                        @Override
                        public void onSuccess(@NonNull Weather weather) {
                            Log.i(TAG, "onSuccess: " + weather.getMain());
                           long result=-1;
                           try {
                              result= weatherDao.addFavoriteWeather(weather);
                           }catch (Exception e){
                               Log.i(TAG, "onError Catch : "+e.toString());
                           }
                           if (result>-1){
                               view.showWeatherItem(weather);
                               view.showViewPagerItems(weatherDao.getFavoriteWeathers());
                           }

                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Log.i(TAG, "onError: "+e.getMessage());
                        }
                    });
        }
    }




    @Override
    public void onClickLocationIcon() {
        view.showLocationFragment();

    }

    @Override
    public void onLongClickViewPager(Weather weather) {
        weatherDao.deleteWeather(weather);


    }

    @Override
    public void broadcastToTime() {

    }

    @Override
    public void setNotification() {

    }

    @Override
    public void isDrawerOpen(boolean value) {

    }


}
