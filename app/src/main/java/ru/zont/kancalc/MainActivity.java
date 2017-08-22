package ru.zont.kancalc;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!hasConnection(this)) {
            findViewById(R.id.main_bt_drop).setEnabled(false);
            findViewById(R.id.main_bt_craft).setEnabled(false);
        }

        try {
            Core.init(this);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            Toast.makeText(this, "ERROR: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }

        final TextView ver = (TextView)findViewById(R.id.main_ver);
        ver.setText("KanCalc "+Core.version+" by ZONT_ ©2017");
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
}
