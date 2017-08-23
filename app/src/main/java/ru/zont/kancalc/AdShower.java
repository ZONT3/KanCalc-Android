package ru.zont.kancalc;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

class AdShower {

    static InterstitialAd interstitial;

    static InterstitialAd load(Context context) {
        interstitial = new InterstitialAd(context);
        interstitial.setAdUnitId("ca-app-pub-7799305268524604/9812741963");
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitial.loadAd(adRequest);
        return interstitial;
    }
}
