package pt.ubi.di.pmd.titcherspet;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.pdf.parser.Line;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class NovoAluno extends Activity {

    private int codReq = 1;
    private LinearLayout linear;
    private String turma;
    private int num = 1;
    private Spinner spin;
    private Uri provider = Uri.parse("content://com.example.dokas.titcherspetadmin.provedor/alunos");
    private Bitmap final_bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_aluno);

        Bundle recebe = getIntent().getExtras();
        turma = recebe.getString("turma");

        linear = (LinearLayout) findViewById(R.id.linear);

        spin = (Spinner) findViewById(R.id.idade);
        String[] items = new String[]{"3 anos","4 anos","5 anos","6 anos"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.custom_spinner, items);
        spin.setAdapter(adapter);
    }

    public void voltar(View v){

        super.finish();
    }

    public void guardar(View v){

        String text = spin.getSelectedItem().toString();

        EditText nome = (EditText) findViewById(R.id.nome);
        EditText alergias = (EditText) findViewById(R.id.alergias);
        EditText telefone = (EditText) findViewById(R.id.telemovel);
        EditText email = (EditText) findViewById(R.id.email);

        if(nome.getText().toString().equals(""))
            Toast.makeText(this,"Introduza um nome válido!",Toast.LENGTH_LONG).show();

        else{

            String alergias_aux = "";

            if(alergias.getText().toString().equals(""))
                alergias_aux = "Nenhuma";

            else
                alergias_aux = alergias.getText().toString();

            if(telefone.getText().toString().equals("") && email.getText().toString().equals(""))
                Toast.makeText(this,"Introduza pelo menos um contacto válido!",Toast.LENGTH_LONG).show();

            else{

                ContentValues valores = new ContentValues();
                valores.put("nome", nome.getText().toString());
                valores.put("id", num);
                valores.put("turma", turma);
                valores.put("idade", text.split(" ")[0]);
                valores.put("alergias", alergias_aux);
                valores.put("telemovel", telefone.getText().toString());
                valores.put("email", email.getText().toString());

                Uri id = getContentResolver().insert(provider, valores);

                Toast.makeText(this,"Aluno guardado com sucesso!", Toast.LENGTH_LONG).show();
                num++;

                Cursor alunos = getContentResolver().query(provider, new String[] {"*"},  "turma='"+ turma +"';", null, null);

                boolean conteudo = alunos.moveToFirst();

                while(conteudo){

                    Log.v("TitchersPET", alunos.getString(0));
                    conteudo = alunos.moveToNext();
                }

                this.finish();

            }
        }
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
                final_bitmap = selectedImage;

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
}
