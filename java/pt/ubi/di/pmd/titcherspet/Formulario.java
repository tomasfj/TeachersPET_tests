package pt.ubi.di.pmd.titcherspet;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Formulario extends Activity {

    private TextView nome;
    private CheckBox comeu;
    private CheckBox dormiu;
    private CheckBox coco;
    private CheckBox xixi;
    private CheckBox magoou;
    private CheckBox chorou;
    private EditText notas;
    private int controlo=0;
    private String educadora;
    private SharedPreferences pref;
    private String pref_aux, notas1, turma, email, alergia, idade, telefone, nome_texto;
    private File ficheiro_final;
    private int id;
    private Boolean comeu1, dormiu1, xixi1, coco1, magoou1, chorou1;
    private Context contexto;
    private Button botao_aux;
    private Cursor aluno;
    private Uri provider = Uri.parse("content://com.example.dokas.titcherspetadmin.provedor/relatorios");
    private Uri provider1 = Uri.parse("content://com.example.dokas.titcherspetadmin.provedor/alunos");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        grantUriPermission("com.example.dokas.titcherspetadmin", provider, 0);
        grantUriPermission("com.example.dokas.titcherspetadmin", provider1, 0);

        nome = (TextView) findViewById(R.id.nome);
        comeu = (CheckBox) findViewById(R.id.comeu);
        dormiu = (CheckBox) findViewById(R.id.dormiu);
        coco = (CheckBox) findViewById(R.id.coco);
        xixi = (CheckBox) findViewById(R.id.xixi);
        magoou = (CheckBox) findViewById(R.id.magoou);
        chorou = (CheckBox) findViewById(R.id.chorou);
        notas = (EditText) findViewById(R.id.notas);

        try{

            Intent intento = getIntent();
            nome.setText(intento.getStringExtra("nome"));
            nome_texto = intento.getStringExtra("nome");
            id = Integer.parseInt(intento.getStringExtra("id"));
            educadora = (intento.getStringExtra("nome_educadora"));
            turma = (intento.getStringExtra("turma"));
            email = (intento.getStringExtra("email"));
            idade = (intento.getStringExtra("idade"));
            telefone = (intento.getStringExtra("telefone"));
            alergia = (intento.getStringExtra("alergia"));

        } catch(Exception e) { Log.e("TitchersPET","Erro interno!"); }

        pref = getApplicationContext().getSharedPreferences("Preferencias", 0);
        pref_aux = pref.getString("guardar_copia", "Sim");
        Log.v("TitchersPET",pref_aux);
    }

    public void voltar(View v){

        super.finish();
    }

    @Override
    protected void onResume(){

        super.onResume();

        if(controlo==1 && pref_aux.equals("Não")){

            try{

                ficheiro_final.delete();
                controlo = 0;
            }

            catch (Exception e){}
        }

        if(controlo==1 && pref_aux.equals("Sim")){

            Toast.makeText(this,"Uma cópia do PDF foi guardada na pasta dos Downloads", Toast.LENGTH_LONG).show();
            controlo = 0;
        }
    }

    public void guardar(View v){

        botao_aux = (Button) v;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String data = sdf.format(new Date());

        Cursor relatorios = getContentResolver().query(provider, new String[] {"idAluno", "data", "comeu", "dormiu", "xixi", "coco", "magoou", "chorou", "turma", "notas"},  "idAluno="+ id +" and data='"+data+"'", null, null);

        if (relatorios.moveToFirst()) {

            contexto = this;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Já guardou um formulário deste aluno hoje! Quer continuar?");
            builder.setMessage("Se continuar, o formulário anterior será reescrito");
            builder.setCancelable(true);

            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() { // botão positivo

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String data = sdf.format(new Date());

                    comeu1 = comeu.isChecked();
                    dormiu1 = dormiu.isChecked();
                    coco1 = coco.isChecked();
                    xixi1 = xixi.isChecked();
                    magoou1 = magoou.isChecked();
                    chorou1 = chorou.isChecked();
                    notas1 = notas.getText().toString();

                    ContentValues valores = new ContentValues();
                    valores.put("idAluno", id);
                    valores.put("data", data);
                    if(comeu1)
                        valores.put("comeu", 1);
                    else
                        valores.put("comeu", 0);
                    if(dormiu1)
                        valores.put("dormiu", 1);
                    else
                        valores.put("dormiu", 0);
                    if(coco1)
                        valores.put("coco", 1);
                    else
                        valores.put("coco", 0);
                    if(xixi1)
                        valores.put("xixi", 1);
                    else
                        valores.put("xixi", 0);
                    if(magoou1)
                        valores.put("magoou", 1);
                    else
                        valores.put("magoou", 0);
                    if(chorou1)
                        valores.put("chorou", 1);
                    else
                        valores.put("chorou", 0);
                    valores.put("turma", turma);
                    valores.put("notas", notas1);
                    getContentResolver().update(provider, valores, "idAluno="+id+" and data='"+data+"'", null);

                    Toast.makeText(contexto,"Guardado com sucesso!", Toast.LENGTH_LONG).show();

                    if(botao_aux.getId()==R.id.enviar)
                        enviar(botao_aux);

                    else
                        ((Activity)contexto).finish();
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
        }

        else {

            comeu1 = comeu.isChecked();
            dormiu1 = dormiu.isChecked();
            coco1 = coco.isChecked();
            xixi1 = xixi.isChecked();
            magoou1 = magoou.isChecked();
            chorou1 = chorou.isChecked();
            notas1 = notas.getText().toString();

            ContentValues valores = new ContentValues();
            valores.put("idAluno", id);
            valores.put("data", data);
            if(comeu1)
                valores.put("comeu", 1);
            else
                valores.put("comeu", 0);
            if(dormiu1)
                valores.put("dormiu", 1);
            else
                valores.put("dormiu", 0);
            if(coco1)
                valores.put("coco", 1);
            else
                valores.put("coco", 0);
            if(xixi1)
                valores.put("xixi", 1);
            else
                valores.put("xixi", 0);
            if(magoou1)
                valores.put("magoou", 1);
            else
                valores.put("magoou", 0);
            if(chorou1)
                valores.put("chorou", 1);
            else
                valores.put("chorou", 0);
            valores.put("turma", turma);
            valores.put("notas", notas1);
            getContentResolver().insert(provider, valores);

            Toast.makeText(this,"Guardado com sucesso!", Toast.LENGTH_SHORT).show();

            Log.v("TitchersPET", "" + v.getId() + R.id.enviar);

            if(v.getId()==R.id.enviar && pref_aux.equals("Sim"))
                enviar(v);

            else
                super.finish();
        }
    }

    public void enviar(View v){

        Log.v("TitchersPET", email);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // no Lollipop as permissões são pedidas ao início, nunca em runtime

            // se, por qualquer motivo, não tivermos permissão de escrita no armazenamento externo, vamos pedi-la ao utilizador
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            else {

                // definir a nomenclatura do .pdf a gerar
                String aux[] = nome.getText().toString().split(" ");
                String nome_ficheiro = aux[0] + "_" + aux[aux.length-1];
                SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy");
                String data = sdf.format(new Date());

                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
                String data_email = sdf2.format(new Date());

                // gerar o .pdf
                try {

                    createPdf(Environment.getExternalStorageDirectory() + "/Download/" + nome_ficheiro + "(" + data + ").pdf",data_email);
                }

                catch (DocumentException d){}

                catch (IOException i){}

                ficheiro_final = new File(Environment.getExternalStorageDirectory() + "/Download/" + nome_ficheiro + "(" + data + ").pdf");

                if(email.equals("")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(contexto);

                    builder.setTitle("O aluno não tem endereço de email associado!");
                    builder.setMessage("Impossível realizar ação");
                    builder.setCancelable(true);

                    builder.setPositiveButton("Adicionar email", new DialogInterface.OnClickListener() { // botão positivo

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intento = new Intent(contexto, InfoAluno.class);
                            intento.putExtra("turma", turma);
                            intento.putExtra("admin", 1);
                            intento.putExtra("nome", nome_texto);
                            intento.putExtra("id", "" + id);
                            intento.putExtra("idade", idade);
                            intento.putExtra("alergia", alergia);
                            intento.putExtra("telefone", telefone);
                            intento.putExtra("email", email);
                            startActivity(intento);
                        }
                    });

                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() { // botão negativo

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
                }

                else{
                    // carregar para o email o .pdf gerado
                    Intent intento = new Intent(Intent.ACTION_SEND); // abrir todos os clientes de email do dispositivo
                    Uri sharedFileUri = FileProvider.getUriForFile(this, "pt.ubi.di.pmd.titcherspet.provedor", ficheiro_final);
                    intento.setType("text/plain");
                    intento.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
                    intento.putExtra(Intent.EXTRA_SUBJECT, "Relatório de " + data_email);
                    intento.putExtra(Intent.EXTRA_TEXT, "Em anexo segue o relatório do seu filho " + nome.getText().toString() + " relativo ao dia " + data_email + ".");
                    intento.putExtra(Intent.EXTRA_STREAM, sharedFileUri);

                    controlo = 1;
                    startActivity(Intent.createChooser(intento, "Escolha o seu cliente de email"));
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int reqCode, String[] permissions, int[] grantResults){

        if(reqCode==1){

            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                Log.i("TitchersPET","Permissões obtidas!");

            else
                Toast.makeText(this,"Para aceder a esta feature, considere dar a permissão pedida à app!", Toast.LENGTH_LONG).show();
        }
    }

    public void createPdf(String filename, String dia) throws DocumentException, IOException {

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();

        Paragraph paragraph = new Paragraph(new Phrase(10f,"Relatório Diário",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);

        paragraph = new Paragraph(new Phrase(10f,nome.getText().toString(),
                FontFactory.getFont(FontFactory.HELVETICA, 15)));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingBefore(20f);
        document.add(paragraph);

        paragraph = new Paragraph(new Phrase(10f,dia,
                FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 13)));
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingBefore(20f);
        document.add(paragraph);

        //Image image = Image.getInstance("res/drawable/logo.png");
        //document.add(image);

        paragraph = new Paragraph(new Phrase(10f,"| Resumo do dia",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)));
        paragraph.setAlignment(Element.ALIGN_LEFT);
        paragraph.setSpacingBefore(100f);
        document.add(paragraph);

        int aux = 0;

        if(comeu.isChecked()){

            paragraph = new Paragraph(new Phrase(10f,"   · ",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)));

            paragraph.add(new Phrase(10f,"Comeu bem",
                    FontFactory.getFont(FontFactory.HELVETICA, 15)));
            paragraph.setAlignment(Element.ALIGN_LEFT);
            paragraph.setSpacingBefore(40f);
            document.add(paragraph);

            aux = 1;

        }

        if(dormiu.isChecked()){

            paragraph = new Paragraph(new Phrase(10f,"   · ",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)));

            paragraph.add(new Phrase(10f,"Dormiu bem",
                    FontFactory.getFont(FontFactory.HELVETICA, 15)));
            paragraph.setAlignment(Element.ALIGN_LEFT);

            if(aux==0)
                paragraph.setSpacingBefore(40f);

            else
                paragraph.setSpacingBefore(20f);

            document.add(paragraph);

            aux = 1;

        }

        if(coco.isChecked()){

            paragraph = new Paragraph(new Phrase(10f,"   · ",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)));

            paragraph.add(new Phrase(10f,"Fez cocó",
                    FontFactory.getFont(FontFactory.HELVETICA, 15)));
            paragraph.setAlignment(Element.ALIGN_LEFT);

            if(aux==0)
                paragraph.setSpacingBefore(40f);

            else
                paragraph.setSpacingBefore(20f);

            document.add(paragraph);

            aux = 1;

        }

        if(xixi.isChecked()){

            paragraph = new Paragraph(new Phrase(10f,"   · ",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)));

            paragraph.add(new Phrase(10f,"Fez xixi",
                    FontFactory.getFont(FontFactory.HELVETICA, 15)));
            paragraph.setAlignment(Element.ALIGN_LEFT);

            if(aux==0)
                paragraph.setSpacingBefore(40f);

            else
                paragraph.setSpacingBefore(20f);

            document.add(paragraph);

            aux = 1;

        }

        if(magoou.isChecked()){

            paragraph = new Paragraph(new Phrase(10f,"   · ",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)));

            paragraph.add(new Phrase(10f,"Magoou-se",
                    FontFactory.getFont(FontFactory.HELVETICA, 15)));
            paragraph.setAlignment(Element.ALIGN_LEFT);

            if(aux==0)
                paragraph.setSpacingBefore(40f);

            else
                paragraph.setSpacingBefore(20f);

            document.add(paragraph);

            aux = 1;

        }

        if(chorou.isChecked()){

            paragraph = new Paragraph(new Phrase(10f,"   · ",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)));

            paragraph.add(new Phrase(10f,"Chorou",
                    FontFactory.getFont(FontFactory.HELVETICA, 15)));
            paragraph.setAlignment(Element.ALIGN_LEFT);

            if(aux==0)
                paragraph.setSpacingBefore(40f);

            else
                paragraph.setSpacingBefore(20f);

            document.add(paragraph);

            aux = 1;

        }

        if(aux==0){

            paragraph = new Paragraph(new Phrase(10f,"   (nada a reportar)",
                    FontFactory.getFont(FontFactory.HELVETICA, 15)));

            paragraph.setAlignment(Element.ALIGN_LEFT);
            paragraph.setSpacingBefore(40f);
            document.add(paragraph);
        }

        paragraph = new Paragraph(new Phrase(10f,"| Notas extra",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)));
        paragraph.setAlignment(Element.ALIGN_LEFT);
        paragraph.setSpacingBefore(100f);
        document.add(paragraph);

        if(!notas.getText().toString().equals("")){

            paragraph = new Paragraph(new Phrase(10f,"   \"" + notas.getText().toString() + "\"",
                    FontFactory.getFont(FontFactory.HELVETICA, 15)));
            paragraph.setAlignment(Element.ALIGN_LEFT);
            paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraph.setSpacingBefore(40f);
            document.add(paragraph);
        }

        else{

            paragraph = new Paragraph(new Phrase(10f,"   (nada a reportar)",
                    FontFactory.getFont(FontFactory.HELVETICA, 15)));

            paragraph.setAlignment(Element.ALIGN_LEFT);
            paragraph.setSpacingBefore(40f);
            document.add(paragraph);
        }

        paragraph = new Paragraph(new Phrase(10f,"| Educadora responsável pelo relatório",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)));
        paragraph.setAlignment(Element.ALIGN_LEFT);
        paragraph.setSpacingBefore(100f);
        document.add(paragraph);

        paragraph = new Paragraph(new Phrase(10f,"   " + educadora,
                FontFactory.getFont(FontFactory.HELVETICA, 15)));
        paragraph.setAlignment(Element.ALIGN_LEFT);
        paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
        paragraph.setSpacingBefore(40f);
        document.add(paragraph);

        document.close();
    }
}