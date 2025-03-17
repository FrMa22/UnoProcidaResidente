package com.porfirio.orariprocida2011.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.porfirio.orariprocida2011.R;

public class InfoActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info);
        LottieAnimationView lottieAnimationView = findViewById(R.id.lottie_ship);
        lottieAnimationView.setAnimation(R.raw.ship_animation);
        lottieAnimationView.setRepeatCount(LottieDrawable.INFINITE);
        lottieAnimationView.playAnimation();

        LottieAnimationView lottieAnimationView2 = findViewById(R.id.wave_top);
        lottieAnimationView2.setAnimation(R.raw.wave_animation);
        lottieAnimationView2.setRepeatCount(LottieDrawable.INFINITE);
        lottieAnimationView2.playAnimation();

        LottieAnimationView lottieAnimationView3 = findViewById(R.id.wave_bottom);
        lottieAnimationView3.setAnimation(R.raw.wave_animation);
        lottieAnimationView3.setRepeatCount(LottieDrawable.INFINITE);
        lottieAnimationView3.playAnimation();


        TextView textView = findViewById(R.id.link_url);
        SpannableString spannableString = getSpannableString();

        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        ImageButton back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.enter_from_center, R.anim.exit_to_center);
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
                overridePendingTransition(R.anim.enter_from_center, R.anim.exit_to_center);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @NonNull
    private SpannableString getSpannableString() {
        SpannableString spannableString = new SpannableString("UnoProcidaResidente");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://unoprocidaresidente.altervista.org/PrivacyPolicyUnoProcidaResidente.htm"));
                startActivity(browserIntent);
            }
        };

        spannableString.setSpan(clickableSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R. color. tertiaryColor, null)), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}