package com.mainway.weatherforecast.View.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;
import com.mainway.weatherforecast.Model.AppDataBase;
import com.mainway.weatherforecast.Model.Weather;
import com.mainway.weatherforecast.Presenter.Detail.ShowItemPresenter;
import com.mainway.weatherforecast.Presenter.Main.MainContract;
import com.mainway.weatherforecast.Presenter.Main.MainPresenter;
import com.mainway.weatherforecast.R;
import com.mainway.weatherforecast.View.Detail.ShowItemsFragment;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity implements MainContract.View, SlidAdapter.ResultListener, ShowItemPresenter.CallBack {

    private ViewPager viewPager;
    private SlidAdapter slidAdapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MainPresenter mainPresenter;
    private ImageView iv_main_menu;
    private ImageView location;
    public static final String EXTRA_SEND_DATA_KEY = "SEND_WEATHER";
    private TextView cityName;
    private TextView tvStateAndDate;
    private TextView tvDegree;
    private ImageView iconMore;
    private EditText etSearchCity;
    private CircleIndicator indicator;
    private LottieAnimationView lottieAnimationView;
    private Weather weather = new Weather();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainPresenter = new MainPresenter(AppDataBase.getAppDataBase(this).getWeatherDao());
        mainPresenter.onAttach(this);
        mainPresenter.getDataFromServer();
        mainPresenter.getDataFromRoomDataBase();

    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public void showWeatherItem(Weather weather) {

        if (weather != null) {
            etSearchCity.setText("");
            etSearchCity.clearFocus();
            this.weather = weather;
            String state = weather.getMain();
            Log.i("getWeather", "showWeatherItem:  get name : " + weather.getName());
            cityName.setText(weather.getName());
            String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            tvStateAndDate.setText(state + "," + currentTime);
            tvDegree.setText(weather.getTemp() + " C");
            if (state.toLowerCase().contains("sun") || state.toLowerCase().contains("clear")) {
                lottieAnimationView.setAnimation(R.raw.clear_day);
            } else if (state.toLowerCase().contains("rain")) {
                lottieAnimationView.setAnimation(R.raw.raining);
            } else if (state.toLowerCase().contains("snow")) {
                lottieAnimationView.setAnimation(R.raw.cloud_plus_snow2);
            } else if (state.toLowerCase().contains("storm")) {
                lottieAnimationView.setAnimation(R.raw.stormy);
            } else if (state.toLowerCase().contains("cloud") || state.toLowerCase().contains("haze")) {
                lottieAnimationView.setAnimation(R.raw.cloud3);
            }
        }


    }

    @SuppressLint("WrongConstant")
    @Override
    public void drawerState() {

        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        } else {
            drawerLayout.openDrawer(Gravity.START);
        }
    }

    @Override
    public void init() {
        lottieAnimationView = findViewById(R.id.animationView);
        cityName = findViewById(R.id.tv_main_cityName);
        tvStateAndDate = findViewById(R.id.tv_main_stateAndDate);
        tvDegree = findViewById(R.id.tv_main_degree);
        iconMore = findViewById(R.id.iv_main_seeMoreDetails);
        etSearchCity = findViewById(R.id.et_main_search);

        location = findViewById(R.id.iv_main_location);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        viewPager = findViewById(R.id.viewPager2_main);
        iv_main_menu = findViewById(R.id.iv_main_menu);
    }

    @Override
    public void onClickButtons() {


        iv_main_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.onClickMenuIcon();
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.onClickLocationIcon();
            }
        });

        iconMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (weather != null) {
                    mainPresenter.onClickWeather(weather);
                }
            }
        });

        etSearchCity.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onClickSearch(etSearchCity.getText().toString());
            }
        });
    }

    private boolean onClickSearch(String q) {
        mainPresenter.onSearch(q);
        return false;
    }

    @Override
    public void showViewPagerItems(List<Weather> weathers) {

        slidAdapter = new SlidAdapter(this, weathers);
        viewPager.setAdapter(slidAdapter);
        viewPager.setPageMargin(dpToPx(30));
        indicator = findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
    }

    @Override
    public void showSearchResult(Weather weather) {

    }

    @Override
    public void showNotificationDialog() {

    }

    @Override
    public void showLocationFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_SEND_DATA_KEY, null);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutFirst, ShowItemsFragment.getInstance(bundle));

        transaction.commit();
    }

    @Override
    public void showWeatherFragment(Weather weather) {
        if (weather!=null){
            Bundle bundle = new Bundle();
            bundle.putParcelable(EXTRA_SEND_DATA_KEY, weather);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayoutFirst, ShowItemsFragment.getInstance(bundle));

            transaction.commit();
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
        } else {
            super.onBackPressed();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.onDetach();
    }

    @Override
    public void onClickItem(Weather weather) {
        mainPresenter.onClickViewPagerItems(weather);
    }

    @Override
    public void onLongClick(Weather weather) {

        mainPresenter.onLongClickViewPager(weather);

    }


    @Override
    public void onBackState(List<Weather> weathers) {
        showViewPagerItems(weathers);
    }
}