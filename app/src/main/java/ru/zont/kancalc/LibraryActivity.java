package ru.zont.kancalc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class LibraryActivity extends AppCompatActivity {

    InterstitialAd interstitialAd;

    final Context context = this;

    boolean enableKai = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        AdView av = (AdView)findViewById(R.id.lib_ad);
        AdRequest request = new AdRequest.Builder().build();
        av.loadAd(request);
        interstitialAd = AdShower.load(this);

        final Spinner kmlist = (Spinner)findViewById(R.id.lib_spinner);
        final Spinner models = (Spinner)findViewById(R.id.lib_otherVers);
        ArrayAdapter<Kanmusu> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Core.kmlist);
        kmlist.setAdapter(adapter);

        kmlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Kanmusu kanmusu = (Kanmusu) kmlist.getSelectedItem();
                ArrayAdapter<Kanmusu> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, kanmusu.remodels);
                models.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        models.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Kanmusu kanmusu = null;
                try {
                    kanmusu = (Kanmusu)models.getSelectedItem();
                } catch (Exception e) {e.printStackTrace();}
                if (kanmusu==null)
                    return;

                parseInfo(kanmusu);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ArrayList<Kanmusu> list = (ArrayList<Kanmusu>) (enableKai ? Core.kmlistAM : Core.kmlist).clone();
        switch (item.getItemId()) {
            case R.id.lib_sort_class:
                Core.kmsortClass(list);
                break;
            case R.id.lib_sort_name:
                Core.kmsortName(list);
                break;
            case R.id.lib_sort_id:
                Core.kmsortId(list);
                break;
            case R.id.lib_sort_nid:
                Core.kmsortNid(list);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        ArrayAdapter<Kanmusu> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, list);
        ((Spinner)findViewById(R.id.lib_spinner)).setAdapter(adapter);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.library, menu);
        return true;
    }

    private void parseInfo(Kanmusu kanmusu) {
        ArrayList<TextView> arr = new ArrayList<>();
        final TextView id = (TextView)findViewById(R.id.lib_id);
        final TextView nid = (TextView)findViewById(R.id.lib_nid);
        final TextView name = (TextView)findViewById(R.id.lib_name); arr.add(name);
        final TextView jpname = (TextView)findViewById(R.id.lib_jpname); arr.add(jpname);
        final TextView craft = (TextView)findViewById(R.id.lib_craft); arr.add(craft);
        final TextView fuel = (TextView)findViewById(R.id.lib_fuel); arr.add(fuel);
        final TextView ammo = (TextView)findViewById(R.id.lib_ammo); arr.add(ammo);
        final TextView hp = (TextView)findViewById(R.id.lib_hp); arr.add(hp);
        final TextView fp = (TextView)findViewById(R.id.lib_fp); arr.add(fp);
        final TextView tp = (TextView)findViewById(R.id.lib_TP); arr.add(tp);
        final TextView aa = (TextView)findViewById(R.id.lib_aa); arr.add(aa);
        final TextView ar = (TextView)findViewById(R.id.lib_ar); arr.add(ar);
        final TextView eva = (TextView)findViewById(R.id.lib_eva); arr.add(eva);
        final TextView asw = (TextView)findViewById(R.id.lib_asw); arr.add(asw);
        final TextView los = (TextView)findViewById(R.id.lib_los); arr.add(los);
        final TextView luk = (TextView)findViewById(R.id.lib_luk); arr.add(luk);
        final TextView rng = (TextView)findViewById(R.id.lib_rng); arr.add(rng);
        final TextView speed = (TextView)findViewById(R.id.lib_speed); arr.add(speed);

        id.setText(kanmusu.id+"");
        nid.setText(kanmusu.nid+"");

        ArrayList<Object> stats = kanmusu.getParcingStats();
        for (int i=0; i<arr.size(); i++) {
            if (stats.get(i)!=null && !stats.get(i).toString().equals("0"))
                arr.get(i).setText(stats.get(i).toString());
            else
                arr.get(i).setText("??");
        }
    }

    public void toggleKai(MenuItem item) {
        final Spinner list = (Spinner)findViewById(R.id.lib_spinner);
        ArrayAdapter<Kanmusu> adapter;

        enableKai = !enableKai;
        if (enableKai) {
            item.setIcon(R.drawable.kai);
            adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, Core.kmlistAM);
        } else {
            item.setIcon(R.drawable.kai_off);
            adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, Core.kmlist);
        }

        list.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        try {
            Intent i = new Intent(LibraryActivity.this, MainActivity.class);
            startActivity(i);
            interstitialAd.show();
            finish();
        } catch (Exception e) {e.printStackTrace();}
    }
}
