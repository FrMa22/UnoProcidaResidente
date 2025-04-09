package com.porfirio.orariprocida2011.entity;

import android.util.Log;

import com.porfirio.orariprocida2011.R;

public enum CompanyEnum {
    Caremar(R.drawable.icon_caremar, "caremar"),
    SNAV(R.drawable.icon_snav, "snav"),
    Medmar(R.drawable.icon_medmar, "medmar"),
    Ippocampo(R.drawable.icon_ippocampo, "ippocampo"),
    ScottoLine(R.drawable.icon_scottoline, "scottoline"),
    LazioMar(R.drawable.icon_laziomar, "laziomar"),
    Gestur(R.drawable.icon_gestur, "gestur"),
    Alilauro(R.drawable.icon_alilauro, "alilauro");

    private final int drawable;
    private final String name;

    CompanyEnum(int drawable, String name) {
        this.drawable = drawable;
        this.name = name;
    }

    public static int findDrawByName(String nome) {
        Log.d("CompanyEnum", "findDrawByName: " + nome);
        for (CompanyEnum company : CompanyEnum.values()) {
            if (nome.contains(company.name)) {
                return company.drawable;
            }
        }
        return R.drawable.traghetto_icon;
    }

}
