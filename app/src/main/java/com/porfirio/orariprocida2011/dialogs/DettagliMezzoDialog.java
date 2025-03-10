package com.porfirio.orariprocida2011.dialogs;


import static android.view.Gravity.END;
import static android.view.Gravity.START;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.porfirio.orariprocida2011.R;
import com.porfirio.orariprocida2011.activities.OrariProcida2011Activity;
import com.porfirio.orariprocida2011.entity.Alert;
import com.porfirio.orariprocida2011.entity.Compagnia;
import com.porfirio.orariprocida2011.entity.Mezzo;
import com.porfirio.orariprocida2011.entity.Taxi;
import com.porfirio.orariprocida2011.threads.alerts.AlertsDAO;
import com.porfirio.orariprocida2011.threads.taxies.TaxisDAO;
import com.porfirio.orariprocida2011.utils.Analytics;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class DettagliMezzoDialog extends DialogFragment implements OnClickListener {
    TextView txtPartenzaDestinazione;
    Button btnTaxi;
    Button btnConfermaOSmentisci;
    Button btnBiglietterie;
    private Compagnia c;
    private final BiglietterieDialog biglietterieDialog = new BiglietterieDialog();
    private LinearLayout linear_layout_dettagli_mezzo;
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
    private View currentDynamicView = null;
    private String porto;
    private List<Taxi> taxis;
    private View view_separator;
    private int ragione;

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
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Ottieni l'altezza dello schermo
//        int screenHeight = getResources().getDisplayMetrics().heightPixels;
//        // Calcola il 60% dell'altezza dello schermo
//        int dialogHeight = (int) (screenHeight * 0.6);
//        // Imposta l'altezza del dialog
//        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight);
//    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            getDialog().getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.dettaglimezzo, container);
        //setContentView(R.layout.dettaglimezzo);
        linear_layout_dettagli_mezzo = view.findViewById(R.id.linear_layout_dettagli_mezzo);

        txtPartenzaDestinazione = view.findViewById(R.id.txtPartenzaDestinazione);
        txtMezzo = view.findViewById(R.id.txtMezzo);
        txtPartenza = view.findViewById(R.id.txtPartenza);
        txtArrivo = view.findViewById(R.id.txtArrivo);
        txtCostoIntero = view.findViewById(R.id.txtCostoIntero);
        txtCostoRidotto = view.findViewById(R.id.txtCostoRidotto);
        txtAuto = view.findViewById(R.id.txtAuto);
        txtAllertaMeteo = view.findViewById(R.id.txtAllertaMeteo);
        view_separator = view.findViewById(R.id.view_separator);

//        Button btnReturnToHome = view.findViewById(R.id.btnReturnToHome);
//        btnReturnToHome.setOnClickListener(v -> dismiss());

        btnTaxi = view.findViewById(R.id.btnTaxi);
        btnTaxi.setOnClickListener(v -> {
            analytics.send("App Event", "Click Taxi Dialog");
//            taxiDialog.show(fragmentManager, "fragment_edit_name");
            toggleGrid("Taxi");
            updateButtonStates("Taxi");
        });

        btnBiglietterie = view.findViewById(R.id.btnBiglietterie);
        btnBiglietterie.setOnClickListener(v -> {
            analytics.send("App Event", "Click Biglietterie Dialog");
            //biglietterieDialog.show(fragmentManager, "fragment_edit_name");
            toggleGrid("Biglietteria");
            updateButtonStates("Biglietteria");
        });

        btnConfermaOSmentisci = view.findViewById(R.id.btnConfermaOSmentisci);
        btnConfermaOSmentisci.setOnClickListener(v -> {
            if (!callingActivity.isOnline())
                Toast.makeText(getContext(), callingActivity.getString(R.string.soloOnline), Toast.LENGTH_SHORT).show();
            else {
                analytics.send("App Event", "Click Segnalazione Dialog");
                //segnalazioneDialog.show(fragmentManager, "fragment_edit_name");
                toggleReportGrid();
                updateButtonStates("Report");
            }
        });

        if (mezzo != null) {
            txtMezzo.setText(mezzo.nave);
        } else {
            Log.d("DettagliMezzoDialog", "Errore: oggetto Mezzo non esiste");
        }


        LocalDate departureDate = LocalDateTime.ofInstant(callingActivity.c.toInstant(), callingActivity.c.getTimeZone().toZoneId()).toLocalDate();
        LocalDate arrivalDate = LocalDateTime.ofInstant(callingActivity.c.toInstant(), callingActivity.c.getTimeZone().toZoneId()).toLocalDate();

        if (mezzo.getGiornoSeguente()) {
            departureDate = departureDate.plusDays(1);
            arrivalDate = arrivalDate.plusDays(1);
        }
        String s = mezzo.portoPartenza + " - " + mezzo.portoArrivo;
        txtPartenzaDestinazione.setText(s);

        s = mezzo.portoPartenza + " - " + DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(mezzo.getDepartureTime());
        txtPartenza.setText(s);

        s = mezzo.portoArrivo + " - " + DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(mezzo.getArrivalTime());
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
        c = null;
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
        this.porto = mezzo.portoPartenza;

        taxisDAO.getUpdates().observe(this, update -> {
            if (update.isValid())
                taxiDialog.setTaxis(update.getData());
                taxis=update.getData();
        });

//        segnalazioneDialog = new SegnalazioneDialog(alertsDAO);
//        segnalazioneDialog.setOrarioRef(calen);
//        segnalazioneDialog.setMezzo(mezzo);
//        segnalazioneDialog.setCallingContext(this.getContext());
//        segnalazioneDialog.setAnalytics(analytics);
//        segnalazioneDialog.setListCompagnia(lc);
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
            txtAllertaMeteo.setVisibility(VISIBLE);
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
    private void toggleGrid(String type) {
        if (currentDynamicView != null) {
            view_separator.setVisibility(GONE);
            linear_layout_dettagli_mezzo.removeView(currentDynamicView);
            if (currentDynamicView.getTag().equals(type)) {
                currentDynamicView = null;
                return;
            }
        }

        currentDynamicView = createGridLayout(type);
        view_separator.setVisibility(VISIBLE);
        linear_layout_dettagli_mezzo.addView(currentDynamicView);
    }

    private GridLayout createGridLayout(String type) {

        GridLayout gridLayout = new GridLayout(getContext());
        gridLayout.setTag(type);
        gridLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        gridLayout.setUseDefaultMargins(true);
        gridLayout.setColumnCount(2);
        gridLayout.setPadding(10, 10, 10, 10);



        if (type.equals("Taxi")) {
            ArrayList<Taxi> taxiPortoList = new ArrayList<>();


            for (int i = 0; i < taxis.size(); i++)
                if (porto.contains(taxis.get(i).getPorto()) && !(porto.contentEquals("Monte di Procida") && taxis.get(i).getPorto().contentEquals("Procida")))
                    taxiPortoList.add(taxis.get(i));

            if (!taxiPortoList.isEmpty()) {
                for (Taxi taxi : taxiPortoList) {
                    addGridItem(gridLayout, taxi.getCompagnia(), taxi.getNumero(), true);
                }
            }
        } else if (type.equals("Biglietteria")) {
            if (c == null) {
                Log.d("createGridLayout","C è null");
                addGridItem(gridLayout, getString(R.string.NoBiglietterie), "", false);
            } else {
                Log.d("createGridLayout","C NON è null");
                int contactsCount = c.getContactsCount();
                for (int i = 0; i < contactsCount; i++) {
                    String contactName = c.getContactName(i);
                    String contactNumber = c.getContactNumber(i);
                    addGridItem(gridLayout, contactName, contactNumber, true);
                }
            }
        }

        return gridLayout;
    }


    private void addGridItem(GridLayout grid, String label, String value, boolean addLinkify) {
        TextView labelView = new TextView(getContext());
        labelView.setText(label + ":");
        labelView.setTypeface(null, Typeface.BOLD);
        labelView.setGravity(Gravity.END);

        GridLayout.LayoutParams labelParams = new GridLayout.LayoutParams();
        labelParams.width = 0;
        labelParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
        labelParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        labelView.setLayoutParams(labelParams);

        TextView valueView = new TextView(getContext());
        valueView.setText(value);
        valueView.setGravity(Gravity.START);

        GridLayout.LayoutParams valueParams = new GridLayout.LayoutParams();
        valueParams.width = 0;
        valueParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
        valueParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        valueView.setLayoutParams(valueParams);

        if (addLinkify) {
            Linkify.addLinks(valueView, Linkify.PHONE_NUMBERS);
        }

        grid.addView(labelView);
        grid.addView(valueView);
    }

    private void toggleReportGrid() {
        if (currentDynamicView != null) {
            view_separator.setVisibility(GONE);
            linear_layout_dettagli_mezzo.removeView(currentDynamicView);
            if ("Report".equals(currentDynamicView.getTag())) {
                currentDynamicView = null;
                return;
            }
        }
        currentDynamicView = createReportLinearLayout();
        view_separator.setVisibility(VISIBLE);
        linear_layout_dettagli_mezzo.addView(currentDynamicView);
    }

    private LinearLayout createReportLinearLayout() {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setPadding(10, 10, 10, 10);
        linearLayout.setTag("Report");

        Spinner spnRagioni = new Spinner(callingContext);
        spnRagioni.setPopupBackgroundResource(R.drawable.spinner_dropdown_background);
        spnRagioni.setBackgroundResource(R.drawable.dropdown_background);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                callingContext, R.array.strRagioni, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        LinearLayout.LayoutParams spinnerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        spnRagioni.setLayoutParams(spinnerParams);
        spnRagioni.setPadding(20,0,0,0);
        spnRagioni.setAdapter(adapter);
        spnRagioni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                ragione = pos;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        linearLayout.addView(spnRagioni);

        EditText editTextDettagli = new EditText(callingContext);
        LinearLayout.LayoutParams etParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        etParams.topMargin =20;
        etParams.bottomMargin = 20;
        editTextDettagli.setLayoutParams(etParams);
        editTextDettagli.setBackgroundResource(R.drawable.edit_text_background);
        editTextDettagli.setHint(getString(R.string.hintDettagli));
        editTextDettagli.setTextColor(getResources().getColor(R. color. secondaryColor));
        editTextDettagli.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        linearLayout.addView(editTextDettagli);

        Button btnInvia = new Button(callingContext);
        btnInvia.setText(getString(R.string.invia));
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        btnParams.gravity = END;
        btnInvia.setLayoutParams(btnParams);
        btnInvia.setTextColor(getResources().getColor(R. color. red));
        btnInvia.setBackgroundResource(R.drawable.background_button_report);
        btnInvia.setOnClickListener(v -> {
            analytics.send("App Event", "Segnala Avaria");
            String dettagli = editTextDettagli.getText().toString().replaceAll("\r\n|\r|\n", " ");
            scriviSegnalazione(true, dettagli);
            Toast.makeText(v.getContext(), R.string.ringraziamentoSegnalazione, Toast.LENGTH_SHORT).show();
            toggleReportGrid();
            updateButtonStates("Report");
        });
        linearLayout.addView(btnInvia);

        return linearLayout;
    }


    private String scriviSegnalazione(boolean problema, String dettagli) {


        int reason = problema ? ragione : Alert.REASON_NO_PROBLEM;
        LocalDate transportDate = LocalDate.of(calen.get(Calendar.YEAR), calen.get(Calendar.MONTH) + 1, calen.get(Calendar.DAY_OF_MONTH));

        if (mezzo.getGiornoSeguente())
            transportDate = transportDate.plusDays(1);

        Alert alert = new Alert(mezzo.getId(), reason, dettagli, transportDate);
        alertsDAO.send(alert);

        return "ok";
    }

    private void updateButtonStates(String activeType) {
        // Per Taxi:
        if ("Taxi".equals(activeType) && currentDynamicView != null && "Taxi".equals(currentDynamicView.getTag())) {
            btnTaxi.setAlpha(0.5f); // Bottone "schiarito"
        } else {
            btnTaxi.setAlpha(1.0f); // Stato originale
        }
        // Per Biglietteria:
        if ("Biglietteria".equals(activeType) && currentDynamicView != null && "Biglietteria".equals(currentDynamicView.getTag())) {
            btnBiglietterie.setAlpha(0.5f);
        } else {
            btnBiglietterie.setAlpha(1.0f);
        }
        // Per Report:
        if ("Report".equals(activeType) && currentDynamicView != null && "Report".equals(currentDynamicView.getTag())) {
            btnConfermaOSmentisci.setAlpha(0.5f);
        } else {
            btnConfermaOSmentisci.setAlpha(1.0f);
        }
    }

}
