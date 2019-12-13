package pt.ubi.di.pmd.titcherspet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public interface HTTPRequester { // this is just an interface (it has not been implemented yet)

    public String getResponse(); // supposedly will return a JSON with the weather forecast
}
