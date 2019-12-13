package pt.ubi.di.pmd.titcherspet;

import android.app.Activity;
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

public class Login extends Activity {

    private EditText nome;
    private EditText password;
    private Uri provider = Uri.parse("content://com.example.dokas.titcherspetadmin.provedor/utilizadores");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        grantUriPermission("com.example.dokas.titcherspetadmin", provider, 0);

    }

    @Override
    protected void onStop(){

        super.onStop();
        //bd.close();
    }

    @Override
    protected void onResume(){

        super.onResume();
        //bd = ajudante.getWritableDatabase();
    }

    public void registar(View v){

        Intent intent = new Intent(this,Registar.class);
        startActivity(intent);
    }

    public void fazerLogin(View v){

        nome = (EditText) findViewById(R.id.nome_texto);
        password = (EditText) findViewById(R.id.pass_texto);

        try{

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] resultado = md.digest(password.getText().toString().getBytes());

            BigInteger aux = new BigInteger(1, resultado);

            String hexadecimal = aux.toString(16);

            while (hexadecimal.length() < 32)
                hexadecimal = "0" + hexadecimal;

            //procurar na base de dados pelo user e pass

            Cursor procura = getContentResolver().query(provider, new String[] {"nome", "password", "admin", "confirmação", "turma"},  "nome='"+ nome.getText().toString() +"' and password='"+hexadecimal+"';", null, null);

            //verificar se existe e se esta no sistema registado e autorizado

            if(procura.moveToFirst()) {

                if(procura.getInt(3) == 1) {

                    //passar parametros no intento
                    Intent login = new Intent(this, AreaPrincipal.class);
                    login.putExtra("nome", procura.getString(0));
                    login.putExtra("turma", procura.getString(4));
                    startActivity(login);
                }

                else{

                    Toast.makeText(this,"Os administradores ainda não aprovaram o seu registo!", Toast.LENGTH_LONG).show();

                }

            }

            else{

                Toast.makeText(this,"Username ou password incorretos!", Toast.LENGTH_LONG).show();

            }

        }

        catch(NoSuchAlgorithmException n){}
    }
}
