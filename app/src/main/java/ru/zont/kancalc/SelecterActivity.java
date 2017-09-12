package ru.zont.kancalc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class SelecterActivity extends AppCompatActivity {
    private String DD;
    private String CL;
    private String CA;
    private String BB;
    private String CLT;
    private String CAV;
    private String BBV;
    private String FBB;
    private String CV;
    private String CVL;
    private String CVB;
    private String SS;
    private String AUX;
    private String DE;
    
    private enum MenuList {types, classes, ships, models}
    private MenuList menu;

    ArrayList<LinearLayout> louts = new ArrayList<>();

    boolean kai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecter);

        DD = getString(R.string.type_dd);
        CL = getString(R.string.type_cl);
        CA = getString(R.string.type_ca);
        BB = getString(R.string.type_bb);
        CLT = getString(R.string.type_clt);
        CAV = getString(R.string.type_cav);
        BBV = getString(R.string.type_bbv);
        FBB = getString(R.string.type_fbb);
        CV = getString(R.string.type_cv);
        CVL = getString(R.string.type_cvl);
        CVB = getString(R.string.type_cvb);
        SS = getString(R.string.type_ss);
        AUX = getString(R.string.type_aux);
        DE = getString(R.string.type_de);

        kai = getIntent().getBooleanExtra("kai", false);

        showTypes();
    }

    private void showTypes() {
        clearActivity();
        menu = MenuList.types;
        ActionBar ab = getSupportActionBar();
        if (ab != null) ab.setTitle(R.string.ds_s_type);

        LinearLayout lay = (LinearLayout)findViewById(R.id.as_lay);
        Button sample = (Button)findViewById(R.id.as_sample);
        TextView tsample = (TextView)findViewById(R.id.as_tsample);
        LinearLayout lsample = (LinearLayout)findViewById(R.id.as_lsample);
        ArrayList<String> types = Core.getTypes();

        for (final String type : types) {
            final Button button = new Button(this);
            final TextView tw = new TextView(this);
            final LinearLayout l = new LinearLayout(this);
            button.setLayoutParams(sample.getLayoutParams());
            button.setBackground(sample.getBackground());
            tw.setTypeface(tsample.getTypeface());
            tw.setLayoutParams(tsample.getLayoutParams());
            tw.setGravity(tsample.getGravity());
            tw.setPadding(0,0,4,0);
            l.setLayoutParams(lsample.getLayoutParams());
            l.setGravity(Gravity.CENTER_HORIZONTAL);
            l.setOrientation(LinearLayout.HORIZONTAL);

            switch (type) {
                case "DD":
                    button.setBackgroundResource(R.drawable.pw_144);
                    tw.setText(DD);
                    break;
                case "CL":
                    button.setBackgroundResource(R.drawable.pw_158);
                    tw.setText(CL);
                    break;
                case "CA":
                    button.setBackgroundResource(R.drawable.pw_416);
                    tw.setText(CA);
                    break;
                case "BB":
                    button.setBackgroundResource(R.drawable.pw_341);
                    tw.setText(BB);
                    break;
                case "CLT":
                    button.setBackgroundResource(R.drawable.pw_119);
                    tw.setText(CLT);
                    break;
                case "CAV":
                    button.setBackgroundResource(R.drawable.pw_129);
                    tw.setText(CAV);
                    break;
                case "BBV":
                    button.setBackgroundResource(R.drawable.pw_412);
                    tw.setText(BBV);
                    break;
                case "FBB":
                    button.setBackgroundResource(R.drawable.pw_178);
                    tw.setText(FBB);
                    break;
                case "CV":
                    button.setBackgroundResource(R.drawable.pw_111);
                    tw.setText(CV);
                    break;
                case "CVL":
                    button.setBackgroundResource(R.drawable.pw_281);
                    tw.setText(CVL);
                    break;
                case "CVB":
                    button.setBackgroundResource(R.drawable.pw_153);
                    tw.setText(CVB);
                    break;
                case "SS":
                    button.setBackgroundResource(R.drawable.pw_191);
                    tw.setText(SS);
                    break;
                case "DE":
                    button.setBackgroundResource(R.drawable.pw_518);
                    tw.setText(DE);
                    break;
                case "AR":
                    button.setBackgroundResource(R.drawable.pw_187);
                    tw.setText(AUX);
                    break;
                default: continue;
            }


            l.addView(tw);
            l.addView(button);
            lay.addView(l);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {tw.callOnClick();}
            });
            tw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {showClasses(((TextView)view).getText().toString());}
            });

            louts.add(l);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void showClasses(String type) {
        clearActivity();
        menu = MenuList.classes;
        ActionBar ab = getSupportActionBar();
        if (ab != null) ab.setTitle(R.string.ds_s_class);

        if (type.equals(DD)) type = "DD";
        if (type.equals(CA)) type = "CA";
        if (type.equals(CL)) type = "CL";
        if (type.equals(BB)) type = "BB";
        if (type.equals(CLT)) type = "CLT";
        if (type.equals(CAV)) type = "CAV";
        if (type.equals(BBV)) type = "BBV";
        if (type.equals(FBB)) type = "FBB";
        if (type.equals(CV)) type = "CV";
        if (type.equals(CVL)) type = "CVL";
        if (type.equals(CVB)) type = "CVB";
        if (type.equals(SS)) type = "SS";
        if (type.equals(DE)) type = "DE";
        if (type.equals(AUX)) type = "AUX";

        LinearLayout lay = (LinearLayout)findViewById(R.id.as_lay);
        Button sample = (Button)findViewById(R.id.as_sample);
        TextView tsample = (TextView)findViewById(R.id.as_tsample);
        LinearLayout lsample = (LinearLayout)findViewById(R.id.as_lsample);
        ArrayList<String> classes;
        switch (type) {
            case "AUX":
                classes = Core.getClasses("AR", Core.kmlistAM);
                classes.addAll(Core.getClasses("AO", Core.kmlistAM));
                classes.addAll(Core.getClasses("AS", Core.kmlistAM));
                classes.addAll(Core.getClasses("LHA", Core.kmlistAM));
                classes.addAll(Core.getClasses("AV", Core.kmlistAM));
                break;
            case "SS":
                classes = Core.getClasses("SS", Core.kmlistAM);
                classes.addAll(Core.getClasses("SSV", Core.kmlistAM));
                break;
            default:
                classes = Core.getClasses(type, Core.kmlistAM);
                break;
        }

        if (classes.size()==1) {showShips(classes.get(0));return;}
        for (final String cls : classes) {
            Kanmusu nameship = Core.getKanmusu(cls, Core.kmlist);
            if (nameship==null)
                for (Kanmusu km : Core.kmlistAM)
                    if (km.cls.equals(cls))
                        nameship = km;

            final Button button = new Button(this);
            final TextView tw = new TextView(this);
            final LinearLayout l = new LinearLayout(this);
            button.setLayoutParams(sample.getLayoutParams());
            button.setBackground(sample.getBackground());
            tw.setTypeface(tsample.getTypeface());
            tw.setLayoutParams(tsample.getLayoutParams());
            tw.setGravity(tsample.getGravity());
            tw.setPadding(0,0,4,0);
            l.setLayoutParams(lsample.getLayoutParams());
            l.setGravity(Gravity.CENTER_HORIZONTAL);
            l.setOrientation(LinearLayout.HORIZONTAL);

            int pw = getResources().getIdentifier("pw_"+nameship.id, "drawable", getPackageName());
            int pwB = getResources().getIdentifier("pw_"+nameship.getBase().id, "drawable", getPackageName());
            if (pw!=0) button.setBackgroundResource(pw);
            else if (pwB!=0) button.setBackgroundResource(pwB);
            tw.setText(cls);

            l.addView(tw);
            l.addView(button);
            lay.addView(l);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {tw.callOnClick();}
            });
            tw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {showShips(((TextView)view).getText().toString());}
            });

            louts.add(l);
        }
    }

    private void showShips(String cls) {
        clearActivity();
        menu = MenuList.ships;
        ActionBar ab = getSupportActionBar();
        if (ab != null) ab.setTitle(R.string.ds_s_ship);

        LinearLayout lay = (LinearLayout)findViewById(R.id.as_lay);
        Button sample = (Button)findViewById(R.id.as_sample);
        TextView tsample = (TextView)findViewById(R.id.as_tsample);
        LinearLayout lsample = (LinearLayout)findViewById(R.id.as_lsample);
        ArrayList<Kanmusu> kms = Core.getKmsByClass(cls, Core.kmlist);

        if (kms.size()==1) {showModels(kms.get(0).toString());return;}
        for (Kanmusu kanmusu : kms) {
            final Button button = new Button(this);
            final TextView tw = new TextView(this);
            final LinearLayout l = new LinearLayout(this);
            button.setLayoutParams(sample.getLayoutParams());
            button.setBackground(sample.getBackground());
            tw.setTypeface(tsample.getTypeface());
            tw.setLayoutParams(tsample.getLayoutParams());
            tw.setGravity(tsample.getGravity());
            tw.setPadding(0,0,4,0);
            l.setLayoutParams(lsample.getLayoutParams());
            l.setGravity(Gravity.CENTER_HORIZONTAL);
            l.setOrientation(LinearLayout.HORIZONTAL);

            int pw = getResources().getIdentifier("pw_"+kanmusu.id, "drawable", getPackageName());
            int pwB = getResources().getIdentifier("pw_"+kanmusu.getBase().id, "drawable", getPackageName());
            if (pw!=0) button.setBackgroundResource(pw);
            else if (pwB!=0) button.setBackgroundResource(pwB);
            tw.setText(kanmusu.toString());

            l.addView(tw);
            l.addView(button);
            lay.addView(l);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {tw.callOnClick();}
            });
            tw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {showModels(((TextView)view).getText().toString());}
            });

            louts.add(l);
        }
    }

    private void showModels(String kmStr) {
        clearActivity();
        menu = MenuList.models;
        ActionBar ab = getSupportActionBar();
        if (ab != null) ab.setTitle(R.string.ds_s_model);

        Kanmusu kanmusu = null;
        for (Kanmusu km : Core.kmlist)
            if (km.toString().equals(kmStr))
                kanmusu = km;
        if (kanmusu == null)
            return;

        if (!kai)
            done(kanmusu.toString());

        LinearLayout lay = (LinearLayout)findViewById(R.id.as_lay);
        Button sample = (Button)findViewById(R.id.as_sample);
        TextView tsample = (TextView)findViewById(R.id.as_tsample);
        LinearLayout lsample = (LinearLayout)findViewById(R.id.as_lsample);

        for (Kanmusu model : kanmusu.remodels) {
            final Button button = new Button(this);
            final TextView tw = new TextView(this);
            final LinearLayout l = new LinearLayout(this);
            button.setLayoutParams(sample.getLayoutParams());
            button.setBackground(sample.getBackground());
            tw.setTypeface(tsample.getTypeface());
            tw.setLayoutParams(tsample.getLayoutParams());
            tw.setGravity(tsample.getGravity());
            tw.setPadding(0,0,4,0);
            l.setLayoutParams(lsample.getLayoutParams());
            l.setGravity(Gravity.CENTER_HORIZONTAL);
            l.setOrientation(LinearLayout.HORIZONTAL);

            int pw = getResources().getIdentifier("pw_"+model.id, "drawable", getPackageName());
            int pwB = getResources().getIdentifier("pw_"+kanmusu.getBase().id, "drawable", getPackageName());
            if (pw!=0) button.setBackgroundResource(pw);
            else if (pwB!=0) button.setBackgroundResource(pwB);
            tw.setText(model.toString());

            l.addView(tw);
            l.addView(button);
            lay.addView(l);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {tw.callOnClick();}
            });
            tw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {done(((TextView)view).getText().toString());}
            });

            louts.add(l);
        }
    }

    private void clearActivity() {
        for (LinearLayout b : louts) {
            ((LinearLayout) findViewById(R.id.as_lay)).removeView(b);
            b.destroyDrawingCache();
        }
        louts = new ArrayList<>();
    }

    @SuppressWarnings("ConstantConditions")
    private void done(String s) {
        Kanmusu kanmusu = null;
        for (Kanmusu k : Core.kmlistAM)
            if (k.toString().equals(s))
                kanmusu = k;

        String from = getIntent().getStringExtra("from");
        if (kanmusu == null) from = "";
        switch (from) {
            case "lib":
                try {
                    Intent i = new Intent(SelecterActivity.this, LibraryActivity.class);
                    i.putExtra("select", kanmusu.id);
                    startActivity(i);
                    finish();
                } catch (Exception e) {e.printStackTrace();}
                break;
            case "farm":
                try {
                    Intent i = new Intent(SelecterActivity.this, FarmActivity.class);
                    i.putExtra("select", kanmusu.id);
                    startActivity(i);
                    finish();
                } catch (Exception e) {e.printStackTrace();}
                break;
            case "drop":
                try {
                    Intent i = new Intent(SelecterActivity.this, DropChanceActivity.class);
                    i.putExtra("select", kanmusu.id);
                    startActivity(i);
                    finish();
                } catch (Exception e) {e.printStackTrace();}
                break;
            case "craft":
                try {
                    Intent i = new Intent(SelecterActivity.this, CraftActivity.class);
                    i.putExtra("select", kanmusu.id);
                    startActivity(i);
                    finish();
                } catch (Exception e) {e.printStackTrace();}
                break;
            case "se":
                try {
                    Intent i = new Intent(SelecterActivity.this, SetupEditorActivity.class);
                    i.putExtra("slot", getIntent().getIntExtra("slot", 0));
                    i.putExtra("select", kanmusu.id);
                    startActivity(i);
                    finish();
                } catch (Exception e) {e.printStackTrace();}
                break;
            default:
                Toast.makeText(this, getString(R.string.unexpected_err), Toast.LENGTH_LONG).show();
                try {
                    Intent i = new Intent(SelecterActivity.this, MainActivity.class);
                    i.putExtra("notFirst", true);
                    startActivity(i);
                    finish();
                } catch (Exception e) {e.printStackTrace();}
                break;
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onBackPressed() {
        switch (menu) {
            case types: done(Core.getKanmusu(45, Core.kmlist).toString()); break;
            default: showTypes();
        }
    }
}
