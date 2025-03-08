package com.porfirio.orariprocida2011.dialogs;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.porfirio.orariprocida2011.R;
import com.porfirio.orariprocida2011.activities.OrariProcida2011Activity;
import com.porfirio.orariprocida2011.entity.Compagnia;
import com.porfirio.orariprocida2011.entity.Mezzo;
import com.porfirio.orariprocida2011.threads.alerts.AlertsDAO;
import com.porfirio.orariprocida2011.threads.taxies.TaxisDAO;
import com.porfirio.orariprocida2011.utils.Analytics;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class DettagliMezzoDialog extends DialogFragment implements OnClickListener {

    private final BiglietterieDialog biglietterieDialog = new BiglietterieDialog();
    private Mezzo mezzo;
    private Context callingContext;
    private TaxiDialog taxiDialog;
    private SegnalazioneDialog segnalazioneDialog;
    private Calendar calen;
    private OrariProcida2011Activity callingActivity;
    private FragmentManager fragmentManager;
    private ArrayList<Compagnia> lc;
    private String[] ragioni;

    private final AlertsDAO alertsDAO;
    private final TaxisDAO taxisDAO;
    private Analytics analytics;
    private TextView txtMezzo;
    private TextView txtPartenza;
    private TextView txtArrivo;
    private TextView txtCostoIntero;
    private TextView txtCostoRidotto;
    private TextView txtAuto;
    private TextView txtAllertaMeteo;

    public DettagliMezzoDialog(AlertsDAO alertsDAO, TaxisDAO taxisDAO) {
        this.alertsDAO = Objects.requireNonNull(alertsDAO);
        this.taxisDAO = taxisDAO;
    }

    public void setDettagliMezzoDialog(FragmentManager fm, OrariProcida2011Activity a, Context context, Calendar cal) {
        fragmentManager = fm;
        callingActivity = a;
        callingContext = context;
        calen = cal;
    }

    public void setAnalytics(Analytics analytics) {
        this.analytics = analytics;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.dettaglimezzo, container);
        //setContentView(R.layout.dettaglimezzo);

        txtMezzo = view.findViewById(R.id.txtMezzo);
        txtPartenza = view.findViewById(R.id.txtPartenza);
        txtArrivo = view.findViewById(R.id.txtArrivo);
        txtCostoIntero = view.findViewById(R.id.txtCostoIntero);
        txtCostoRidotto = view.findViewById(R.id.txtCostoRidotto);
        txtAuto = view.findViewById(R.id.txtAuto);
        txtAllertaMeteo = view.findViewById(R.id.txtAllertaMeteo);

        Button btnReturnToHome = view.findViewById(R.id.btnReturnToHome);
        btnReturnToHome.setOnClickListener(v -> dismiss());

        Button btnTaxi = view.findViewById(R.id.btnTaxi);
        btnTaxi.setOnClickListener(v -> {
            analytics.send("App Event", "Click Taxi Dialog");
            taxiDialog.show(fragmentManager, "fragment_edit_name");
        });

        Button btnBiglietterie = view.findViewById(R.id.btnBiglietterie);
        btnBiglietterie.setOnClickListener(v -> {
            analytics.send("App Event", "Click Biglietterie Dialog");
            biglietterieDialog.show(fragmentManager, "fragment_edit_name");
        });

        Button btnConfermaOSmentisci = view.findViewById(R.id.btnConfermaOSmentisci);
        btnConfermaOSmentisci.setOnClickListener(v -> {
            if (!callingActivity.isOnline())
                Toast.makeText(getContext(), callingActivity.getString(R.string.soloOnline), Toast.LENGTH_SHORT).show();
            else {
                analytics.send("App Event", "Click Segnalazione Dialog");
                segnalazioneDialog.show(fragmentManager, "fragment_edit_name");
            }
        });

        if (mezzo != null) {
            final String text = "    " + mezzo.nave + "    ";
            txtMezzo.setText(text);
        } else {
            Log.d("DettagliMezzoDialog", "Errore: oggetto Mezzo non esiste");
        }


        LocalDate departureDate = LocalDateTime.ofInstant(callingActivity.c.toInstant(), callingActivity.c.getTimeZone().toZoneId()).toLocalDate();
        LocalDate arrivalDate = LocalDateTime.ofInstant(callingActivity.c.toInstant(), callingActivity.c.getTimeZone().toZoneId()).toLocalDate();

        if (mezzo.getGiornoSeguente()) {
            departureDate = departureDate.plusDays(1);
            arrivalDate = arrivalDate.plusDays(1);
        }

        String s = mezzo.portoPartenza + " - " + DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(mezzo.getDepartureTime()) + " " + DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format(departureDate);
        txtPartenza.setText(s);

        s = mezzo.portoArrivo + " - " + DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(mezzo.getArrivalTime()) + " - " + DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format(arrivalDate);
        txtArrivo.setText(s);



        if (mezzo.getReducedPrice() > 0){
            String ridotto = mezzo.getReducedPrice() + " € ";
            txtCostoRidotto.setText(ridotto);
        }


        if (mezzo.getFullPrice() > 0) {
            String intero = String.format(Locale.getDefault(), "%.2f", mezzo.getFullPrice()) + " € ";
            txtCostoIntero.setText(intero);
        }


        //trova compagnia c
        Compagnia c = null;
        for (int i = 0; i < lc.size(); i++) {
            if (mezzo.nave.contains(lc.get(i).getName()))
                c = lc.get(i);
        }

        //Aggiunto Aladino
        if (c != null) {
            if (c.getName().contains("Ippocampo") || c.getName().contentEquals("Procida Lines") || mezzo.nave.contains("Aliscafo") || mezzo.nave.contains("Aladino") || mezzo.nave.contains("Motonave") || mezzo.nave.contains("Scotto Line"))
                txtAuto.setText(callingContext.getString(R.string.trasportaSoloPasseggeri));
            else
                txtAuto.setText(callingContext.getString(R.string.trasportaAutoPasseggeri));

            biglietterieDialog.setCompagnia(c);
        } else
            txtAuto.setText("");
        taxiDialog = new TaxiDialog();
        taxiDialog.setPorto(mezzo.portoPartenza);

        taxisDAO.getUpdates().observe(this, update -> {
            if (update.isValid())
                taxiDialog.setTaxis(update.getData());
        });

        segnalazioneDialog = new SegnalazioneDialog(alertsDAO);
        segnalazioneDialog.setOrarioRef(calen);
        segnalazioneDialog.setMezzo(mezzo);
        segnalazioneDialog.setCallingContext(this.getContext());
        segnalazioneDialog.setAnalytics(analytics);
        segnalazioneDialog.setListCompagnia(lc);
        //segnalazioneDialog.fill(lc);

        ragioni = getResources().getStringArray(R.array.strRagioni);
        String spc = "";
        if (mezzo.segnalazionePiuComune() > -1) {
            spc = ragioni[mezzo.segnalazionePiuComune()];
        }
        if (mezzo.tot > 0 || mezzo.conferme > 0) {
            StringBuilder alert = new StringBuilder();
            if (mezzo.tot > 0) {
                if (mezzo.conc) {
                    alert.append(" - ").append(mezzo.tot).append(mezzo.tot == 1 ? " " + getString(R.string.segnalazione) : " " + getString(R.string.segnalazioni));
                    alert.append(" ").append(getString(R.string.diProblemi)).append(" (").append(spc).append(")");
                } else {
                    alert.append(" - ").append(getString(R.string.possibiliProblemi)).append(" (").append(mezzo.tot);
                    alert.append(mezzo.tot == 1 ? " " + getString(R.string.segnalazione) + ")" : " " + getString(R.string.segnalazioni) + ")");
                    alert.append(", ").append(getString(R.string.inParticolare)).append(" ").append(spc);
                }
            }
            if (mezzo.conferme > 0) {
                alert.append(" - ").append(mezzo.conferme).append(mezzo.conferme == 1 ? " " + getString(R.string.utenteDice) : " " + getString(R.string.utentiDicono));
                alert.append(" ").append(getString(R.string.cheLaCorsaERegolare));
            }

            txtAllertaMeteo.setText(alert);
        }

        return view;
    }

    public void setMezzo(Mezzo m) {
        mezzo = m;
    }

    @Override
    public void onClick(View arg0) {
        this.dismiss();
    }

    public void setListCompagnia(ArrayList<Compagnia> listCompagnia) {
        lc = listCompagnia;
    }

}
