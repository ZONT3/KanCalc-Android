package ru.zont.kancalc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

@SuppressWarnings({"unchecked", "ConstantConditions"})
public class LibraryActivity extends AppCompatActivity {

    InterstitialAd interstitialAd;

    final Context context = this;

    boolean enableKai = false;
    boolean selectKai = false;
    int selectid;

    private ArrayList<ImageView> auxCgs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        ActionBar ab = getSupportActionBar();
        if (ab != null) ab.setTitle(R.string.t_lib);

        AdView av = (AdView)findViewById(R.id.lib_ad);
        AdRequest request = new AdRequest.Builder().build();
        av.loadAd(request);
        interstitialAd = AdShower.load(this);

        final Spinner kmlist = (Spinner)findViewById(R.id.lib_spinner);
        final Spinner models = (Spinner)findViewById(R.id.lib_otherVers);
        ArrayAdapter<Kanmusu> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Core.kmlist);
        kmlist.setAdapter(adapter);
        selectid = getIntent().getIntExtra("select", -1);
        if (selectid!=-1) {
            int pos = Core.findKmPos(selectid, kmlist);
            if (pos>=0) kmlist.setSelection(pos);
            else {
                Kanmusu k = Core.getKanmusu(selectid, Core.kmlistAM).getBase();
                kmlist.setSelection(Core.findKmPos(k.id, kmlist));
                selectKai = true;
            }
        }

        kmlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Kanmusu kanmusu = (Kanmusu) kmlist.getSelectedItem();
                ArrayAdapter<Kanmusu> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, kanmusu.remodels);
                if (!enableKai) models.setAdapter(adapter);
                else parseInfo(kanmusu);

                if (selectKai) models.setSelection(Core.findKmPos(selectid, models));
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
        ArrayList<Kanmusu> list;
        switch (item.getItemId()) {
            case R.id.lib_sort_type:
                list = (ArrayList<Kanmusu>) cloneList(enableKai ? Core.kmlistAM : Core.kmlist);
                Core.kmsortType(list);
                break;
            case R.id.lib_sort_class:
                list = (ArrayList<Kanmusu>) cloneList(enableKai ? Core.kmlistAM : Core.kmlist);
                Core.kmsortClass(list);
                break;
            case R.id.lib_sort_name:
                list = (ArrayList<Kanmusu>) cloneList(enableKai ? Core.kmlistAM : Core.kmlist);
                Core.kmsortName(list);
                break;
            case R.id.lib_sort_id:
                list = (ArrayList<Kanmusu>) cloneList(enableKai ? Core.kmlistAM : Core.kmlist);
                Core.kmsortId(list);
                break;
            case R.id.lib_sort_nid:
                list = (ArrayList<Kanmusu>) cloneList(enableKai ? Core.kmlistAM : Core.kmlist);
                Core.kmsortNid(list);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        ArrayAdapter<Kanmusu> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, list);
        ((Spinner)findViewById(R.id.lib_spinner)).setAdapter(adapter);
        return true;
    }

    private Object cloneList(ArrayList<Kanmusu> list) {
        Object res = null;
        try {
            File f = new File(getCacheDir(), "clone");
            FileOutputStream out = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(list);
            oos.flush();
            oos.close();
            FileInputStream in = new FileInputStream(f);
            ObjectInputStream oin = new ObjectInputStream(in);
            res = oin.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return res;
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
        final TextView name = (TextView)findViewById(R.id.lib_name);
        final TextView jpname = (TextView)findViewById(R.id.lib_jpname);
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
        final TextView slots = (TextView)findViewById(R.id.lib_slots);

        LinearLayout mainLayout = (LinearLayout)findViewById(R.id.lib_mainLayout);
        final ImageView preview = (ImageView)findViewById(R.id.lib_preview);
        final ImageView cg = (ImageView)findViewById(R.id.lib_cg);
        final ImageView cgD = (ImageView)findViewById(R.id.lib_cg_d);

        id.setText(kanmusu.id+"");
        nid.setText(kanmusu.nid+"");
        name.setText(kanmusu.type+" "+kanmusu.name);
        jpname.setText(kanmusu.jpname);
        if (!kanmusu.isBase())
            jpname.setText(jpname.getText()+" (Lv."+kanmusu.level+")");

        ArrayList<Kanmusu.Stats> stats = kanmusu.getParcingStats();
        for (int i=0; i<arr.size(); i++) {
            if (stats.get(i).toString()!=null)
                arr.get(i).setText(stats.get(i).toString());
            else {
                arr.get(i).setText("??");
                if (getPreferences(MODE_PRIVATE).getBoolean("libWarn", true)) {
                    Toast.makeText(this, R.string.lib_warning, Toast.LENGTH_LONG).show();
                    getPreferences(MODE_PRIVATE).edit().putBoolean("libWarn", false).apply();
                }
            }
        }

        slots.setText("");
        if (kanmusu.slots!=null) {
            int total = 0;
            for (int s : kanmusu.slots) {slots.setText(slots.getText() + " [" + s + "]");total+=s;}
            slots.setText(slots.getText()+" = "+total);
        } else slots.setText("---");



        int pwId = getResources().getIdentifier("pw_"+kanmusu.id, "drawable", getPackageName());
        int pwIdB = getResources().getIdentifier("pw_"+kanmusu.getBase().id, "drawable", getPackageName());
        if (pwId!=0)
            preview.setImageResource(pwId);
        else if (pwIdB!=0)
            preview.setImageResource(pwIdB);
        else
            preview.setImageResource(R.drawable.logo);

        int cgId = getResources().getIdentifier("cg_"+kanmusu.id, "drawable", getPackageName());
        int cgIdB = getResources().getIdentifier("cg_"+kanmusu.getBase().id, "drawable", getPackageName());
        if (cgId!=0)
            cg.setImageResource(cgId);
        else if (cgIdB!=0)
            cg.setImageResource(cgIdB);
        else
            cg.setImageResource(R.drawable.logo);

        int dCgId = getResources().getIdentifier("cgd_"+kanmusu.id, "drawable", getPackageName());
        int dCgIdB = getResources().getIdentifier("cgd_"+kanmusu.getBase().id, "drawable", getPackageName());
        if (dCgId!=0)
            cgD.setImageResource(dCgId);
        else if (dCgIdB!=0)
            cgD.setImageResource(dCgIdB);
        else
            cgD.setImageResource(R.drawable.logo);

        for (ImageView v : auxCgs) {mainLayout.removeView(v); v.destroyDrawingCache();}
        auxCgs = new ArrayList<>();

        int i=0;
        int auxCgId = getResources().getIdentifier("cga_"+kanmusu.id+"_"+i, "drawable", getPackageName());
        while (auxCgId!=0) {
            ImageView auxCg = new ImageView(this);
            auxCg.setLayoutParams(cg.getLayoutParams());
            auxCg.setImageResource(auxCgId);
            mainLayout.addView(auxCg);
            auxCgs.add(auxCg);

            i++;
            auxCgId = getResources().getIdentifier("cga_"+kanmusu.id+"_"+i, "drawable", getPackageName());
        }
    }

    public void toggleKai(MenuItem item) {
        final Spinner list = (Spinner)findViewById(R.id.lib_spinner);
        final Spinner mods = (Spinner)findViewById(R.id.lib_otherVers);
        ArrayAdapter<Kanmusu> adapter;

        enableKai=!enableKai;
        if (enableKai) {
            item.setIcon(R.drawable.kai);
            adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, Core.kmlistAM);
            mods.setVisibility(View.GONE);
        } else {
            item.setIcon(R.drawable.kai_off);
            adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, Core.kmlist);
            mods.setVisibility(View.VISIBLE);
        }

        list.setAdapter(adapter);
    }

    public void gotoDrop(MenuItem item) {
        Intent i = new Intent(LibraryActivity.this, DropChanceActivity.class);
        i.putExtra("select", ((Kanmusu)((Spinner)findViewById(R.id.lib_otherVers)).getSelectedItem()).getBase().id);
        startActivity(i);
        finish();
    }

    public void gotoCraft(MenuItem item) {
        if (((Kanmusu)((Spinner)findViewById(R.id.lib_otherVers)).getSelectedItem()).getBase().craft.equals("unbuildable")) {
            Toast.makeText(this, R.string.lib_unbuildable, Toast.LENGTH_LONG).show();
            return;
        }

        Intent i = new Intent(LibraryActivity.this, CraftActivity.class);
        i.putExtra("select", ((Kanmusu)((Spinner)findViewById(R.id.lib_otherVers)).getSelectedItem()).getBase().id);
        startActivity(i);
        finish();
    }

    public void toDS(View v) {
        try {
            Intent i = new Intent(LibraryActivity.this, SelecterActivity.class);
            i.putExtra("from", "lib");
            i.putExtra("kai", true);
            startActivity(i);
            finish();
        } catch (Exception e) {e.printStackTrace();}
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        try {
            Intent i = new Intent(LibraryActivity.this, MainActivity.class);
            i.putExtra("notFirst", true);
            startActivity(i);
            interstitialAd.show();
            finish();
        } catch (Exception e) {e.printStackTrace();}
    }
}
