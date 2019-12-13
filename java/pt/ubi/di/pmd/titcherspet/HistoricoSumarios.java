package pt.ubi.di.pmd.titcherspet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoricoSumarios extends Activity {

    private String turma;
    private Cursor relatorios, sumarios;
    private int id=0, flag;
    private ArrayList<String> datas1 = new ArrayList<String>();
    private ArrayList<String> datas2 = new ArrayList<String>();
    private ArrayList<String> sumario = new ArrayList<String>();
    private ArrayList<Integer> comeu = new ArrayList<Integer>();
    private ArrayList<Integer> dormiu = new ArrayList<Integer>();
    private ArrayList<Integer> xixi = new ArrayList<Integer>();
    private ArrayList<Integer> coco = new ArrayList<Integer>();
    private ArrayList<Integer> magoou = new ArrayList<Integer>();
    private ArrayList<Integer> chorou = new ArrayList<Integer>();
    private ArrayList<String> notas = new ArrayList<String>();
    private ArrayList<Integer> ids = new ArrayList<Integer>();
    private Context contexto;
    private Cursor alunos;
    private Uri provider = Uri.parse("content://com.example.dokas.titcherspetadmin.provedor/alunos");
    private Uri provider1 = Uri.parse("content://com.example.dokas.titcherspetadmin.provedor/relatorios");
    private Uri provider2 = Uri.parse("content://com.example.dokas.titcherspetadmin.provedor/sumarios");
    private Uri provider3 = Uri.parse("content://com.example.dokas.titcherspetadmin.provedor/faltas");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_sumarios);
        grantUriPermission("com.example.dokas.titcherspetadmin", provider, 0);
        grantUriPermission("com.example.dokas.titcherspetadmin", provider1, 0);
        grantUriPermission("com.example.dokas.titcherspetadmin", provider2, 0);
        grantUriPermission("com.example.dokas.titcherspetadmin", provider3, 0);

        ScrollView scroll = (ScrollView) findViewById(R.id.lista1);
        contexto = this;
        scroll.setFadingEdgeLength(25);

        scroll = (ScrollView) findViewById(R.id.lista2);
        scroll.setFadingEdgeLength(25);

        Bundle recebe = getIntent().getExtras();
        turma = recebe.getString("turma");

        sumarios = getContentResolver().query(provider2, new String[] {"turma", "data", "sumario"},  "turma='"+ turma +"';", null, null);

        boolean conteudo1 = sumarios.moveToFirst();

        relatorios = getContentResolver().query(provider1, new String[] {"idAluno", "data", "comeu", "dormiu", "xixi", "coco", "magoou", "chorou", "turma", "notas"},  "turma='"+turma+"'", null, null);

        boolean conteudo2 = relatorios.moveToFirst();

        LinearLayout layout = (LinearLayout) findViewById(R.id.linear1);
        layout.removeAllViews();

        LinearLayout layout2 = (LinearLayout) findViewById(R.id.linear2);
        layout.removeAllViews();

        if(conteudo1==false){

            LinearLayout principal = (LinearLayout) findViewById(R.id.linear1);

            LinearLayout vazio = (LinearLayout) getLayoutInflater().inflate(R.layout.texto_vazio_linear,null);
            TextView texto = (TextView) vazio.findViewById(R.id.vazio_texto);
            texto.setText("Nenhum sumário guardado");
            vazio.removeView(texto);

            principal.addView(texto);
        }

        else{

            LinearLayout principal = (LinearLayout) findViewById(R.id.linear1);

            TextView texto = (TextView) findViewById(R.id.vazio_texto);

            if(texto!=null)
                principal.removeView(texto);
        }


        if(conteudo2==false){

            LinearLayout principal = (LinearLayout) findViewById(R.id.linear2);

            LinearLayout vazio = (LinearLayout) getLayoutInflater().inflate(R.layout.texto_vazio_linear,null);
            TextView texto = (TextView) vazio.findViewById(R.id.vazio_texto);
            texto.setText("Nenhum relatório guardado");
            vazio.removeView(texto);

            principal.addView(texto);
        }

        else{

            LinearLayout principal = (LinearLayout) findViewById(R.id.linear2);

            TextView texto = (TextView) findViewById(R.id.vazio_texto);

            if(texto!=null)
                principal.removeView(texto);
        }

        while(conteudo1) {

            ConstraintLayout secundario1 = (ConstraintLayout) getLayoutInflater().inflate(R.layout.linha_sumario, null);

            Button novo = (Button) secundario1.findViewById(R.id.sumarios);
            datas1.add(sumarios.getString(1));
            sumario.add(sumarios.getString(2));
            novo.setId(id);
            id += 1;

            novo.setText(sumarios.getString(1));

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(5, 10, 5, 0);
            layout.addView(secundario1, layoutParams);

            novo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    String presencas = "";

                    Cursor faltas = getContentResolver().query(provider3, new String[] {"turma", "data","id", "falta"},  "data='"+ datas1.get((v.getId())) +"' and turma='"+turma+"';", null, null);
                    boolean conteudo3 = faltas.moveToFirst();
                    ArrayList<Integer> faltas_ids = new ArrayList<Integer>();

                    Log.v("TitchersPET","" + datas1.get((v.getId())) + turma);

                    while(conteudo3){

                        faltas_ids.add(faltas.getInt(2));
                        Log.v("TitchersPET","" + faltas.getInt(2));
                        conteudo3 = faltas.moveToNext();
                    }

                    Cursor alunos_turma = getContentResolver().query(provider, new String[] {"nome","id","turma","idade","alergias","telemovel", "email"},  "turma='"+turma+"';", null, null);
                    conteudo3 = alunos_turma.moveToFirst();

                    while(conteudo3){

                        if(!faltas_ids.contains(alunos_turma.getInt(1))){

                            presencas += alunos_turma.getString(0) + ",";
                        }

                        conteudo3 = alunos_turma.moveToNext();
                    }

                    if(presencas.split(",").length==1)
                        presencas = presencas.split(",")[0];

                    else{

                        String[] presencas_aux = presencas.split(",");
                        presencas = "";

                        for(int i=0; i<presencas_aux.length; i++){

                            if(i==((presencas_aux.length)-2)){

                                presencas += presencas_aux[i] + " e " + presencas_aux[i+1];
                                break;
                            }

                            else
                                presencas += presencas_aux[i] + ", ";
                        }
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(contexto);

                    builder.setTitle("Sumário do dia " + datas1.get((v.getId())));
                    builder.setMessage("\"" + sumario.get((v.getId())) + "\"\n\nPresenças: " + presencas);
                    builder.setCancelable(true);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { // botão positivo

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    // mudar a cor dos botões
                    Button positivo = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positivo.setTextColor(Color.parseColor("#3392ff"));
                }
            });

            conteudo1 = sumarios.moveToNext();

        }

        id = 0;

        while(conteudo2) {

            ConstraintLayout secundario = (ConstraintLayout) getLayoutInflater().inflate(R.layout.linha_relatorio, null);

            Button novo1 = (Button) secundario.findViewById(R.id.relatorios);
            datas2.add(relatorios.getString(1));
            comeu.add(relatorios.getInt(2));
            dormiu.add(relatorios.getInt(3));
            xixi.add(relatorios.getInt(4));
            coco.add(relatorios.getInt(5));
            magoou.add(relatorios.getInt(6));
            chorou.add(relatorios.getInt(7));
            notas.add(relatorios.getString(9));
            novo1.setId(id);
            id += 1;

            alunos = getContentResolver().query(provider, new String[] {"nome","id","turma","idade","alergias","telemovel", "email"},  "id="+relatorios.getInt(0), null, null);
            boolean conteudo = alunos.moveToFirst();

            novo1.setText(alunos.getString(0) + " (ID " + relatorios.getInt(0) + ")\n" + relatorios.getString(1));
            ids.add(relatorios.getInt(0));

            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams2.setMargins(5, 10, 5, 0);
            layout2.addView(secundario, layoutParams2);

            novo1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    String aux = "";

                    if (comeu.get((v.getId())) == 1)
                        aux += "Comeu bem\n";
                    if (dormiu.get((v.getId())) == 1)
                        aux += "Dormiu bem\n";
                    if (xixi.get((v.getId())) == 1)
                        aux += "Fez xixi\n";
                    if (coco.get((v.getId())) == 1)
                        aux += "Fez cocó\n";
                    if (magoou.get((v.getId())) == 1)
                        aux += "Magoou-se\n";
                    if (chorou.get((v.getId())) == 1)
                        aux += "Chorou\n";

                    if(!notas.get((v.getId())).equals(""))
                        aux += "\n\"" + notas.get((v.getId())) + "\"";

                    AlertDialog.Builder builder = new AlertDialog.Builder(contexto);

                    builder.setTitle("Relatório do " + alunos.getString(0) + " (" + datas2.get((v.getId())) + ")");
                    builder.setMessage(aux);
                    builder.setCancelable(true);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { // botão positivo

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    // mudar a cor dos botões
                    Button positivo = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positivo.setTextColor(Color.parseColor("#3392ff"));
                }
            });

            conteudo2 = relatorios.moveToNext();

        }

    }

    public void voltar(View v){

        this.finish();

    }
}