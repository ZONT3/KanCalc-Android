package ru.zont.kancalc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //resetSetups();

        MobileAds.initialize(this, "ca-app-pub-7799305268524604~4205778796");
        AdView av = (AdView)findViewById(R.id.main_ad);
        AdRequest request = new AdRequest.Builder().build();
        av.loadAd(request);

        boolean notFirst = getIntent().getBooleanExtra("notFirst", false);
        if (!notFirst)
            new CoreInit().execute(this);
        if (!hasConnection(this)) {
            ((TextView)findViewById(R.id.main_concheck)).setText(R.string.err_main_inet);
            findViewById(R.id.main_pb).setVisibility(View.GONE);
            findViewById(R.id.main_bt_drop).setEnabled(false);
            findViewById(R.id.main_bt_craft).setEnabled(false);
        } else if (!notFirst)
            new CheckConnect().execute(this);
        else {
            SharedPreferences p = getPreferences(MODE_PRIVATE);
            showConInfo(p.getBoolean("hasConnection", true));
        }

        if (!notFirst)
            getPreferences(MODE_PRIVATE).edit().putBoolean("libWarn", true).apply();

        final TextView ver = (TextView)findViewById(R.id.main_ver);
        ver.setText(getString(R.string.main_copyright_1)+Core.version+getString(R.string.main_copyright_2));
    }

    public void toCC(View v) {
        try {
            Intent i = new Intent(MainActivity.this, ChanceCalcActivity.class);
            startActivity(i);
            finish();
        } catch (Exception e) {e.printStackTrace();}
    }

    public void toFarm(View v) {
        try {
            Intent i = new Intent(MainActivity.this, FarmActivity.class);
            startActivity(i);
            finish();
        } catch (Exception e) {e.printStackTrace();}
    }

    public void toDrop(View v) {
        try {
            Intent i = new Intent(MainActivity.this, DropChanceActivity.class);
            startActivity(i);
            finish();
        } catch (Exception e) {e.printStackTrace();}
    }

    public void toLib(View v) {
        try {
            Intent i = new Intent(MainActivity.this, LibraryActivity.class);
            i.putExtra("select", 144);
            startActivity(i);
            finish();
        } catch (Exception e) {e.printStackTrace();}
    }

    public void toCraft(View v) {
        try {
            Intent i = new Intent(MainActivity.this, CraftActivity.class);
            startActivity(i);
            finish();
        } catch (Exception e) {e.printStackTrace();}
    }

    private static boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
            return true;
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
            return true;
        wifiInfo = cm.getActiveNetworkInfo();
        return wifiInfo != null && wifiInfo.isConnected();
    }

    private class CoreInit extends AsyncTask<Context, Void, Context> {
        ProgressBar pb = (ProgressBar)findViewById(R.id.main_pbi);
        TextView tw = (TextView)findViewById(R.id.main_initst);
        final Button craft = (Button)findViewById(R.id.main_bt_craft);
        final Button drop = (Button)findViewById(R.id.main_bt_drop);
        final Button chance = (Button)findViewById(R.id.main_bt_proke);
        final Button farm = (Button)findViewById(R.id.main_bt_farm);
        final Button lib = (Button)findViewById(R.id.main_bt_library);

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
            tw.setVisibility(View.VISIBLE);
            craft.setEnabled(false);
            drop.setEnabled(false);
            chance.setEnabled(false);
            farm.setEnabled(false);
            lib.setEnabled(false);
        }

        @Override
        protected Context doInBackground(Context... contexts) {
            try {
                Core.init(contexts[0]);
            } catch (IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
            }

            return contexts[0];
        }

        @Override
        protected void onPostExecute(Context context) {
            pb.setVisibility(View.GONE);
            tw.setVisibility(View.GONE);
            chance.setEnabled(true);
            farm.setEnabled(true);
            lib.setEnabled(true);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private class CheckConnect extends AsyncTask<Context, Void, Context> {

        final ProgressBar pb = (ProgressBar)findViewById(R.id.main_pb);
        final TextView cs = (TextView)findViewById(R.id.main_concheck);
        final Button craft = (Button)findViewById(R.id.main_bt_craft);
        final Button drop = (Button)findViewById(R.id.main_bt_drop);

        boolean result = false;

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
            cs.setVisibility(View.VISIBLE);
            cs.setText(R.string.checking_connection_with_kcdb);
            craft.setEnabled(false);
            drop.setEnabled(false);
        }

        @Override
        protected Context doInBackground(Context... contexts) {
            while (!Core.inited) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {e.printStackTrace();}
            }

            Kanmusu yuu = Core.getKanmusu("Yuudachi", Core.kmlist);
            try {
                KCDB.getCC(yuu, yuu.craft);
            } catch (IOException e) {e.printStackTrace();return contexts[0];}
            result = true;
            return contexts[0];
        }

        @Override
        protected void onPostExecute(Context context) {

            SharedPreferences p = getPreferences(MODE_PRIVATE);
            p.edit().putBoolean("hasConnection", result).apply();

            showConInfo(result);
        }
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "unused"})
    private void resetSetups() {
        File f = new File(getFilesDir(), "last.ss");
        f.delete();
        f = new File(getFilesDir(), "last.sskm");
        f.delete();
    }

    private void showConInfo(boolean hasConnection) {
        final ProgressBar pb = (ProgressBar)findViewById(R.id.main_pb);
        final TextView cs = (TextView)findViewById(R.id.main_concheck);
        final Button craft = (Button)findViewById(R.id.main_bt_craft);
        final Button drop = (Button)findViewById(R.id.main_bt_drop);

        pb.setVisibility(View.GONE);

        if (hasConnection) {
            cs.setVisibility(View.GONE);
            craft.setEnabled(true);
            drop.setEnabled(true);
        } else {
            craft.setEnabled(false);
            drop.setEnabled(false);
            cs.setText(R.string.err_main_kcdb);
        }
    }
}
