package com.mainway.weatherforecast.View.Main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.mainway.weatherforecast.R;

public class SlidAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;
    private int[] images={
            R.drawable.pic3,
            R.drawable.pic2,
            R.drawable.pic4,
            R.drawable.london_pic1,
            R.drawable.pic5
    };

    public SlidAdapter(Context context) {
        this.context = context;
        inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return images.length;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view=inflater.inflate(R.layout.favorite_container,container,false);
        ImageView imageView=view.findViewById(R.id.iv_favorite_locations);
        imageView.setImageResource(images[position]);
        container.addView(view);
        return  view;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       //delete before views
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public float getPageWidth(int position) {
        return .5f;
    }
}
