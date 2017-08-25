package ru.zont.kancalc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

public class DropChanceActivity extends AppCompatActivity {

    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_chance);

        AdView av = (AdView)findViewById(R.id.drop_ad);
        AdRequest request = new AdRequest.Builder().build();
        av.loadAd(request);
        interstitialAd = AdShower.load(this);

        final Spinner kmlist = (Spinner)findViewById(R.id.drop_kms);
        final Spinner maps = (Spinner)findViewById(R.id.drop_maps);
        final Spinner nodes = (Spinner)findViewById(R.id.drop_nodes);
        final TextView singleChance = (TextView)findViewById(R.id.drop_singleChance);
        final Context context = this;

        ArrayAdapter<Kanmusu> kmadapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, Core.kmlist);
        kmlist.setAdapter(kmadapter);
        int selectid = getIntent().getIntExtra("select", -1);
        if (selectid!=-1)
            kmlist.setSelection(Core.findKmPos(selectid, kmlist));
        else
            kmlist.setSelection(Core.findKmPos(45, kmlist));

        kmlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                new GetDrops().execute(context);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        maps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Kanmusu.Map map = (Kanmusu.Map) maps.getSelectedItem();
                ArrayAdapter<Kanmusu.Map.Node> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, map.nodes);
                nodes.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        nodes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Kanmusu.Map.Node node = (Kanmusu.Map.Node) nodes.getSelectedItem();
                singleChance.setText(node.chance+"%");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private class GetDrops extends AsyncTask<Context, Void, Boolean> {

        final Spinner kmlist = (Spinner)findViewById(R.id.drop_kms);
        final Spinner maps = (Spinner)findViewById(R.id.drop_maps);
        final ProgressBar pb = (ProgressBar)findViewById(R.id.drop_progressBar);
        final TextView cs = (TextView)findViewById(R.id.drop_comstate);
        final TextView single = (TextView)findViewById(R.id.drop_singleChance);
        final TextView res = (TextView)findViewById(R.id.drop_chance);

        Context context;
        Kanmusu kanmusu;
        ArrayList<Kanmusu.Map> drops = null;
        Exception e;
        ArrayAdapter<Kanmusu.Map> adapter;

        @Override
        protected void onPreExecute() {
            pb.setVisibility(ProgressBar.VISIBLE);
            cs.setVisibility(TextView.VISIBLE);
            kmlist.setEnabled(false);
            kanmusu = (Kanmusu) kmlist.getSelectedItem();
            single.setText("..");
            res.setText("..");
        }

        @Override
        protected Boolean doInBackground(Context... cts) {
            context = cts[0];
            try {
                drops = KCDB.getDrops(kanmusu);
                if (drops == null)
                    return false;
                adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, drops);
                return true;
            } catch (Exception e) {this.e = e;}
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                single.setText("---");
                res.setText("ERROR");
                if (drops == null) {
                    Toast.makeText(context, R.string.err_com_kcdb, Toast.LENGTH_LONG).show();
                    end();
                    return;
                }
                e.printStackTrace();
                Toast.makeText(context, R.string.unexpected_err, Toast.LENGTH_LONG).show();
            }
            end();
            maps.setAdapter(adapter);
            kmlist.setEnabled(true);
        }

        private void end() {
            pb.setVisibility(ProgressBar.GONE);
            cs.setVisibility(TextView.GONE);
        }
    }

    public void onEnter(View v) {
        final Spinner nodes = (Spinner)findViewById(R.id.drop_nodes);
        final EditText triesView = (EditText)findViewById(R.id.drop_tries);
        final TextView res = (TextView)findViewById(R.id.drop_chance);
        final double singleChance = ((Kanmusu.Map.Node)nodes.getSelectedItem()).chance;
        final int tries = Integer.valueOf(triesView.getText().toString());

        res.setText(Core.getSumChance(singleChance, tries)+"%");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        try {
            Intent i = new Intent(DropChanceActivity.this, MainActivity.class);
            i.putExtra("notFirst", true);
            startActivity(i);
            interstitialAd.show();
            finish();
        } catch (Exception e) {e.printStackTrace();}
    }
}
