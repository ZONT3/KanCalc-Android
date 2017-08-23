package ru.zont.kancalc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class ChanceCalcActivity extends AppCompatActivity {

    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chance_calc);

        AdView av = (AdView)findViewById(R.id.cc_ad);
        AdRequest request = new AdRequest.Builder().build();
        av.loadAd(request);
        interstitialAd = AdShower.load(this);
    }

    public void onEnter(View v) {
        final EditText chance = (EditText)findViewById(R.id.cc_chance);
        final EditText tries = (EditText)findViewById(R.id.cc_tries);
        final TextView result = (TextView)findViewById(R.id.cc_result);

        result.setText(Core.getSumChance(Double.parseDouble(chance.getText().toString()),
                Integer.parseInt(tries.getText().toString()))+"%");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            Intent i = new Intent(ChanceCalcActivity.this, MainActivity.class);
            i.putExtra("notFirst", true);
            startActivity(i);
            interstitialAd.show();
            finish();
        } catch (Exception e) {e.printStackTrace();}
    }
}
