package ru.zont.kancalc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class FarmActivity extends AppCompatActivity {

    InterstitialAd interstitialAd;

    Kanmusu[] setup = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm);
        ActionBar ab = getSupportActionBar();
        if (ab != null) ab.setTitle(R.string.t_farm);

        final EditText lvls = (EditText)findViewById(R.id.farm_lvls);
        final EditText lvle = (EditText)findViewById(R.id.farm_lvle);
        final Spinner maps = (Spinner)findViewById(R.id.farm_maps);
        final Spinner ranks = (Spinner)findViewById(R.id.farm_ranks);
        final AdView av = (AdView)findViewById(R.id.farm_ad);

        AdRequest request = new AdRequest.Builder().build();
        av.loadAd(request);
        interstitialAd = AdShower.load(this);

        ArrayAdapter<String> mapsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Core.farmMaps);
        ArrayAdapter<String> ranksAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Core.ranks);
        maps.setAdapter(mapsAdapter);
        ranks.setAdapter(ranksAdapter);
        maps.setSelection(1);

        try {
            File setupFile = new File(getFilesDir(), "last.sskm");
            FileInputStream in = new FileInputStream(setupFile);
            ObjectInputStream ois = new ObjectInputStream(in);
            setup = (Kanmusu[]) ois.readObject();
            ois.close();
        } catch (Exception e) {e.printStackTrace();}

        if (setup == null) return;
        Kanmusu kekan = setup[0];
        Kanmusu remodel = kekan.getRemodel();
        if (remodel!=null) lvle.setText(remodel.level+"");
        lvls.setText(kekan.level+"");
    }
    public void onEnter(View v) {
        try {
            final EditText lvls = (EditText)findViewById(R.id.farm_lvls);
            final EditText lvle = (EditText)findViewById(R.id.farm_lvle);
            final Spinner map = (Spinner)findViewById(R.id.farm_maps);
            final Spinner rank = (Spinner)findViewById(R.id.farm_ranks);
            final TextView res = (TextView)findViewById(R.id.farm_result);

            if (lvls.getText().toString().isEmpty() || lvle.getText().toString().isEmpty()) {
                Toast.makeText(this, R.string.iinpt, Toast.LENGTH_LONG).show();
                return;
            }

            if (setup == null) {
                Toast.makeText(this, R.string.setup_not_setted_up, Toast.LENGTH_LONG).show();
                editSetup(null);
                return;
            }

            int battles = Core.getBattlesLeft(lvls.getText()+"-"+lvle.getText(), (String)map.getSelectedItem(), (String)rank.getSelectedItem());
            Core.Consumption consumption = Core.getConsumption(setup, battles);
            res.setText(battles+" bs | "+consumption+" F/A");
        } catch (Exception e) {
            Toast.makeText(this, R.string.iinpt, Toast.LENGTH_SHORT).show();
        }
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
            i.putExtra("notFirst", true);
            startActivity(i);
            interstitialAd.show();
            finish();
        } catch (Exception e) {e.printStackTrace();}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new File(getCacheDir(), "setup").delete();
    }
}
