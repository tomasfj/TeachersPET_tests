package pt.ubi.di.pmd.titcherspet;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

class GetData extends AsyncTask<String,Void,String> {

    public String result = "";

    @Override
    public String doInBackground(String... params) {

        HttpURLConnection conn = null;

        try {

            URL url = new URL("http://dataservice.accuweather.com/forecasts/v1/hourly/12hour/1-272687_1_AL?apikey=GkF2B5oupP2UhaHlUR6osgcLXY4F8dbB");
            conn = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(conn.getInputStream());

            if (in != null) {

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                String line = "";

                while ((line = bufferedReader.readLine()) != null)
                    result += line;
            }
            in.close();
            return result;

        }

        catch (IOException e) {

            result = "Erro!";
        }

        finally {

            if(conn!=null)
                conn.disconnect();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {

        super.onPostExecute(result);
    }

}
