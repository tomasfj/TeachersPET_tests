package pt.ubi.di.pmd.titcherspet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Sumario extends Activity {

    private Cursor alunos;
    private String turma;
    private ArrayList<String> nomes = new ArrayList<String>();
    private ArrayList<Integer> ids = new ArrayList<Integer>();
    private ArrayList<String> idades = new ArrayList<String>();
    private ArrayList<String> alergias = new ArrayList<String>();
    private ArrayList<String> telefones = new ArrayList<String>();
    private ArrayList<String> emails = new ArrayList<String>();
    private int id = 1;
    private EditText sumario1;
    private ArrayList<Integer> ids_atualizar;
    private Context contexto;
    private int sair = 0;
    private Uri provider = Uri.parse("content://com.example.dokas.titcherspetadmin.provedor/alunos");
    private Uri provider1 = Uri.parse("content://com.example.dokas.titcherspetadmin.provedor/sumarios");
    private Uri provider2 = Uri.parse("content://com.example.dokas.titcherspetadmin.provedor/faltas");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sumario);

        grantUriPermission("com.example.dokas.titcherspetadmin", provider, 0);
        grantUriPermission("com.example.dokas.titcherspetadmin", provider1, 0);
        grantUriPermission("com.example.dokas.titcherspetadmin", provider2, 0);

        try{

            Intent intento = getIntent();
            turma = intento.getStringExtra("turma");
        }

        catch (Exception e){}

        ScrollView scroll = (ScrollView) findViewById(R.id.scroll);
        scroll.setFadingEdgeLength(25);

        Bundle recebe = getIntent().getExtras();
        String turma = recebe.getString("turma");
        int admin = recebe.getInt("admin");
    }

    @Override
    protected void onResume(){

        super.onResume();

        alunos = getContentResolver().query(provider, new String[] {"nome", "id", "turma", "idade", "alergias", "telemovel", "email"},  "turma='"+ turma +"';", null, null);

        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_check);
        layout.removeAllViews();

        while(alunos.moveToNext()) {

            ConstraintLayout secundario = (ConstraintLayout) getLayoutInflater().inflate(R.layout.linha_check, null); // instanciar os objetos que estão em "linha_pomar.xml"

            nomes.add(alunos.getString(0));
            ids.add(alunos.getInt(1));
            idades.add(alunos.getString(3));
            alergias.add(alunos.getString(4));
            telefones.add(alunos.getString(5));
            emails.add(alunos.getString(6));

            CheckBox novo = (CheckBox) secundario.findViewById(R.id.check1);
            novo.setText(alunos.getString(0));
            novo.setId(alunos.getInt(1));

            Button novo_info = (Button) secundario.findViewById(R.id.image1);
            novo_info.setText("" + alunos.getInt(1));
            novo_info.setId(id*1000);
            id+=1;

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(5, 10, 5, 0);
            layout.addView(secundario, layoutParams);
        }
    }

    @Override
    protected void onStop(){

        super.onStop();

    }

    public void voltar(View v){

        super.finish();
    }

    public void guardar(View v){

        sumario1 = (EditText) findViewById(R.id.sumario);

        if(sumario1.getText().toString().equals(""))
            Toast.makeText(this,"Preencha o sumário corretamente!", Toast.LENGTH_LONG).show();

        else {

            Cursor sumarios = getContentResolver().query(provider1, new String[] {"turma", "data", "sumario"},  "turma='"+ turma +"';", null, null);

            if (sumarios.moveToFirst()) {

                contexto = this;

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Já guardou um sumário para hoje! Quer continuar?");
                builder.setMessage("Se continuar, o sumário anterior será reescrito");
                builder.setCancelable(true);

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() { // botão positivo

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
                        String data = sdf2.format(new Date());

                        sumario1 = (EditText) findViewById(R.id.sumario);

                        ContentValues valores = new ContentValues();
                        valores.put("turma", turma);
                        valores.put("data", data);
                        valores.put("sumario", sumario1.getText().toString());
                        getContentResolver().update(provider1, valores, "data='" + data + "' AND turma='" + turma + "'", null);

                        CheckBox check;

                        // apagar as faltas do dia e desta turma
                        getContentResolver().delete(provider2, "data='" + data + "' AND turma='" + turma + "'", null);

                        for (int i = 0; i < ids.size(); i++) {

                            check = (CheckBox) findViewById(ids.get(i));

                            if (check.isChecked()) {

                                Log.v("TitchersPET", "" + ids.get(i));

                                valores = new ContentValues();
                                valores.put("turma", turma);
                                valores.put("data", data);
                                valores.put("id", ids.get(i));
                                valores.put("falta", 1);
                                getContentResolver().insert(provider2, valores);

                                Cursor faltas = getContentResolver().query(provider2,new String[]{"*"}, "data='" + data + "' AND turma='" + turma + "'",null,null,null);
                                boolean conteudo3 = faltas.moveToFirst();

                                while(conteudo3){

                                    Log.v("TitchersPET", "" + faltas.getInt(0));
                                    conteudo3 = faltas.moveToNext();
                                }
                            }
                        }

                        Toast.makeText(contexto, "Guardado com sucesso!", Toast.LENGTH_LONG).show();
                        ((Activity) contexto).finish();
                    }
                });

                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() { // botão negativo

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

                Button negativo = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negativo.setTextColor(Color.parseColor("#3392ff"));
            } else {

                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
                String data = sdf2.format(new Date());

                sumario1 = (EditText) findViewById(R.id.sumario);

                ContentValues valores = new ContentValues();
                valores.put("turma", turma);
                valores.put("data", data);
                valores.put("sumario", sumario1.getText().toString());
                getContentResolver().insert(provider1, valores);

                CheckBox check;

                for (int i = 0; i < ids.size(); i++) {

                    check = (CheckBox) findViewById(ids.get(i));

                    if (check.isChecked()) {

                        valores = new ContentValues();
                        valores.put("turma", turma);
                        valores.put("data", data);
                        valores.put("id", ids.get(i));
                        valores.put("falta", 1);
                        getContentResolver().insert(provider2, valores);
                    }
                }

                Toast.makeText(this, "Guardado com sucesso!", Toast.LENGTH_LONG).show();
                super.finish();
            }

        }
    }

    public void infoAluno(View v){

        Log.v("TitchersPET", "" + v.getId());
        Log.v("TitchersPET", "tamanho" + nomes.size());

        int indice = ((int)v.getId()/1000);

        Intent muda = new Intent(v.getContext(), InfoAluno.class);
        muda.putExtra("turma", turma);
        muda.putExtra("nome", nomes.get(indice-1));
        muda.putExtra("idade",idades.get(indice-1));
        muda.putExtra("id", "" + ids.get(indice-1));
        muda.putExtra("alergia", alergias.get(indice-1));
        muda.putExtra("telefone", telefones.get(indice-1));
        muda.putExtra("email", emails.get(indice-1));

        nomes.clear();
        ids.clear();
        idades.clear();
        alergias.clear();
        telefones.clear();
        emails.clear();
        id = 1;

        startActivity(muda);
        onResume();
    }
}