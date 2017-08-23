package ru.zont.kancalc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class FarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm);

        AdView av = (AdView)findViewById(R.id.farm_ad);
        AdRequest request = new AdRequest.Builder().build();
        av.loadAd(request);

        final Spinner maps = (Spinner)findViewById(R.id.farm_maps);
        final Spinner ranks = (Spinner)findViewById(R.id.farm_ranks);
        ArrayAdapter<String> mapsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Core.farmMaps);
        ArrayAdapter<String> ranksAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Core.ranks);
        maps.setAdapter(mapsAdapter);
        ranks.setAdapter(ranksAdapter);
        maps.setSelection(1);
    }

    public void onEnter(View v) {
        final EditText lvls = (EditText)findViewById(R.id.farm_lvls);
        final EditText lvle = (EditText)findViewById(R.id.farm_lvle);
        final Spinner map = (Spinner)findViewById(R.id.farm_maps);
        final Spinner rank = (Spinner)findViewById(R.id.farm_ranks);
        final TextView res = (TextView)findViewById(R.id.farm_result);

        if (lvls.getText().toString().isEmpty() || lvle.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.empty_level_field, Toast.LENGTH_LONG).show();
            return;
        }

        Kanmusu[] setup = null;
        try {
            File setupFile = new File(getCacheDir(), "setup");
            FileInputStream in = new FileInputStream(setupFile);
            ObjectInputStream ois = new ObjectInputStream(in);
            setup = (Kanmusu[]) ois.readObject();
            ois.close();
        } catch (Exception e) {e.printStackTrace();}

        if (setup == null) {
            Toast.makeText(this, R.string.setup_not_setted_up, Toast.LENGTH_LONG).show();
            editSetup(null);
            return;
        }

        int battles = Core.getBattlesLeft(lvls.getText()+"-"+lvle.getText(), (String)map.getSelectedItem(), (String)rank.getSelectedItem());
        Core.Consumption consumption = Core.getConsumption(setup, battles);
        res.setText(battles+" bs | "+consumption+" F/A");
    }

    public void editSetup(View v) {
        try {
            Intent i = new Intent(FarmActivity.this, SetupEditorActivity.class);
            startActivity(i);
        } catch (Exception e) {e.printStackTrace();}
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        try {
            Intent i = new Intent(FarmActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        } catch (Exception e) {e.printStackTrace();}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new File(getCacheDir(), "setup").delete();
    }
}
