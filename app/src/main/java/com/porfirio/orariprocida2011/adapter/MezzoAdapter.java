package com.porfirio.orariprocida2011.adapter;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import static androidx.appcompat.graphics.drawable.DrawableContainerCompat.Api21Impl.getResources;

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
import android.widget.LinearLayout;
import android.widget.TextView;


public class MezzoAdapter extends ArrayAdapter<Mezzo> {

    private Context context;
    private List<Mezzo> mezziList;
    private ImageView infoImageView;

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
        //infoImageView = convertView.findViewById(R.id.image_view_warning);
        LinearLayout layout_list_item = convertView.findViewById(R.id.layout_list_item);

        if(mezzo.tot > 0){
            //infoImageView.setVisibility(VISIBLE);
            layout_list_item.setBackgroundResource(R.drawable.list_item_background_warning);
            textViewPartenzaArrivo.setTextColor(getResources().getColor(R. color. red);
        }else{
            //infoImageView.setVisibility(INVISIBLE);
            layout_list_item.setBackgroundResource(R.drawable.list_item_background);
            textViewPartenzaArrivo.setTextColor(getResources().getColor(R. color. tertiaryColor);
        }

//        infoButton.setOnClickListener(v -> {
//            // Logica per l'azione sul bottone info, se necessario
//            // Puoi aprire un dialog o eseguire altre azioni qui
//        });

        return convertView;
    }
}
