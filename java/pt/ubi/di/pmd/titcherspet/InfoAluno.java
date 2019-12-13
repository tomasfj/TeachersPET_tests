package pt.ubi.di.pmd.titcherspet;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.pdf.parser.Line;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class InfoAluno extends Activity {

    private TextView nome;
    private TextView idade;
    private TextView alergias_texto;
    private TextView telemovel;
    private TextView email;
    private int codReq = 1;
    private  LinearLayout linear;
    private EditText edit_nome;
    private Spinner spin;
    private EditText edit_alergias;
    private EditText edit_telemovel;
    private EditText edit_email;
    private String turma;
    private String id;
    private Uri provider = Uri.parse("content://com.example.dokas.titcherspetadmin.provedor/alunos");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_aluno);
        grantUriPermission("com.example.dokas.titcherspetadmin", provider, 0);

        try{
            Intent intento = getIntent();
            id = intento.getStringExtra("id");
            turma = intento.getStringExtra("turma");
            nome = (TextView) findViewById(R.id.nome);
            nome.setText(intento.getStringExtra("nome"));

            idade = (TextView) findViewById(R.id.idade);
            idade.setText(intento.getStringExtra("idade") + " anos");

            alergias_texto = (TextView) findViewById(R.id.alergias_texto);
            alergias_texto.setText(intento.getStringExtra("alergia"));

            telemovel = (TextView) findViewById(R.id.telemovel);

            if(intento.getStringExtra("telefone").equals("")){

                LinearLayout linear3 = (LinearLayout) findViewById(R.id.linear3);
                linear3.removeView(telemovel);
            }

            else
                telemovel.setText("Telemóvel: " + intento.getStringExtra("telefone"));

            email = (TextView) findViewById(R.id.email);

            if(intento.getStringExtra("email").equals("")){

                LinearLayout linear3 = (LinearLayout) findViewById(R.id.linear3);
                linear3.removeView(email);
            }

            else
                email.setText("Email: " + intento.getStringExtra("email"));
        }

        catch(Exception e){}
    }

    public void voltar(View v){

        super.finish();
    }

    public void editarAluno(View v){

        try {

            linear = (LinearLayout) findViewById(R.id.linear);
            LinearLayout linear1 = (LinearLayout) findViewById(R.id.linear1);
            LinearLayout linear2 = (LinearLayout) findViewById(R.id.linear2);
            LinearLayout linear3 = (LinearLayout) findViewById(R.id.linear3);

            // alterar o botão de editar para apresentar a mensagem "Guardar"
            ConstraintLayout layout_constraint = (ConstraintLayout) findViewById(R.id.constraint_grande);
            ImageButton editar = (ImageButton) findViewById(R.id.edit);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) editar.getLayoutParams();

            ConstraintLayout.LayoutParams newParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT);

            newParams.rightToRight = params.rightToRight;
            newParams.topToTop = params.topToTop;
            newParams.setMargins(0,30,55,0);

            layout_constraint.removeView(editar);

            ConstraintLayout layout_constraint_secundario = (ConstraintLayout) getLayoutInflater().inflate(R.layout.texto_guardar,null);
            Button guardar = (Button) layout_constraint_secundario.findViewById(R.id.guardar);

            layout_constraint_secundario.removeView(guardar);

            layout_constraint.addView(guardar, newParams);

            // botão que permite escolher outra imagem
            LinearLayout linear_aux = (LinearLayout) getLayoutInflater().inflate(R.layout.imagem_novo_aluno_depois, null);
            ImageButton imagem_depois = (ImageButton) linear_aux.findViewById(R.id.imagem_depois);
            linear_aux.removeView(imagem_depois);

            linear.addView(imagem_depois);

            // nome e idade
            LinearLayout linear_secundario = (LinearLayout) getLayoutInflater().inflate(R.layout.edittext_spinner, null);

            TextView nome_idade = (TextView) linear_secundario.findViewById(R.id.nome_idade);
            linear_secundario.removeView(nome_idade);

            edit_nome = (EditText) linear_secundario.findViewById(R.id.edit1);
            edit_nome.setHint(nome.getText().toString());

            linear_secundario.removeView(edit_nome);

            spin = (Spinner) linear_secundario.findViewById(R.id.spin);
            String[] items = new String[]{"3 anos","4 anos","5 anos","6 anos"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.custom_spinner, items);
            spin.setAdapter(adapter);

            linear_secundario.removeView(spin);

            linear1.addView(nome_idade);
            linear1.removeView(nome);
            linear1.addView(edit_nome);
            linear1.removeView(idade);
            linear1.addView(spin);

            // alergias
            linear_secundario = (LinearLayout) getLayoutInflater().inflate(R.layout.edittext_spinner, null);
            edit_alergias = (EditText) linear_secundario.findViewById(R.id.edit1);

            edit_alergias.setHint(alergias_texto.getText().toString());
            linear_secundario.removeView(edit_alergias);

            linear2.removeView(alergias_texto);
            linear2.addView(edit_alergias);

            // informação de contacto

            linear_secundario = (LinearLayout) getLayoutInflater().inflate(R.layout.edittext_spinner, null);
            edit_telemovel = (EditText) linear_secundario.findViewById(R.id.edit1);

            if(telemovel.getText().toString().split(" ")[1].equals("erro"))
                edit_telemovel.setHint("Nenhum telemóvel");

            else
                edit_telemovel.setHint(telemovel.getText().toString().split(" ")[1]);

            edit_telemovel.setInputType(InputType.TYPE_CLASS_NUMBER);
            linear_secundario.removeView(edit_telemovel);

            linear3.removeView(telemovel);
            linear3.addView(edit_telemovel);

            edit_email = (EditText) linear_secundario.findViewById(R.id.edit2);

            if(email.getText().toString().split(" ")[1].equals("erro"))
                edit_email.setHint("Nenhum email");

            else
                edit_email.setHint(email.getText().toString().split(" ")[1]);

            linear_secundario.removeView(edit_email);

            linear3.removeView(email);
            linear3.addView(edit_email);

        }

        catch (Exception e){

            Log.v("TitchersPET",e.getMessage());
        }

    }

    @Override
    protected void onStop(){

        super.onStop();

    }

    @Override
    protected void onResume(){

        super.onResume();

    }

    public void chamada(View v){

        TextView aux = (TextView) findViewById(v.getId());
        int numero = Integer.parseInt(aux.getText().toString().split(" ")[1]);

        Intent intento = new Intent(Intent.ACTION_DIAL);
        intento.setData(Uri.parse("tel:" + numero));

        startActivity(intento);
    }

    public void email(View v){

        TextView aux = (TextView) findViewById(v.getId());
        String email = aux.getText().toString().split(" ")[1];

        Intent intento = new Intent(Intent.ACTION_SEND);
        intento.setType("text/plain");
        intento.putExtra(Intent.EXTRA_EMAIL, new String[] {email});

        startActivity(Intent.createChooser(intento, "Escolha o seu cliente de email"));
    }

    public void obterImagem(View v){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // no Lollipop as permissões são pedidas ao início, nunca em runtime

            // se, por qualquer motivo, não tivermos permissão de escrita no armazenamento externo, vamos pedi-la ao utilizador
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            else {

                Intent intento = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intento, codReq);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == codReq && resultCode == RESULT_OK) {

            try {

                // obter a imagem
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Bitmap final_bitmap = selectedImage;

                // endireitar a imagem
                try {

                    String realPath = ImageFilePath.getPath(this, data.getData());

                    ExifInterface exif = new ExifInterface(realPath);
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

                    switch (orientation) {

                        case 3: Matrix matrix = new Matrix();
                            matrix.postRotate(180);
                            final_bitmap = Bitmap.createBitmap(selectedImage, 0, 0, selectedImage.getWidth(), selectedImage.getHeight(), matrix, true);
                            break;

                        case 6: matrix = new Matrix();
                            matrix.postRotate(90);
                            final_bitmap = Bitmap.createBitmap(selectedImage, 0, 0, selectedImage.getWidth(), selectedImage.getHeight(), matrix, true);
                            break;

                        case 8: matrix = new Matrix();
                            matrix.postRotate(270);
                            final_bitmap = Bitmap.createBitmap(selectedImage, 0, 0, selectedImage.getWidth(), selectedImage.getHeight(), matrix, true);
                            break;
                    }
                }

                catch (IOException e){

                    Log.v("TitchersPET",e.getMessage());
                }

                linear.removeAllViews();

                ConstraintLayout constraint = (ConstraintLayout) getLayoutInflater().inflate(R.layout.imagem_novo_aluno, null);
                ImageView imagem = (ImageView) constraint.findViewById(R.id.imagem);
                imagem.setImageBitmap(final_bitmap);
                constraint.removeView(imagem);

                // botão que permite escolher outra imagem
                LinearLayout constraint2 = (LinearLayout) getLayoutInflater().inflate(R.layout.imagem_novo_aluno_depois, null);
                ImageButton imagem_depois = (ImageButton) constraint2.findViewById(R.id.imagem_depois);
                constraint2.removeView(imagem_depois);

                linear.addView(imagem);
                linear.addView(imagem_depois);
            }

            catch (FileNotFoundException e) {

                Log.v("TitchersPET", e.getMessage());
            }
        }

    }


    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {

        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public void guardar(View v){

        Toast.makeText(this,"Aluno atualizado com sucesso!", Toast.LENGTH_LONG).show();

        ContentValues conteudo = new ContentValues();



        if(edit_nome.getText().toString().equals(""))
            conteudo.put("nome",edit_nome.getHint().toString());

        else
            conteudo.put("nome",edit_nome.getText().toString());

        conteudo.put("id",Integer.parseInt(id));
        conteudo.put("turma",turma);

        conteudo.put("idade",(spin.getSelectedItem().toString()).split(" ")[0]);

        if(edit_alergias.getText().toString().equals(""))
            conteudo.put("alergias",edit_alergias.getHint().toString());

        else
            conteudo.put("alergias",edit_alergias.getText().toString());

        if(edit_telemovel.getText().toString().equals("")){

            if(edit_telemovel.getHint().toString().equals("Nenhum telemóvel"))
                conteudo.put("telemovel",0);

            else
                conteudo.put("telemovel",Integer.parseInt(edit_telemovel.getHint().toString()));
        }

        else
            conteudo.put("telemovel",Integer.parseInt(edit_telemovel.getText().toString()));

        if(edit_email.getText().toString().equals("")){

            if(edit_email.getHint().toString().equals("Nenhum email"))
                conteudo.put("email","");

            else
                conteudo.put("email",edit_email.getHint().toString());
        }

        else
            conteudo.put("email",edit_email.getText().toString());

        getContentResolver().update(provider, conteudo, null, null);

        super.finish();

    }
}