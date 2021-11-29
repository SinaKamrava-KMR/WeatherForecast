package com.mainway.weatherforecast.Presenter.Detail;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;


import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.mainway.weatherforecast.Model.ApiService;
import com.mainway.weatherforecast.Model.Constants;
import com.mainway.weatherforecast.Model.Weather;
import com.mainway.weatherforecast.Model.WeatherDao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ShowItemPresenter implements ShowItemFragmentContract.Presenter {
    private ShowItemFragmentContract.View view;
    private WeatherDao weatherDao;
    private Activity activity;
    private ResultReceiver resultReceiver;
    private String addressFromService = null;
    private ApiService apiService;
    private CallBack callBack;
    private CompositeDisposable disposables = new CompositeDisposable();
    private static final String TAG = "getLatAndLon";


    public ShowItemPresenter(WeatherDao weatherDao, Activity activity) {
        this.weatherDao = weatherDao;
        this.activity = activity;
        callBack= (CallBack) activity;
        resultReceiver = new AddressResultReceiver(new Handler());
        apiService = new ApiService();


    }

    @Override
    public void onAttach(ShowItemFragmentContract.View view) {
        this.view = view;
        view.onClickButtons();
        Log.i(TAG, "onAttach: ");
        if (!checkPermissions()) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 111);
        }


    }

    @Override
    public void onDetach() {
        if (disposables.size() > 0) {
            disposables.clear();
            Log.i(TAG, "onDetach ShowItemPresenter: ");
        }
        callBack.onBackState(weatherDao.getFavoriteWeathers());
    }

    @Override
    public void getDataFromServer() {
        view.isAnimation(true);
        getLatAndLon()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Map<String, Double>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Map<String, Double> stringDoubleMap) {
                        view.isAnimation(false);
                        Log.i(TAG, "onSuccess: get lat and lon");
                        getFromApi(stringDoubleMap.get("lat"), stringDoubleMap.get("lon"));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.isAnimation(false);
                        Log.i(TAG, "onError: in getFromApi Method SingleObserve" + e.toString());
                    }
                });


    }

    @Override
    public void getDataFromRoomDataBase() {

    }

    @Override
    public void onClickLike(Weather weather) {
        try {
            List<Weather> weathers = weatherDao.getFavoriteWeathers();
            for (int i = 0; i < weathers.size(); i++) {
                if (weathers.get(i).getId() == weather.getId()) {
                    weatherDao.deleteWeather(weather);
                    view.likeIconState(false);
                    return;
                }
            }
            weatherDao.addFavoriteWeather(weather);
            view.likeIconState(true);


        } catch (Exception e) {
            Log.i(TAG, "onClickLike: Error " + e.toString());
        }
    }

    public void getFromApi(double lat, double lon) {
        view.isAnimation(true);
        apiService.getWeathersByLatAndLon(lat, lon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new SingleObserver<Weather>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add(d);

                    }

                    @Override
                    public void onSuccess(@NonNull Weather weather) {
                        view.isAnimation(false);
                        view.showWeatherDetail(weather, addressFromService);
                        Log.i(TAG, "onSuccess: ShowItemPresenter");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        view.isAnimation(false);
                        Log.i(TAG, "ShowItemPresenter , onError: " + e.toString());
                    }
                });

    }


    @Override
    public void checkArguments(Weather weather) {
        if (weather != null) {
            getFromApi(weather.getLat(), weather.getLon());
            Log.i(TAG, "checkArguments: weather not null  weather main has :" + weather.getMain());
            view.likeIconState(true);

        } else {
            getDataFromServer();
            view.likeIconState(false);
            Log.i(TAG, "checkArguments: weather  null  called getDataFromServer Method :");
        }
    }


    @SuppressLint("MissingPermission")
    public Single<Map<String, Double>> getLatAndLon() {

        return Single.create(emitter -> {
            if (!emitter.isDisposed()) {

                Map<String, Double> latAndLonMap = new HashMap<>();
                //===================================
                //this class must be from  com.google.android.gms.location.LocationRequest
                LocationRequest locationRequest = new LocationRequest();
                locationRequest.setInterval(1000);
                locationRequest.setFastestInterval(3000);
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


                LocationServices.getFusedLocationProviderClient(activity)
                        .requestLocationUpdates(locationRequest, new LocationCallback() {
                            @SuppressLint("MissingPermission")
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                LocationServices.getFusedLocationProviderClient(activity)
                                        .removeLocationUpdates(this);
                                if (locationResult != null && locationResult.getLocations().size() > 0) {
                                    int lastedLocationIndex = locationResult.getLocations().size() - 1;
                                    double lat = locationResult.getLocations().get(lastedLocationIndex).getLatitude();
                                    double lon = locationResult.getLocations().get(lastedLocationIndex).getLongitude();
                                    Log.i(TAG, "lat : " + lat);
                                    Log.i(TAG, "lon : " + lon);

                                    latAndLonMap.put("lat", lat);
                                    latAndLonMap.put("lon", lon);
                                    if (latAndLonMap.get("lat") != null && latAndLonMap.get("lon") != null) {
                                        emitter.onSuccess(latAndLonMap);
                                    } else {
                                        emitter.onError(new Throwable("the map is empty..."));
                                    }
                                    Location location = new Location("ProviderNA");
                                    location.setLatitude(lat);
                                    location.setLongitude(lon);
                                    fetchAddressFromLatLon(location);
                                }
                            }
                        }, Looper.getMainLooper());
                //==================================


            }


        });

    }


    public boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void fetchAddressFromLatLon(Location location) {

        Intent intent = new Intent(activity, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        Log.i(TAG, "fetchAddressFromLatLon: Start Service");
        activity.startService(intent);
    }


    private class AddressResultReceiver extends ResultReceiver {


        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.SUCCESS_RESULT) {
                addressFromService = "";
                Log.i(TAG, "Location Address : " + resultData.getString(Constants.RESULT_DATA_KEY));
                addressFromService = resultData.getString(Constants.RESULT_DATA_KEY);

            } else {
                Log.i(TAG, "Error In  AddressResultReceiver class :  " + resultData.getString(Constants.RESULT_DATA_KEY));
            }
        }
    }


    public interface CallBack{
        void onBackState(List<Weather> weathers);
    }

}
