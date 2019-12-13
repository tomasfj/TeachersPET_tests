package pt.ubi.di.pmd.titcherspet;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Registar extends Activity {

    private EditText nome;
    private EditText turma;
    private EditText pass;
    private EditText pass_confirmar;
    private Uri provider = Uri.parse("content://com.example.dokas.titcherspetadmin.provedor/utilizadores");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar);

        nome = (EditText) findViewById(R.id.nome_texto);
        turma = (EditText) findViewById(R.id.turma);
        pass = (EditText) findViewById(R.id.pass_texto);
        pass_confirmar = (EditText) findViewById(R.id.pass_texto2);
    }

    @Override
    protected void onStop(){

        super.onStop();

    }

    public void voltarLogin(View v){

        super.finish();
    }

    public void registar(View v){

        if(nome.getText().toString().equals(""))
            Toast.makeText(this,"Introduza um nome válido!", Toast.LENGTH_LONG).show();

        else{

            if(turma.getText().toString().equals(""))
                Toast.makeText(this,"Introduza uma turma válida!", Toast.LENGTH_LONG).show();

            else{

                if(pass.getText().toString().equals(""))
                    Toast.makeText(this,"Introduza uma password válida!", Toast.LENGTH_LONG).show();

                else{

                    if(!pass.getText().toString().equals(pass_confirmar.getText().toString()))

                        Toast.makeText(this,"As Passwords não coincidem!", Toast.LENGTH_LONG).show();

                    else{

                        try{

                            MessageDigest md = MessageDigest.getInstance("MD5");
                            byte[] resultado = md.digest(pass.getText().toString().getBytes());

                            BigInteger aux = new BigInteger(1, resultado);

                            String hexadecimal = aux.toString(16);

                            while (hexadecimal.length() < 32)
                                hexadecimal = "0" + hexadecimal;

                            ContentValues valores = new ContentValues();
                            valores.put("nome", nome.getText().toString());
                            valores.put("password", hexadecimal);
                            valores.put("admin", 0);
                            valores.put("confirmação", 0);
                            valores.put("turma", turma.getText().toString());

                            Uri id = getContentResolver().insert(provider, valores);

                            Toast.makeText(this,"O seu pedido de registo será avaliado pelos administradores em breve!", Toast.LENGTH_LONG).show();
                            super.finish();
                        }

                        catch(NoSuchAlgorithmException n){}
                    }
                }
            }
        }
    }
}
