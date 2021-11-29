package com.mainway.weatherforecast.Presenter;

public interface BasePresenter<T extends BaseView> {
     void onAttach(T view);
     void onDetach();
     void getDataFromServer();

     void getDataFromRoomDataBase();
}
