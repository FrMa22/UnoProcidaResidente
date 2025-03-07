package com.porfirio.orariprocida2011.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;

import com.porfirio.orariprocida2011.R;
import com.porfirio.orariprocida2011.entity.Mezzo;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class MezzoAdapter extends ArrayAdapter<Mezzo> {

    private Context context;
    private List<Mezzo> mezziList;
    private ImageButton infoButton;

    public MezzoAdapter(Context context, List<Mezzo> mezziList) {
        super(context, R.layout.list_item, mezziList);
        this.context = context;
        this.mezziList = mezziList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        Mezzo mezzo = mezziList.get(position);

        TextView textViewMezzo = convertView.findViewById(R.id.text_view_list_item_mezzo);
        textViewMezzo.setText(mezzo.nave);

        TextView textViewPartenzaArrivo = convertView.findViewById(R.id.text_view_list_item_Partenza_Arrivo);
        textViewPartenzaArrivo.setText(mezzo.portoPartenza + " - " + mezzo.portoArrivo);

        TextView textViewOrario = convertView.findViewById(R.id.text_view_list_item_Orario);
        textViewOrario.setText(mezzo.getDepartureTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));




        // Gestisci il bottone info
        infoButton = convertView.findViewById(R.id.image_button_info);

        if(mezzo.tot > 0){
            infoButton.setImageResource(R.drawable.exclamation);
        }else{
            infoButton.setImageResource(R.drawable.info_icon);
        }

//        infoButton.setOnClickListener(v -> {
//            // Logica per l'azione sul bottone info, se necessario
//            // Puoi aprire un dialog o eseguire altre azioni qui
//        });

        return convertView;
    }
}
