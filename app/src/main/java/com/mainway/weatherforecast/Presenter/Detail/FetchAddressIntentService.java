package com.mainway.weatherforecast.Presenter.Detail;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.mainway.weatherforecast.Model.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class FetchAddressIntentService extends IntentService {

    private ResultReceiver resultReceiver;
    public  final String TAG="FetchAddressIntentService";

    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String errorMessage = "";
            resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);
            Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
            if (location == null) {
                return;
            }
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
            }

            if (addresses == null || addresses.isEmpty()) {
                deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
                Log.i(TAG, "addresses == null || addresses.isEmpty() ");
            } else {
                Address address = addresses.get(0);
                List<String> addressFragments = new ArrayList<>();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(address.getAddressLine(i));
                }
                deliverResultToReceiver(Constants.SUCCESS_RESULT, TextUtils.join(Objects.requireNonNull(System.getProperty("line.separator")),
                        addressFragments));
            }
        }
    }

    public void deliverResultToReceiver(int resultCode, String addressMessage) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, addressMessage);
        resultReceiver.send(resultCode, bundle);
    }
}