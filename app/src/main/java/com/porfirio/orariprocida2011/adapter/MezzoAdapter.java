package com.porfirio.orariprocida2011.adapter;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;

import com.porfirio.orariprocida2011.R;
import com.porfirio.orariprocida2011.entity.Mezzo;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class MezzoAdapter extends ArrayAdapter<Mezzo> {

    private Context context;
    private List<Mezzo> mezziList;
    private ImageView iconLogo;
    private TextView textViewWarning;
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

        iconLogo = convertView.findViewById(R.id.image_view_icon_logo);
        iconLogo.setImageResource(getIconForCompany(mezzo.nave));


        textViewWarning = convertView.findViewById(R.id.text_view_warning);
        Animation blinkAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        if(mezzo.tot > 0){
            textViewWarning.setVisibility(VISIBLE);
            textViewWarning.startAnimation(blinkAnimation);
        }else{
            textViewWarning.setVisibility(INVISIBLE);
            textViewWarning.clearAnimation();
        }


        return convertView;
    }
    private int getIconForCompany(String mezzoNome) {
        // Lista delle aziende supportate
        String[] aziende = {"Caremar", "SNAV", "Medmar", "Ippocampo", "Scotto Line", "Alilauro", "LazioMar","Gestur"};

        for (String azienda : aziende) {
            if (mezzoNome.contains(azienda)) {
                String iconName = "icon_" + azienda.toLowerCase().replace(" ", "");
                int resId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());

                if (resId != 0) {
                    return resId;
                }
            }
        }

        return R.drawable.traghetto_icon;
    }
}
