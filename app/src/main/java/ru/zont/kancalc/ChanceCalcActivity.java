package ru.zont.kancalc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChanceCalcActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chance_calc);
    }

    public void onEnter(View v) {
        final EditText chance = (EditText)findViewById(R.id.cc_chance);
        final EditText tries = (EditText)findViewById(R.id.cc_tries);
        final TextView result = (TextView)findViewById(R.id.cc_result);

        result.setText(Core.getSumChance(Double.parseDouble(chance.getText().toString()),
                Integer.parseInt(tries.getText().toString()))+"%");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        try {
            Intent i = new Intent(ChanceCalcActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        } catch (Exception e) {e.printStackTrace();}
    }
}
