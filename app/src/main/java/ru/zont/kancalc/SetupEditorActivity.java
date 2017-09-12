package ru.zont.kancalc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
    private static final String setupsDirStr = "setups";

    private Menu optionsMenu;
    private ArrayList<MenuItem> setupItems = new ArrayList<>();

    ArrayList<Spinner> setupSpinners;

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

        File f = new File(getFilesDir(), setupsDirStr);
        if (!f.exists()) f.mkdir();

        setupSpinners = new ArrayList<>();
        final Spinner km1 = (Spinner)findViewById(R.id.se_spinner1); setupSpinners.add(km1);
        final Spinner km2 = (Spinner)findViewById(R.id.se_spinner2); setupSpinners.add(km2);
        final Spinner km3 = (Spinner)findViewById(R.id.se_spinner3); setupSpinners.add(km3);
        final Spinner km4 = (Spinner)findViewById(R.id.se_spinner4); setupSpinners.add(km4);
        final Spinner km5 = (Spinner)findViewById(R.id.se_spinner5); setupSpinners.add(km5);
        final Spinner km6 = (Spinner)findViewById(R.id.se_spinner6); setupSpinners.add(km6);
        ArrayAdapter<Kanmusu> a = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Core.kmlistAM);
        for (Spinner s : setupSpinners) s.setAdapter(a);

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

    private boolean loadSetup(File save) {
        Kanmusu[] setup = null;
        try {
            FileInputStream in = new FileInputStream(save);
            ObjectInputStream ois = new ObjectInputStream(in);
            setup = (Kanmusu[]) ois.readObject();
            ois.close();
        } catch (Exception e) {e.printStackTrace();}
        if (setup==null) return false;

        try {
            for (int i = 0; i < setup.length; i++)
                setupSpinners.get(i).setSelection(Core.findKmPos(setup[i].id, setupSpinners.get(i)));
        } catch (Exception e) {e.printStackTrace(); return false;}
        return true;
    }

    private void addSetup(File file, Menu menu) {
        MenuItem item = menu.add(file.getName().substring(0, file.getName().length()-6));
        final File save = file;

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {return loadSetup(save);}
        });
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        setupItems.add(item);
    }

    private boolean createSetup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SetupEditorActivity.this);
        View view = LayoutInflater.from(SetupEditorActivity.this).inflate(R.layout.dialog_setup_editor, null);
        final EditText name = (EditText) view.findViewById(R.id.se_add_name);

        final Kanmusu[] setup = new Kanmusu[6];
        for (int i=0; i<setupSpinners.size(); i++)
            setup[i] = (Kanmusu) setupSpinners.get(i).getSelectedItem();

        builder.setTitle(R.string.se_create)
                .setIcon(R.drawable.plus)
                .setCancelable(true)
                .setView(view)
                .setPositiveButton(R.string.enter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            String na = name.getText().toString();
                            if (na.contains("\\") || na.contains(":") || na.contains("\'") || na.contains("\"")
                                    || na.contains("|") || na.contains("/") || na.contains(";") || na.contains(".")
                                    || na.contains(",") || na.equals("")) {
                                Toast.makeText(SetupEditorActivity.this, R.string.iinpt, Toast.LENGTH_LONG).show();
                                return;
                            }

                            File file = new File(getFilesDir(), setupsDirStr+"/"+na+".setup");
                            FileOutputStream out = new FileOutputStream(file);
                            ObjectOutputStream oout = new ObjectOutputStream(out);
                            oout.writeObject(setup);
                            oout.flush();
                            oout.close();
                            addSetup(file, optionsMenu);
                        } catch (Exception e) {e.printStackTrace();}
                    }});

        builder.create().show();
        return true;
    }

    private boolean deleteSetup() {
        if (setupItems==null) return false;
        if (setupItems.size()==0) return false;

        AlertDialog.Builder builder = new AlertDialog.Builder(SetupEditorActivity.this);
        final boolean[] checkStates = new boolean[setupItems.size()];
        final String[] setupStrs = new String[setupItems.size()];

        for (boolean b : checkStates) b = false;
        for (int i=0; i<setupStrs.length; i++) setupStrs[i] = setupItems.get(i).getTitle().toString();

        builder.setTitle(R.string.se_delete)
                .setCancelable(true)
                .setMultiChoiceItems(setupStrs, checkStates, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {checkStates[i] = b;}})
                .setPositiveButton(R.string.enter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (int j=0; j<checkStates.length; j++) {
                            if (checkStates[j]) {
                                File file = new File(getFilesDir(), setupsDirStr+"/"+setupStrs[j]+".setup");
                                file.delete();
                                for (int k=0; k<optionsMenu.size(); k++)
                                    if (optionsMenu.getItem(k)==setupItems.get(j))
                                        optionsMenu.removeItem(k);
                            }
                        }
                    }
                });

        builder.create().show();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            File setupsDir = new File(getFilesDir(), setupsDirStr);
            File[] setups = setupsDir.listFiles();

            for (File file : setups) {
                addSetup(file, menu);
            }
        } catch (Exception e) {e.printStackTrace();}

        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.setup, menu);
        optionsMenu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.se_add: return createSetup();
            case R.id.se_delete: return deleteSetup();
            default: return super.onOptionsItemSelected(item);
        }
    }
}
