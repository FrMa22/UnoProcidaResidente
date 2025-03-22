package com.porfirio.orariprocida2011.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Window;
import android.widget.GridView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.porfirio.orariprocida2011.R;
import com.porfirio.orariprocida2011.adapter.WindObservationAdapter;
import com.porfirio.orariprocida2011.entity.Meteo;

import java.util.Objects;

public class WeatherDialog extends Dialog {

    public WeatherDialog(Context context, String dateTime, String windInfo, Meteo meteo) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_dialog);
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvDateTime = findViewById(R.id.tvDateTime);
        TextView tvWindInfo = findViewById(R.id.tvWindInfo);
        GridView windTable = findViewById(R.id.windTable);

        tvWindInfo.setText(windInfo);
        tvDateTime.setText(dateTime);
        Log.d("WeatherDialog","datetime: " + dateTime);

        LottieAnimationView lottieAnimationView = findViewById(R.id.lottie_wind_view);
        lottieAnimationView.setAnimation(R.raw.wind_animation);
        lottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);
        lottieAnimationView.playAnimation();

        if (meteo.getForecasts() != null && !meteo.getForecasts().isEmpty()) {
            WindObservationAdapter adapter = new WindObservationAdapter(getContext(), meteo.getForecasts());
            windTable.setAdapter(adapter);
        }
    }
}
