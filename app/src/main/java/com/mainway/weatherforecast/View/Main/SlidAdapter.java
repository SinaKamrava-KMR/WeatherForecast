package com.mainway.weatherforecast.View.Main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.mainway.weatherforecast.Model.GetDate;
import com.mainway.weatherforecast.Model.Weather;
import com.mainway.weatherforecast.R;

import java.util.List;
import java.util.Random;

public class SlidAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ResultListener listener;
    private List<Weather> favoritWeathers;
    private int[] images={
            R.drawable.pic3,
            R.drawable.pic2,
            R.drawable.pic4,
            R.drawable.london_pic1,
            R.drawable.pic5
    };

    public SlidAdapter(Context context, List<Weather> favoriteWeathers) {
        this.context = context;
        inflater=LayoutInflater.from(context);
        listener= (ResultListener) context;
        this.favoritWeathers = favoriteWeathers;
    }
    @Override
    public int getCount() {
        return favoritWeathers.size();
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view=inflater.inflate(R.layout.favorite_container,container,false);
        ImageView imageView=view.findViewById(R.id.iv_favorite_locations);
        imageView.setImageResource(images[new Random().nextInt(images.length)]);
        bindPage(favoritWeathers.get(position),view);
        container.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickItem(favoritWeathers.get(position));
            }
        });


        return  view;
    }
    public void delete(Weather weather){
        for (int i = 0; i < favoritWeathers.size(); i++) {
            if (favoritWeathers.get(i).getId()==weather.getId()){
                this.favoritWeathers.remove(i);

            }
        }
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       //delete before views
        container.removeView((View) object);
    }

    public void bindPage(Weather weather,View v){

        TextView name=v.findViewById(R.id.tv_favorite_location_cityName);
        name.setText(weather.getName());

        TextView date=v.findViewById(R.id.tv_favorite_location_date);
        date.setText(GetDate.date(context));

        TextView degree=v.findViewById(R.id.tv_tv_favorite_location_Degree);
        degree.setText(weather.getTemp()+" C");
        String state= weather.getMain();
        TextView stateM=v.findViewById(R.id.tv_favorite_location_state);
        stateM.setText(state);

        LottieAnimationView lottieAnimationView=v.findViewById(R.id.animation_favorite_location_lottie);
        if (state.toLowerCase().contains("sun")|| state.toLowerCase().contains("clear")){
            lottieAnimationView.setAnimation(R.raw.clear_day);
        }else if (state.toLowerCase().contains("rain")){
            lottieAnimationView.setAnimation(R.raw.raining);
        }else if (state.toLowerCase().contains("snow")){
            lottieAnimationView.setAnimation(R.raw.cloud_plus_snow2);
        }else if (state.toLowerCase().contains("storm")){
            lottieAnimationView.setAnimation(R.raw.stormy);
        }else if (state.toLowerCase().contains("cloud") || state.toLowerCase().contains("laze")){
            lottieAnimationView.setAnimation(R.raw.cloud3);
        }


    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public float getPageWidth(int position) {
        return .5f;
    }

    public interface  ResultListener{
        void onClickItem(Weather weather);
        void onLongClick(Weather weather);
    }
}
