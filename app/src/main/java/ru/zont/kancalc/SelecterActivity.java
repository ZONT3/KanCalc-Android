package ru.zont.kancalc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class SelecterActivity extends AppCompatActivity {

    final String DD = getString(R.string.type_dd);
    final String CL = getString(R.string.type_cl);
    final String CA = getString(R.string.type_ca);
    final String BB = getString(R.string.type_bb);
    final String CLT = getString(R.string.type_clt);
    final String CAV = getString(R.string.type_cav);
    final String BBV = getString(R.string.type_bbv);
    final String FBB = getString(R.string.type_fbb);
    final String CV = getString(R.string.type_cv);
    final String CVL = getString(R.string.type_cvl);
    final String CVB = getString(R.string.type_cvb);
    final String SS = getString(R.string.type_ss);
    final String AUX = getString(R.string.type_aux);
    final String DE = getString(R.string.type_de);

    ArrayList<Button> buttons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecter);
    }

    private void showTypes() {
        clearActivity();

        LinearLayout lay = (LinearLayout)findViewById(R.id.as_lay);
        Button sample = (Button)findViewById(R.id.as_sample);
        ArrayList<String> types = Core.getTypes(Core.kmlistAM);

        for (final String type : types) {
            final Button button = new Button(this);
            button.setTypeface(sample.getTypeface());
            button.setLayoutParams(sample.getLayoutParams());
            button.setBackground(sample.getBackground());

            switch (type) {
                case "DD":
                    button.setBackgroundResource(R.drawable.pw_144);
                    button.setText(DD);
                    break;
                case "CL":
                    button.setBackgroundResource(R.drawable.pw_158);
                    button.setText(CL);
                    break;
                case "CA":
                    button.setBackgroundResource(R.drawable.pw_416);
                    button.setText(CA);
                    break;
                case "BB":
                    button.setBackgroundResource(R.drawable.pw_341);
                    button.setText(BB);
                    break;
                case "CLT":
                    button.setBackgroundResource(R.drawable.pw_119);
                    button.setText(CLT);
                    break;
                case "CAV":
                    button.setBackgroundResource(R.drawable.pw_129);
                    button.setText(CAV);
                    break;
                case "BBV":
                    button.setBackgroundResource(R.drawable.pw_412);
                    button.setText(BBV);
                    break;
                case "FBB":
                    button.setBackgroundResource(R.drawable.pw_178);
                    button.setText(FBB);
                    break;
                case "CV":
                    button.setBackgroundResource(R.drawable.pw_111);
                    button.setText(CV);
                    break;
                case "CVL":
                    button.setBackgroundResource(R.drawable.pw_281);
                    button.setText(CVL);
                    break;
                case "CVB":
                    button.setBackgroundResource(R.drawable.pw_153);
                    button.setText(CVB);
                    break;
                case "SS":
                    button.setBackgroundResource(R.drawable.pw_191);
                    button.setText(SS);
                    break;
                case "DE":
                    button.setBackgroundResource(R.drawable.pw_518);
                    button.setText(DE);
                    break;
                case "AR":
                    button.setBackgroundResource(R.drawable.pw_187);
                    button.setText(AUX);
                    break;
                default: break;
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {showClasses(((Button)view).getText().toString());}
            });

            buttons.add(button);
        }
    }

    private void showClasses(String type) {
        clearActivity();

        switch (type) {

        }

        LinearLayout lay = (LinearLayout)findViewById(R.id.as_lay);
        Button sample = (Button)findViewById(R.id.as_sample);
        ArrayList<String> classes = Core.getClasses(type, Core.kmlistAM);

        for (final String cls : classes) {
            Kanmusu nameship = Core.getKanmusu(cls, Core.kmlist);
            if (nameship==null)
                for (Kanmusu km : Core.kmlistAM)
                    if (km.cls.equals(cls))
                        nameship = km;

            final Button button = new Button(this);
            button.setTypeface(sample.getTypeface());
            button.setLayoutParams(sample.getLayoutParams());
            button.setBackground(sample.getBackground());

            int pw = getResources().getIdentifier("pw_"+nameship.id, "drawable", getPackageName());
            if (pw!=0)
                button.setBackgroundResource(pw);
            button.setText(cls);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {showSips(((Button)view).getText().toString());}
            });

            buttons.add(button);
        }
    }

    private void showSips(String cls) {

    }

    private void showModels(Kanmusu kanmusu) {

    }

    private void clearActivity() {
        for (Button b : buttons) {
            ((LinearLayout) findViewById(R.id.as_lay)).removeView(b);
            b.destroyDrawingCache();
        }
        buttons = new ArrayList<>();
    }
}
