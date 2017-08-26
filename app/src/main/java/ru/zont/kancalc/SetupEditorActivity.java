package ru.zont.kancalc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
        if (!savedSetup.exists())
            return;
        ArrayList<Integer> saved = null;
        try {
            FileInputStream in = new FileInputStream(savedSetup);
            ObjectInputStream ois = new ObjectInputStream(in);
            saved = (ArrayList<Integer>) ois.readObject();
            ois.close();
        } catch (java.io.IOException | ClassNotFoundException e) {e.printStackTrace();}
        if (saved == null)
            return;
        for (int i=0; i<setupSpinners.size(); i++)
            setupSpinners.get(i).setSelection(saved.get(i));
    }

    public void onOK(View v) {onBackPressed();}

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        ArrayList<Spinner> setupSpinners = new ArrayList<>();
        final Spinner km1 = (Spinner)findViewById(R.id.se_spinner1); setupSpinners.add(km1);
        final Spinner km2 = (Spinner)findViewById(R.id.se_spinner2); setupSpinners.add(km2);
        final Spinner km3 = (Spinner)findViewById(R.id.se_spinner3); setupSpinners.add(km3);
        final Spinner km4 = (Spinner)findViewById(R.id.se_spinner4); setupSpinners.add(km4);
        final Spinner km5 = (Spinner)findViewById(R.id.se_spinner5); setupSpinners.add(km5);
        final Spinner km6 = (Spinner)findViewById(R.id.se_spinner6); setupSpinners.add(km6);


        Kanmusu[] setup = new Kanmusu[6];
        for (int i=0; i<setupSpinners.size(); i++)
            setup[i] = (Kanmusu) setupSpinners.get(i).getSelectedItem();


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

            File setupFile = new File(getCacheDir(), "setup");
            FileOutputStream out2 = new FileOutputStream(setupFile);
            ObjectOutputStream oos2 = new ObjectOutputStream(out2);
            oos2.writeObject(setup);
            oos2.flush();
            oos2.close();

            Intent i = new Intent(SetupEditorActivity.this, FarmActivity.class);
            startActivity(i);
            finish();
        } catch (Exception e) {e.printStackTrace();}
    }
}
