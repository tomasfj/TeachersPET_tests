package pt.ubi.di.pmd.titcherspet;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.Switch;

public class Definicoes extends Activity {

    private Button local_botao;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definicoes);

        local_botao = (Button) findViewById(R.id.definicao1_escolher);

        pref = getApplicationContext().getSharedPreferences("Preferencias", 0);
        String aux = pref.getString("guardar_copia", "");

        Switch botao_switch = (Switch) findViewById(R.id.definicao1_escolher);

        if(aux.equals("Sim"))
            botao_switch.setChecked(true);

        else
            botao_switch.setChecked(false);

        botao_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("guardar_copia", "Sim");
                    editor.apply();
                }

                else{

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("guardar_copia", "Não");
                    editor.apply();
                }
            }
        });
    }

    public void voltar(View v){

        super.finish();
    }

    public void accuWeather(View v){

        String url = "http://www.accuweather.com";

        Intent intento = new Intent(Intent.ACTION_VIEW);
        intento.setData(Uri.parse(url));

        startActivity(intento);
    }
}
