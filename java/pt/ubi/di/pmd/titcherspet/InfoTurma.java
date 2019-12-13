package pt.ubi.di.pmd.titcherspet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

public class InfoTurma extends Activity {

    private Context contexto;
    private Cursor alunos;
    private String turma;
    private int admin, id=0;
    private ArrayList<String> nomes = new ArrayList<String>();
    private ArrayList<Integer> ids = new ArrayList<Integer>();
    private ArrayList<String> idades = new ArrayList<String>();
    private ArrayList<String> alergias = new ArrayList<String>();
    private ArrayList<String> telefones = new ArrayList<String>();
    private ArrayList<String> emails = new ArrayList<String>();
    private ArrayList<Bitmap> imagens = new ArrayList<Bitmap>();
    private Bitmap img;
    private byte[] imgByte;
    private Drawable img2;
    private Uri provider = Uri.parse("content://com.example.dokas.titcherspetadmin.provedor/alunos");
    private Uri provider1 = Uri.parse("content://com.example.dokas.titcherspetadmin.provedor/relatorios");
    private Uri provider2 = Uri.parse("content://com.example.dokas.titcherspetadmin.provedor/faltas");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_turma);
        grantUriPermission("com.example.dokas.titcherspetadmin", provider, 0);
        grantUriPermission("com.example.dokas.titcherspetadmin", provider1, 0);
        grantUriPermission("com.example.dokas.titcherspetadmin", provider2, 0);

        ScrollView scroll = (ScrollView) findViewById(R.id.lista);
        scroll.setFadingEdgeLength(25);
    }

    @Override
    public void onResume(){

        super.onResume();

        contexto = this;

        Bundle recebe = getIntent().getExtras();
        turma = recebe.getString("turma");
        admin = recebe.getInt("admin");

        alunos = getContentResolver().query(provider, new String[] {"nome", "id", "turma", "idade", "alergias", "telemovel", "email"},  "turma='"+ turma +"';", null, null);

        LinearLayout layout = (LinearLayout) findViewById(R.id.linear);
        layout.removeAllViews();

        boolean conteudo = alunos.moveToFirst();

        if(conteudo==false){

            ConstraintLayout principal = (ConstraintLayout) findViewById(R.id.principal_turma);

            ConstraintLayout vazio = (ConstraintLayout) getLayoutInflater().inflate(R.layout.texto_vazio,null);
            TextView texto = (TextView) vazio.findViewById(R.id.vazio_texto);
            vazio.removeView(texto);

            principal.addView(texto);
        }

        else{

            ConstraintLayout principal = (ConstraintLayout) findViewById(R.id.principal_turma);

            TextView texto = (TextView) findViewById(R.id.vazio_texto);

            if(texto!=null)
                principal.removeView(texto);
        }

        while(conteudo) {

            ConstraintLayout secundario = (ConstraintLayout) getLayoutInflater().inflate(R.layout.linha_aluno, null); // instanciar os objetos que estão em "linha_pomar.xml"

            Button novo = (Button) secundario.findViewById(R.id.aluno);
            nomes.add(alunos.getString(0));
            ids.add(alunos.getInt(1));
            idades.add(alunos.getString(3));
            alergias.add(alunos.getString(4));
            telefones.add(alunos.getString(5));
            emails.add(alunos.getString(6));
            novo.setId(id);
            id += 1;

            novo.setText(alunos.getString(0));

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(5, 10, 5, 0);
            layout.addView(secundario, layoutParams);

            novo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //Inicialização e envio de intento
                    Intent muda = new Intent(v.getContext(), InfoAluno.class);
                    muda.putExtra("turma", turma);
                    muda.putExtra("admin", admin);
                    muda.putExtra("nome", nomes.get((v.getId())));
                    muda.putExtra("id", "" + ids.get((v.getId())));
                    muda.putExtra("idade", idades.get((v.getId())));
                    muda.putExtra("alergia", alergias.get((v.getId())));
                    muda.putExtra("telefone", telefones.get((v.getId())));
                    muda.putExtra("email", emails.get((v.getId())));
                    //muda.putExtra("imagem", imagens.get((v.getId())));
                    startActivity(muda);
                }
            });

            novo.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(final View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(contexto);

                    builder.setTitle("Deseja mesmo apagar este aluno?");
                    builder.setMessage("Esta ação é irreversível");
                    builder.setCancelable(true);

                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() { // botão positivo

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                getContentResolver().delete(provider, "id=" + ids.get(v.getId()) + "", null);
                                onResume();
                                getContentResolver().delete(provider1, "idAluno=" + ids.get(v.getId()) + "", null);
                                getContentResolver().delete(provider2, "id=" + ids.get(v.getId()) + "", null);

                            }catch(Exception e){}
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

                    return true;
                }
            });


            conteudo = alunos.moveToNext();
        }
    }

    @Override
    protected void onStop(){

        super.onStop();

    }

    public void voltar(View v){

        super.finish();
    }

    public void novoAluno(View v){

        Intent intento = new Intent(this,NovoAluno.class);
        intento.putExtra("turma",turma);
        startActivity(intento);
    }

    public void aluno(View v){

        Intent intento = new Intent(this,NovoAluno.class);
        intento.putExtra("turma", turma);
        startActivity(intento);
    }

}