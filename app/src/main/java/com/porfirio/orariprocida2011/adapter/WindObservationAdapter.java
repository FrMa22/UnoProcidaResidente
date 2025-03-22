package com.porfirio.orariprocida2011.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.porfirio.orariprocida2011.R;
import com.porfirio.orariprocida2011.entity.Osservazione;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class WindObservationAdapter extends ArrayAdapter<Osservazione> {
    private final Context context;
    private final List<Osservazione> observations;

    public WindObservationAdapter(Context context, List<Osservazione> observations) {
        super(context, R.layout.list_item, observations);
        this.context = context;
        this.observations = observations;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.weather_grid_item, parent, false);
        }

        Osservazione observation = observations.get(position);
        TextView textViewMezzo = convertView.findViewById(R.id.weather_item_text);

        String time = observation.getTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        String windSpeed = (int) Math.floor(observation.getWindSpeed()) + " Km/h";
        String formattedText = time + "\n" + windSpeed;

        textViewMezzo.setText(formattedText);

        return convertView;
    }
}
