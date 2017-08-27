package ru.zont.kancalc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class SetupEditorActivity extends AppCompatActivity {

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_editor);
        ActionBar ab = getSupportActionBar();
        if (ab != null) ab.setTitle(R.string.t_setup);

        AdView av = (AdView)findViewById(R.id.se_ad);
        AdRequest request = new AdRequest.Builder().build();
        av.loadAd(request);

        ArrayList<Spinner> setupSpinners = new ArrayList<>();
        final Spinner km1 = (Spinner)findViewById(R.id.se_spinner1); setupSpinners.add(km1);
        final Spinner km2 = (Spinner)findViewById(R.id.se_spinner2); setupSpinners.add(km2);
        final Spinner km3 = (Spinner)findViewById(R.id.se_spinner3); setupSpinners.add(km3);
        final Spinner km4 = (Spinner)findViewById(R.id.se_spinner4); setupSpinners.add(km4);
        final Spinner km5 = (Spinner)findViewById(R.id.se_spinner5); setupSpinners.add(km5);
        final Spinner km6 = (Spinner)findViewById(R.id.se_spinner6); setupSpinners.add(km6);
        ArrayAdapter<Kanmusu> a = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Core.kmlistAM);
        km1.setAdapter(a);
        km2.setAdapter(a);
        km3.setAdapter(a);
        km4.setAdapter(a);
        km5.setAdapter(a);
        km6.setAdapter(a);

        File savedSetup = new File(getFilesDir(), "last.ss");
        if (savedSetup.exists()) {
            ArrayList<Integer> saved = null;
            try {
                FileInputStream in = new FileInputStream(savedSetup);
                ObjectInputStream ois = new ObjectInputStream(in);
                saved = (ArrayList<Integer>) ois.readObject();
                ois.close();
            } catch (java.io.IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (saved != null)
                for (int i = 0; i < setupSpinners.size(); i++)
                    setupSpinners.get(i).setSelection(saved.get(i));
        }

        int slot = getIntent().getIntExtra("slot", 0);
        int select = getIntent().getIntExtra("select", -1);
        int selectInd = Core.findKmPos(select, km1);
        switch (slot) {
            case 1: km1.setSelection(selectInd); break;
            case 2: km2.setSelection(selectInd); break;
            case 3: km3.setSelection(selectInd); break;
            case 4: km4.setSelection(selectInd); break;
            case 5: km5.setSelection(selectInd); break;
            case 6: km6.setSelection(selectInd); break;
            default: break;
        }

        final Button ds1 = (Button)findViewById(R.id.se_ds1);
        final Button ds2 = (Button)findViewById(R.id.se_ds2);
        final Button ds3 = (Button)findViewById(R.id.se_ds3);
        final Button ds4 = (Button)findViewById(R.id.se_ds4);
        final Button ds5 = (Button)findViewById(R.id.se_ds5);
        final Button ds6 = (Button)findViewById(R.id.se_ds6);

        ds1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {toDs(1);}
        });
        ds2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {toDs(2);}
        });
        ds3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {toDs(3);}
        });
        ds4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {toDs(4);}
        });
        ds5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {toDs(5);}
        });
        ds6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {toDs(6);}
        });
    }

    private void toDs(int i) {
        Intent intent = new Intent(SetupEditorActivity.this, SelecterActivity.class);
        intent.putExtra("kai", true);
        intent.putExtra("from", "se");
        intent.putExtra("slot", i);
        saveSetup();
        startActivity(intent);
        finish();
    }

    public void onOK(View v) {onBackPressed();}

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        try {
            saveSetup();

            Intent i = new Intent(SetupEditorActivity.this, FarmActivity.class);
            startActivity(i);
            finish();
        } catch (Exception e) {e.printStackTrace();}
    }

    private void saveSetup() {
        ArrayList<Spinner> setupSpinners = new ArrayList<>();
        final Spinner km1 = (Spinner)findViewById(R.id.se_spinner1); setupSpinners.add(km1);
        final Spinner km2 = (Spinner)findViewById(R.id.se_spinner2); setupSpinners.add(km2);
        final Spinner km3 = (Spinner)findViewById(R.id.se_spinner3); setupSpinners.add(km3);
        final Spinner km4 = (Spinner)findViewById(R.id.se_spinner4); setupSpinners.add(km4);
        final Spinner km5 = (Spinner)findViewById(R.id.se_spinner5); setupSpinners.add(km5);
        final Spinner km6 = (Spinner)findViewById(R.id.se_spinner6); setupSpinners.add(km6);

        try {
            ArrayList<Integer> composition = new ArrayList<>();
            for (int i=0; i<setupSpinners.size(); i++)
                composition.add(setupSpinners.get(i).getSelectedItemPosition());
            File dir = new File(getFilesDir(), "last.ss");
            FileOutputStream out = new FileOutputStream(dir);
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(composition);
            oos.flush();
            oos.close();

            Kanmusu[] setup = new Kanmusu[6];
            for (int i=0; i<composition.size(); i++)
                setup[i] = (Kanmusu) setupSpinners.get(i).getItemAtPosition(composition.get(i));
            File dir2 = new File(getFilesDir(), "last.sskm");
            FileOutputStream out2 = new FileOutputStream(dir2);
            ObjectOutputStream oos2 = new ObjectOutputStream(out2);
            oos2.writeObject(setup);
            oos2.flush();
            oos2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
