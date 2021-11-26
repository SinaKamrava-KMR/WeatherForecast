package com.mainway.weatherforecast.View.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.mainway.weatherforecast.R;
import com.mainway.weatherforecast.View.Detail.ShowItemsFragment;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private SlidAdapter slidAdapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager=findViewById(R.id.viewPager2_main);
        slidAdapter=new SlidAdapter(this);
        viewPager.setAdapter(slidAdapter);
        viewPager.setPageMargin(dpToPx(30));

        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        setUpNavigationView();

        ImageView location=findViewById(R.id.iv_main_location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.frameLayoutFirst,new ShowItemsFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
    // replace 10 with the value you want for margin in dp.
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void setUpNavigationView(){
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.navigation_view);
        ImageView iv_main_menu=findViewById(R.id.iv_main_menu);
        iv_main_menu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(Gravity.START)){
                    drawerLayout.closeDrawer(Gravity.START);
                }else {
                    drawerLayout.openDrawer(Gravity.START);
                }
            }
        });


    }

    @SuppressLint("WrongConstant")
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)){
            drawerLayout.closeDrawer(Gravity.START);
        }else {
            super.onBackPressed();
        }


    }
}