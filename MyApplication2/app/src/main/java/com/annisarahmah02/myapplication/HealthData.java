package com.annisarahmah02.myapplication;

import com.google.gson.annotations.SerializedName;

public class HealthData {
    @SerializedName("lokasi")
    private String lokasi;

    @SerializedName("No_Badge")
    private String noBadge;

    @SerializedName("Name")
    private String name;

    @SerializedName("Perusahaan")
    private String perusahaan;

    @SerializedName("Suhu_Badan")
    private String suhuBadan;

    @SerializedName("Tekanan_Sistolik")
    private String tekananSistolik;

    @SerializedName("Tekanan_Diastolik")
    private String tekananDiastolik;

    @SerializedName("Nadi_istirahat")
    private String nadiIstirahat;

    // getter methods for all fields

    public String getLokasi() {
        return lokasi;
    }

    public String getNoBadge() {
        return noBadge;
    }

    public String getName() {
        return name;
    }

    public String getPerusahaan() {
        return perusahaan;
    }

    public String getSuhuBadan() {
        return suhuBadan;
    }

    public String getTekananSistolik() {
        return tekananSistolik;
    }

    public String getTekananDiastolik() {
        return tekananDiastolik;
    }

    public String getNadiIstirahat() {
        return nadiIstirahat;
    }
}

