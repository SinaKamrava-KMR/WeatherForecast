package com.mainway.weatherforecast.View.Detail;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.mainway.weatherforecast.Model.AppDataBase;
import com.mainway.weatherforecast.Model.GetDate;
import com.mainway.weatherforecast.Model.Weather;
import com.mainway.weatherforecast.Presenter.Detail.ShowItemFragmentContract;
import com.mainway.weatherforecast.Presenter.Detail.ShowItemPresenter;
import com.mainway.weatherforecast.R;
import com.mainway.weatherforecast.View.Main.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ShowItemsFragment extends Fragment implements ShowItemFragmentContract.View {
    private ImageView btnBack;
    private ImageView btnFavorite;
    private LottieAnimationView findLocationAnimation;
    private LottieAnimationView showStateAnimation;
    private TextView cityName;
    private TextView stateAndDateTV;
    private TextView degreeTV;
    private TextView date;
    private TextView addressTV;
    private TextView cloudyTV;
    private TextView windTV;
    private TextView humidity;
    private TextView pressureTV;
    private TextView sunriseTV;
    private TextView sunsetTV;
    private View container;

    private boolean isCheckFavorite = false;
    private View view;

    private ShowItemPresenter showItemPresenter;
    private Context context;
    private Weather weather;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;

        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_items_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        initialize(view);
        if (weather == null) {
            isCheckFavorite = false;
        } else {
            isCheckFavorite = true;
        }

        showItemPresenter = new ShowItemPresenter(AppDataBase.getAppDataBase(context).getWeatherDao(), getActivity());
        showItemPresenter.onAttach(ShowItemsFragment.this);
        Weather weather = getArguments().getParcelable(MainActivity.EXTRA_SEND_DATA_KEY);
        showItemPresenter.checkArguments(weather);

    }

    public void initialize(View view) {
        container = view.findViewById(R.id.relative_showItemsFragment_Container);
        btnBack = view.findViewById(R.id.iv_showItemsFragment_back);
        btnFavorite = view.findViewById(R.id.iv_showItemsFragment_ISFavorite);
        findLocationAnimation = view.findViewById(R.id.animation_showItemsFragment_location);
        showStateAnimation = view.findViewById(R.id.animation_showItemsFragment_state);
        cityName = view.findViewById(R.id.tv_showItemsFragment_cityName);
        stateAndDateTV = view.findViewById(R.id.tv_showItemFragment_stateAndDate);
        addressTV = view.findViewById(R.id.tv_showItemFragment_Address);
        degreeTV = view.findViewById(R.id.tv_showItemFragment_degree);
        date = view.findViewById(R.id.tv_showItemFragment_dateValue);
        cloudyTV = view.findViewById(R.id.tv_showItemFragment_CloudyValue);
        windTV = view.findViewById(R.id.tv_showItemFragment_WindValue);
        humidity = view.findViewById(R.id.tv_showItemFragment_humidityValue);
        pressureTV = view.findViewById(R.id.tv_showItemFragment_pressureValue);
        sunriseTV = view.findViewById(R.id.tv_showItemFragment_sunriseValue);
        sunsetTV = view.findViewById(R.id.tv_showItemFragment_sunsetValue);
        if (isCheckFavorite) {
            btnFavorite.setImageResource(R.drawable.ic_favorite);
        } else {
            btnFavorite.setImageResource(R.drawable.ic_not_favorite);
        }
    }

    @Override
    public void showWeatherItem(Weather weather) {

    }

    @Override
    public void drawerState() {

    }

    @Override
    public void init() {

    }

    @Override
    public void onClickButtons() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = getParentFragmentManager().findFragmentById(R.id.frameLayoutFirst);
                if (fragment instanceof ShowItemsFragment) {
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.remove(fragment);
                    transaction.commit();
                    showItemPresenter.onDetach();

                }
            }
        });

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCheckFavorite = !isCheckFavorite;
                showItemPresenter.onClickLike(weather);
            }
        });
    }

    @Override
    public void isAnimation(boolean value) {
      try {
          findLocationAnimation.setVisibility(value ? View.VISIBLE : View.INVISIBLE);
      }catch (Exception e){
          Log.i("getLatAndLon", "isAnimation  Error  : "+e.getMessage());
      }
    }

    @Override
    public void showWeatherDetail(Weather weather, String address) {
        if (weather != null) {
            container.setVisibility(View.VISIBLE);
            this.weather = weather;
            String state = weather.getMain();
            cityName.setText(weather.getName());
            addressTV.setText(address);
            date.setText(GetDate.date(context));
            cloudyTV.setText(String.valueOf(weather.getClouds()));
            windTV.setText(String.valueOf(weather.getSpeed()));
            humidity.setText(String.valueOf(weather.getHumidity()));
            pressureTV.setText(String.valueOf(weather.getPressure()));
            sunriseTV.setText(String.valueOf(weather.getSunrise()));
            sunsetTV.setText(String.valueOf(weather.getSunset()));
            String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            stateAndDateTV.setText(state + " ," + currentTime);
            degreeTV.setText(weather.getTemp() + " C");
            if (state.toLowerCase().contains("sun") || state.toLowerCase().contains("clear")) {
                showStateAnimation.setAnimation(R.raw.clear_day);
            } else if (state.toLowerCase().contains("rain")) {
                showStateAnimation.setAnimation(R.raw.raining);
            } else if (state.toLowerCase().contains("snow")) {
                showStateAnimation.setAnimation(R.raw.cloud_plus_snow2);
            } else if (state.toLowerCase().contains("storm")) {
                showStateAnimation.setAnimation(R.raw.stormy);
            } else if (state.toLowerCase().contains("cloud") || state.toLowerCase().contains("haze")) {
                showStateAnimation.setAnimation(R.raw.cloud3);
            }
        }
    }

    @Override
    public void likeIconState(boolean isFavorite) {
        if (isFavorite) {
            btnFavorite.setImageResource(R.drawable.ic_favorite);
        } else {
            btnFavorite.setImageResource(R.drawable.ic_not_favorite);
        }
    }

    public static ShowItemsFragment getInstance(Bundle bundle) {
        ShowItemsFragment fragment = new ShowItemsFragment();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }




}
