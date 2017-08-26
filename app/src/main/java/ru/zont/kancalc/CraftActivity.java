package ru.zont.kancalc;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.IOException;

public class CraftActivity extends AppCompatActivity {

    InterstitialAd interstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_craft);
        ActionBar ab = getSupportActionBar();
        if (ab != null) ab.setTitle(R.string.t_craft);

        AdView ad = (AdView)findViewById(R.id.craft_ad);
        AdRequest request = new AdRequest.Builder().build();
        ad.loadAd(request);
        interstitialAd = AdShower.load(this);

        final Context context = this;

        final Spinner kmlist = (Spinner)findViewById(R.id.craft_kmlist);
        ArrayAdapter<Kanmusu> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Core.kmlist);
        kmlist.setAdapter(adapter);
        int selectid = getIntent().getIntExtra("select", -1);
        if (selectid!=-1)
            kmlist.setSelection(Core.findKmPos(selectid, kmlist));
        else
            kmlist.setSelection(Core.findKmPos(45, kmlist));

        kmlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                new GetCC().execute(context);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    public void onEnter(View v) {
        TextView res = (TextView)findViewById(R.id.craft_result);
        int tries = Integer.valueOf(((TextView)findViewById(R.id.craft_tries)).getText().toString());
        Kanmusu kanmusu = (Kanmusu)((Spinner)findViewById(R.id.craft_kmlist)).getSelectedItem();

        res.setText(Core.getPrice(tries, kanmusu)+" - "+Core.getSumChance(kanmusu.chance, tries)+"%");
    }

    private class GetCC extends AsyncTask<Context, Void, Context> {

        private ProgressBar pb = (ProgressBar)findViewById(R.id.craft_pb);
        private TextView cs = (TextView)findViewById(R.id.craft_constate);

        private Spinner kmlist = (Spinner)findViewById(R.id.craft_kmlist);
        private Button ds = (Button)findViewById(R.id.craft_ds);
        private TextView single = (TextView)findViewById(R.id.craft_single);
        private TextView timeV = (TextView)findViewById(R.id.craft_time);
        private TextView res = (TextView)findViewById(R.id.craft_result);
        private Button enter = (Button)findViewById(R.id.craft_enter);

        private Kanmusu kanmusu;
        private String craft;
        private double chance = -1;
        private String time = "";

        @Override
        protected void onPreExecute() {
            pb.setVisibility(ProgressBar.VISIBLE);
            cs.setVisibility(TextView.VISIBLE);
            kanmusu = (Kanmusu) kmlist.getSelectedItem();
            kmlist.setEnabled(false);
            ds.setEnabled(false);
            res.setText("..");
            timeV.setText("..");
            single.setText("..");
        }

        @Override
        protected Context doInBackground(Context... contexts) {
            try {
                craft = kanmusu.craft;
                chance = KCDB.getCC(kanmusu, craft);
                time = KMParser.getConstTime(kanmusu);
            } catch (IOException e) {e.printStackTrace();}
            return contexts[0];
        }

        @Override
        protected void onPostExecute(Context context) {
            pb.setVisibility(View.GONE);
            cs.setVisibility(View.GONE);
            kmlist.setEnabled(true);
            ds.setEnabled(true);

            if (chance == -1) {
                Toast.makeText(context, getString(R.string.craft_iss_1)+craft
                        +getString(R.string.craft_iss_2), Toast.LENGTH_LONG).show();
                enter.setEnabled(false);
                res.setText("ERROR");
                single.setText("---");
                timeV.setText("---");

                return;
            }

            single.setText(craft+" - "+chance+"%");
            timeV.setText(time);
            kanmusu.chance = chance;
        }
    }

    public void toDS(View v) {
        try {
            Intent i = new Intent(CraftActivity.this, SelecterActivity.class);
            i.putExtra("from", "craft");
            startActivity(i);
            finish();
        } catch (Exception e) {e.printStackTrace();}
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            Intent i = new Intent(CraftActivity.this, MainActivity.class);
            i.putExtra("notFirst", true);
            startActivity(i);
            interstitialAd.show();
            finish();
        } catch (Exception e) {e.printStackTrace();}
    }
}
