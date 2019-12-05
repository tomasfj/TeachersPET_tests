package pt.ubi.di.pmd.titcherspet;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AreaPrincipal extends Activity {

    private TextView nome;
    private TextView tempo;
    private TextView data;
    private String turma;
    private ImageView imagem_principal;
    private ImageView linear2_imagem;
    private ImageView linear3_imagem;
    private ImageView linear4_imagem;
    private TextView texto_principal;
    private TextView texto_principal2;
    private TextView linear2_texto;
    private TextView linear2_texto2;
    private TextView linear3_texto;
    private TextView linear3_texto2;
    private TextView linear4_texto;
    private TextView linear4_texto2;
    private TextView informacao;
    private ConstraintLayout layout_principal;
    private LinearLayout linha_tempo;
    private TextView falha;
    private GetData dados;
    private Handler handler;
    private int externo;
    private SharedPreferences pref;
    private String atualizado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_principal);

        nome = (TextView) findViewById(R.id.login);
        //tempo = (TextView) findViewById(R.id.tempo);

        layout_principal = (ConstraintLayout) findViewById(R.id.layout_principal);
        linha_tempo = (LinearLayout) findViewById(R.id.linha_tempo);

        imagem_principal = (ImageView) findViewById(R.id.imagem_principal);
        linear2_imagem = (ImageView) findViewById(R.id.linear2_imagem);
        linear3_imagem = (ImageView) findViewById(R.id.linear3_imagem);
        linear4_imagem = (ImageView) findViewById(R.id.linear4_imagem);

        texto_principal = (TextView) findViewById(R.id.texto_principal);
        texto_principal2 = (TextView) findViewById(R.id.texto_principal_2);

        linear2_texto = (TextView) findViewById(R.id.linear2_texto);
        linear2_texto2 = (TextView) findViewById(R.id.linear2_texto2);

        linear3_texto = (TextView) findViewById(R.id.linear3_texto);
        linear3_texto2 = (TextView) findViewById(R.id.linear3_texto2);

        linear4_texto = (TextView) findViewById(R.id.linear4_texto);
        linear4_texto2 = (TextView) findViewById(R.id.linear4_texto2);

        informacao = (TextView) findViewById(R.id.informacao);

        falha = (TextView) findViewById(R.id.falha);

        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        String data_aux = sdf2.format(new Date());
        data = (TextView) findViewById(R.id.data);
        data.setText(data_aux);

        pref = getApplicationContext().getSharedPreferences("Preferencias", 0);
        String aux = pref.getString("guardar_copia", "");

        atualizado = pref.getString("atualizacao", "");

        if(aux.equals("")){

            SharedPreferences.Editor editor = pref.edit();
            editor.putString("guardar_copia", "Sim");
            editor.apply();
        }

        if(atualizado.equals("")){

            SharedPreferences.Editor editor = pref.edit();
            editor.putString("atualizacao", "inicio");
            editor.apply();

            // dados de meteorologia
            editor.putString("dados1", "");
            editor.apply();

            editor.putString("dados2", "");
            editor.apply();

            editor.putString("dados3", "");
            editor.apply();

            editor.putString("dados4", "");
            editor.apply();

            // icones
            editor.putString("dados1_icone", "");
            editor.apply();

            editor.putString("dados2_icone", "");
            editor.apply();

            editor.putString("dados3_icone", "");
            editor.apply();

            editor.putString("dados4_icone", "");
            editor.apply();
        }

        try{
            Intent recebido = getIntent();
            nome.setText(recebido.getStringExtra("nome"));
            turma = recebido.getStringExtra("turma");
        }

        catch(Exception e){}

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // no Lollipop as permissões são pedidas ao início, nunca em runtime

            // se, por qualquer motivo, não tivermos permissão de escrita no armazenamento externo, vamos pedi-la ao utilizador
            if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.INTERNET}, 1);
        }

        externo=4;

        handler = new Handler();

        Thread novo = new Thread(){ // thread que tratará de saber se a informação de tempo foi obtida

            public void run() {

                Runnable runnable = new Runnable(){

                    @Override
                    public void run(){

                        Date currentTime = Calendar.getInstance().getTime();
                        String hora = currentTime.toString().split(" ")[2] + "_" + currentTime.toString().split(" ")[3];

                        String hora_guardada = pref.getString("atualizacao", "");

                        if(!hora_guardada.equals("inicio")){

                            String[] hora_guardada_array = hora_guardada.split("_");
                            String[] hora_aux = hora.split("_");

                            informacao.setText("Última atualização às " + pref.getString("atualizacao_horas", hora_guardada_array[1]));

                            // os dias são iguais
                            if(Integer.parseInt(hora_guardada_array[0])==Integer.parseInt(hora_aux[0])) {

                                // as horas são iguais
                                if(Integer.parseInt(hora_aux[1].split(":")[0])==Integer.parseInt(hora_guardada_array[1].split(":")[0])){

                                    // os minutos estão no intervalo guardado e atualizado
                                    if(Integer.parseInt(hora_aux[1].split(":")[1])>=Integer.parseInt(hora_guardada_array[1].split(":")[1]) &&
                                            Integer.parseInt(hora_aux[1].split(":")[1])<=Integer.parseInt(hora_guardada_array[2].split(":")[1])){

                                        Log.v("TitchersPET", "Obtendo dados já guardados");

                                        String[] dados_aux = pref.getString("dados1","").split("_");
                                        texto_principal.setText(dados_aux[0]);
                                        texto_principal2.setText(dados_aux[1]);

                                        String icone_aux = pref.getString("dados1_icone","");
                                        Drawable icone_aux_draw;

                                        if(icone_aux.equals("sol")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.sol);
                                            imagem_principal.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("nublado")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.nublado);
                                            imagem_principal.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("nublado_forte")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.nublado_forte);
                                            imagem_principal.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("nublado_chuva")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.nublado_chuva);
                                            imagem_principal.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("neve")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.neve);
                                            imagem_principal.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("noite")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.noite);
                                            imagem_principal.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("noite_nublado")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.noite_nublado);
                                            imagem_principal.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("noite_chuva")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.noite_chuva);
                                            imagem_principal.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("noite_neve")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.noite_neve);
                                            imagem_principal.setImageDrawable(icone_aux_draw);
                                        }

                                        // segundo icone
                                        dados_aux = pref.getString("dados2","").split("_");
                                        linear2_texto.setText(dados_aux[0]);
                                        linear2_texto2.setText(dados_aux[1]);

                                        icone_aux = pref.getString("dados2_icone","");

                                        if(icone_aux.equals("sol")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.sol);
                                            linear2_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("nublado")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.nublado);
                                            linear2_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("nublado_forte")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.nublado_forte);
                                            linear2_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("nublado_chuva")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.nublado_chuva);
                                            linear2_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("neve")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.neve);
                                            linear2_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("noite")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.noite);
                                            linear2_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("noite_nublado")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.noite_nublado);
                                            linear2_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("noite_chuva")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.noite_chuva);
                                            linear2_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("noite_neve")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.noite_neve);
                                            linear2_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        // terceiro icone
                                        dados_aux = pref.getString("dados3","").split("_");
                                        linear3_texto.setText(dados_aux[0]);
                                        linear3_texto2.setText(dados_aux[1]);

                                        icone_aux = pref.getString("dados3_icone","");

                                        if(icone_aux.equals("sol")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.sol);
                                            linear3_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("nublado")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.nublado);
                                            linear3_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("nublado_forte")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.nublado_forte);
                                            linear3_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("nublado_chuva")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.nublado_chuva);
                                            linear3_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("neve")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.neve);
                                            linear3_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("noite")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.noite);
                                            linear3_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("noite_nublado")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.noite_nublado);
                                            linear3_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("noite_chuva")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.noite_chuva);
                                            linear3_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("noite_neve")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.noite_neve);
                                            linear3_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        // quarto e último icone
                                        dados_aux = pref.getString("dados4","").split("_");
                                        linear4_texto.setText(dados_aux[0]);
                                        linear4_texto2.setText(dados_aux[1]);

                                        icone_aux = pref.getString("dados4_icone","");

                                        if(icone_aux.equals("sol")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.sol);
                                            linear4_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("nublado")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.nublado);
                                            linear4_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("nublado_forte")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.nublado_forte);
                                            linear4_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("nublado_chuva")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.nublado_chuva);
                                            linear4_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("neve")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.neve);
                                            linear4_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("noite")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.noite);
                                            linear4_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("noite_nublado")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.noite_nublado);
                                            linear4_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("noite_chuva")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.noite_chuva);
                                            linear4_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        else if(icone_aux.equals("noite_neve")) {

                                            icone_aux_draw = getResources().getDrawable(R.drawable.noite_neve);
                                            linear4_imagem.setImageDrawable(icone_aux_draw);
                                        }

                                        return;
                                    }
                                }

                            }
                        }

                        // buscar os dados
                        dados = new GetData();
                        dados.execute("Covilhã");

                        while(true){

                            Log.v("TitchersPET", "Obtendo dados novos");

                            if(dados.result.equals("Erro!")){

                                falha.setText("(Erro: impossível obter dados de tempo)");
                                layout_principal.removeView(linha_tempo);
                                return;
                            }

                            else if(!dados.result.equals("")){

                                Log.v("TitchersPET",dados.result);

                                String aux[] = dados.result.split("\"DateTime\":\"");
                                String aux3[] = dados.result.split("\"Value\":");
                                String aux5[] = dados.result.split("\"IconPhrase\":\"");
                                String aux2 = "";
                                String final_aux = "";
                                String aux4 = "";
                                int j = 0;
                                int k = 1;
                                int k2 = 1;
                                int interno = 0;
                                Drawable icone;

                                for(int i=1; i<aux.length; i=i+2){

                                    for(j=11; j<13; j++){

                                        aux2 += aux[i].charAt(j);
                                    }

                                    String descricao = "";
                                    ImageView editar = imagem_principal;

                                    for(int k3=0; k3<aux5[k2].length(); k3++){

                                        descricao += aux5[k2].charAt(k3);

                                        if(aux5[k2].charAt(k3+1)=='\"'){

                                            if(interno==1)
                                                editar=linear2_imagem;

                                            else if(interno==2)
                                                editar=linear3_imagem;

                                            else if(interno==3)
                                                editar=linear4_imagem;

                                            if(aux5[k2].charAt(k3+16)=='t') { // dia

                                                // alterar o ícone
                                                if (descricao.equalsIgnoreCase("Sunny") || descricao.equalsIgnoreCase("Mostly Sunny") || descricao.equalsIgnoreCase("Partly Sunny") || descricao.equalsIgnoreCase("Hazy Sunshine") || descricao.equalsIgnoreCase("Hot")) {

                                                    icone = getResources().getDrawable(R.drawable.sol);
                                                    editar.setImageDrawable(icone);

                                                    // guardar nas shared preferences
                                                    SharedPreferences.Editor editor = pref.edit();
                                                    editor.putString("dados" + (interno+1) + "_icone", "sol");
                                                    editor.apply();
                                                }

                                                else if (descricao.equalsIgnoreCase("Mostly Cloudy") || descricao.equalsIgnoreCase("Cloudy") || descricao.equalsIgnoreCase("Windy") || descricao.equalsIgnoreCase("Intermittent Clouds")) {

                                                    icone = getResources().getDrawable(R.drawable.nublado);
                                                    editar.setImageDrawable(icone);

                                                    // guardar nas shared preferences
                                                    SharedPreferences.Editor editor = pref.edit();
                                                    editor.putString("dados" + (interno+1) + "_icone", "nublado");
                                                    editor.apply();
                                                }

                                                else if (descricao.equalsIgnoreCase("Dreary (Overcast)") || descricao.equalsIgnoreCase("Fog")) {

                                                    icone = getResources().getDrawable(R.drawable.nublado_forte);
                                                    editar.setImageDrawable(icone);

                                                    // guardar nas shared preferences
                                                    SharedPreferences.Editor editor = pref.edit();
                                                    editor.putString("dados" + (interno+1) + "_icone", "nublado_forte");
                                                    editor.apply();
                                                }

                                                else if (descricao.equalsIgnoreCase("Rain") || descricao.equalsIgnoreCase("Showers") || descricao.equalsIgnoreCase("T-Storms") || descricao.equalsIgnoreCase("Mostly Cloudy w/ Showers") || descricao.equalsIgnoreCase("Partly Sunny w/ Showers") || descricao.equalsIgnoreCase("Mostly Cloudy w/ T-Storms") || descricao.equalsIgnoreCase("Partly Cloudy w/ T-Storms") || descricao.equalsIgnoreCase("Cold")) {

                                                    icone = getResources().getDrawable(R.drawable.nublado_chuva);
                                                    editar.setImageDrawable(icone);

                                                    // guardar nas shared preferences
                                                    SharedPreferences.Editor editor = pref.edit();
                                                    editor.putString("dados" + (interno+1) + "_icone", "nublado_chuva");
                                                    editor.apply();
                                                }

                                                else if (descricao.equalsIgnoreCase("Flurries") || descricao.equalsIgnoreCase("Mostly Cloudy w/ Flurries") || descricao.equalsIgnoreCase("Partly Sunny w/ Flurries") || descricao.equalsIgnoreCase("Snow") || descricao.equalsIgnoreCase("Mostly Cloudy w/ Snow") || descricao.equalsIgnoreCase("Ice") || descricao.equalsIgnoreCase("Sleet") || descricao.equalsIgnoreCase("Freezing Rain") || descricao.equalsIgnoreCase("Rain and Snow")) {

                                                    icone = getResources().getDrawable(R.drawable.neve);
                                                    editar.setImageDrawable(icone);

                                                    // guardar nas shared preferences
                                                    SharedPreferences.Editor editor = pref.edit();
                                                    editor.putString("dados" + (interno+1) + "_icone", "neve");
                                                    editor.apply();
                                                }

                                            }

                                            else{ // noite

                                                // alterar o ícone
                                                if (descricao.equalsIgnoreCase("Clear") || descricao.equalsIgnoreCase("Mostly Clear")) {

                                                    icone = getResources().getDrawable(R.drawable.noite);
                                                    editar.setImageDrawable(icone);

                                                    // guardar nas shared preferences
                                                    SharedPreferences.Editor editor = pref.edit();
                                                    editor.putString("dados" + (interno+1) + "_icone", "noite");
                                                    editor.apply();
                                                }

                                                else if (descricao.equalsIgnoreCase("Partly Cloudy") || descricao.equalsIgnoreCase("Cloudy") || descricao.equalsIgnoreCase("Intermittent Clouds") || descricao.equalsIgnoreCase("Hazy Moonlight") || descricao.equalsIgnoreCase("Mostly Cloudy")) {

                                                    icone = getResources().getDrawable(R.drawable.noite_nublado);
                                                    editar.setImageDrawable(icone);

                                                    // guardar nas shared preferences
                                                    SharedPreferences.Editor editor = pref.edit();
                                                    editor.putString("dados" + (interno+1) + "_icone", "noite_nublado");
                                                    editor.apply();
                                                }

                                                else if (descricao.equalsIgnoreCase("Partly Cloudy w/ Showers") || descricao.equalsIgnoreCase("Mostly Cloudy w/ Showers") || descricao.equalsIgnoreCase("Partly Cloudy w/ T-Storms") || descricao.equalsIgnoreCase("Mostly Cloudy w/ T-Storms")) {

                                                    icone = getResources().getDrawable(R.drawable.noite_chuva);
                                                    editar.setImageDrawable(icone);

                                                    // guardar nas shared preferences
                                                    SharedPreferences.Editor editor = pref.edit();
                                                    editor.putString("dados" + (interno+1) + "_icone", "noite_chuva");
                                                    editor.apply();
                                                }

                                                else if (descricao.equalsIgnoreCase("Mostly Cloudy w/ Flurries") || descricao.equalsIgnoreCase("Mostly Cloudy w/ Snow")) {

                                                    icone = getResources().getDrawable(R.drawable.noite_neve);
                                                    editar.setImageDrawable(icone);

                                                    // guardar nas shared preferences
                                                    SharedPreferences.Editor editor = pref.edit();
                                                    editor.putString("dados" + (interno+1) + "_icone", "noite_neve");
                                                    editor.apply();
                                                }
                                            }

                                            descricao = "";
                                            break;
                                        }
                                    }

                                    aux4 = "" +  aux3[k].charAt(0) + aux3[k].charAt(1) + aux3[k].charAt(2) + aux3[k].charAt(3);
                                    Double valor = (Double.parseDouble(aux4)-32.0)*(5.0/9.0);
                                    NumberFormat formatter = new DecimalFormat("#0");

                                    if(interno==0){

                                        texto_principal.setText(aux2+"h");
                                        texto_principal2.setText(formatter.format(valor)+"ºC");
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("dados1", texto_principal.getText().toString() + "_" + texto_principal2.getText().toString());
                                        editor.apply();
                                    }

                                    else if(interno==1){

                                        linear2_texto.setText(aux2+"h");
                                        linear2_texto2.setText(formatter.format(valor)+"ºC");
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("dados2", linear2_texto.getText().toString() + "_" + linear2_texto2.getText().toString());
                                        editor.apply();
                                    }

                                    else if(interno==2){

                                        linear3_texto.setText(aux2+"h");
                                        linear3_texto2.setText(formatter.format(valor)+"ºC");
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("dados3", linear3_texto.getText().toString() + "_" + linear3_texto2.getText().toString());
                                        editor.apply();
                                    }

                                    else if(interno==3){

                                        linear4_texto.setText(aux2+"h");
                                        linear4_texto2.setText(formatter.format(valor)+"ºC");
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("dados4", linear4_texto.getText().toString() + "_" + linear4_texto2.getText().toString());
                                        editor.apply();
                                    }

                                    aux2 = "";
                                    aux4 = "";
                                    interno++;
                                    k = k + 2;
                                    k2 = k2 + 2;

                                    if(interno==externo) { // estão feitas as 4 operações de obtenção do estado do tempo

                                        currentTime = Calendar.getInstance().getTime();
                                        hora = currentTime.toString().split(" ")[3];

                                        String conteudo = currentTime.toString().split(" ")[2] + "_" + hora.split(":")[0]; // dia_hora:

                                        if(Integer.parseInt(hora.split(":")[1])>=0 && Integer.parseInt(hora.split(":")[1])<=29)
                                            conteudo = conteudo + ":00_" + hora.split(":")[0] + ":29";

                                        else
                                            conteudo = conteudo + ":30_" + hora.split(":")[0] + ":59";

                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("atualizacao", conteudo);
                                        editor.apply();
                                        Log.v("TitchersPET",conteudo);

                                        informacao.setText("Última atualização às " + hora.split(":")[0] + ":" + hora.split(":")[1] + "h");
                                        editor.putString("atualizacao_horas", hora.split(":")[0] + ":" + hora.split(":")[1] + "h");
                                        editor.apply();
                                        return;
                                    }
                                }
                                break;
                            }
                        }
                    }
                };

                handler.post(runnable);
            }
        };

        novo.start();
    }

    public void infoTurma(View v){

        Intent intento = new Intent(this,InfoTurma.class);
        intento.putExtra("turma",turma);
        startActivity(intento);
    }

    public void sumario(View v){

        Intent intento = new Intent(this,Sumario.class);
        intento.putExtra("turma",turma);
        startActivity(intento);
    }

    public void historico(View v){

        Intent intento = new Intent(this,HistoricoSumarios.class);
        intento.putExtra("turma",turma);
        startActivity(intento);
    }

    public void pedirRelatorio(View v){

        Intent intento = new Intent(this,AlunoFormulario.class);
        intento.putExtra("turma",turma);
        intento.putExtra("nome_educadora",nome.getText().toString());
        startActivity(intento);
    }

    public void logout(View v){

        super.finish();
    }

    public void infoApp(View v){

        Toast.makeText(this, "Obrigado por usar a nossa aplicação!", Toast.LENGTH_SHORT).show();
    }

    public void definicoes(View v){

        Intent info = new Intent(this,Definicoes.class);
        startActivity(info);
    }

    @Override
    public void onRequestPermissionsResult(int reqCode, String[] permissions, int[] grantResults){

        if(reqCode==1){

            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                Log.i("TitchersPET","Permissões obtidas!");

            else
                Toast.makeText(this,"Para aceder a esta feature, considere dar a permissão pedida à app!", Toast.LENGTH_LONG).show();
        }
    }

}
