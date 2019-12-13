package pt.ubi.di.pmd.titcherspet;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class Tests{

    @Mock
    // the dependency
    private HTTPRequester requester;

    @InjectMocks
    // the classes under test
    private WeatherHelper helper = new WeatherHelper();
    private InputChecker checker = new InputChecker();

    @Before
    public void setUp() throws Exception{

        MockitoAnnotations.initMocks(this);
    }

    // O que queremos
    // (contexto.getResources().getIdentifier("nublado", "drawable", contexto.getPackageName()))

    // O que temos
    // when(weatherAux.getFirst_id()).thenReturn(2131099786);

    @Test
    public void testJSONParser(){ // test the parsing ability of the WeatherHelper class

        String expected_JSON = "{\"DateTime\":\"2019-12-05T12:00:00+00:00\",\"EpochDateTime\":1575547200,\"WeatherIcon\":3,\"IconPhrase\":\"Partly sunny\",\"HasPrecipitation\":false,\"IsDaylight\":true,\"Temperature\":{\"Value\":47.0,\"Unit\":\"F\",\"UnitType\":18},\"PrecipitationProbability\":0,\"MobileLink\":\"http://m.accuweather.com/en/pt/covilha/1-272687_1_al/hourly-weather-forecast/1-272687_1_al?day=1&hbhhour=12&lang=en-us\",\"Link\":\"http://www.accuweather.com/en/pt/covilha/1-272687_1_al/hourly-weather-forecast/1-272687_1_al?day=1&hbhhour=12&lang=en-us\"},{\"DateTime\":\"2019-12-05T13:00:00+00:00\",\"EpochDateTime\":1575550800,\"WeatherIcon\":3,\"IconPhrase\":\"Partly sunny\",\"HasPrecipitation\":false,\"IsDaylight\":true,\"Temperature\":{\"Value\":49.0,\"Unit\":\"F\",\"UnitType\":18},\"PrecipitationProbability\":0,\"MobileLink\":\"http://m.accuweather.com/en/pt/covilha/1-272687_1_al/hourly-weather-forecast/1-272687_1_al?day=1&hbhhour=13&lang=en-us\",\"Link\":\"http://www.accuweather.com/en/pt/covilha/1-272687_1_al/hourly-weather-forecast/1-272687_1_al?day=1&hbhhour=13&lang=en-us\"},{\"DateTime\":\"2019-12-05T14:00:00+00:00\",\"EpochDateTime\":1575554400,\"WeatherIcon\":3,\"IconPhrase\":\"Partly sunny\",\"HasPrecipitation\":false,\"IsDaylight\":true,\"Temperature\":{\"Value\":51.0,\"Unit\":\"F\",\"UnitType\":18},\"PrecipitationProbability\":0,\"MobileLink\":\"http://m.accuweather.com/en/pt/covilha/1-272687_1_al/hourly-weather-forecast/1-272687_1_al?day=1&hbhhour=14&lang=en-us\",\"Link\":\"http://www.accuweather.com/en/pt/covilha/1-272687_1_al/hourly-weather-forecast/1-272687_1_al?day=1&hbhhour=14&lang=en-us\"},{\"DateTime\":\"2019-12-05T15:00:00+00:00\",\"EpochDateTime\":1575558000,\"WeatherIcon\":2,\"IconPhrase\":\"Mostly sunny\",\"HasPrecipitation\":false,\"IsDaylight\":true,\"Temperature\":{\"Value\":51.0,\"Unit\":\"F\",\"UnitType\":18},\"PrecipitationProbability\":0,\"MobileLink\":\"http://m.accuweather.com/en/pt/covilha/1-272687_1_al/hourly-weather-forecast/1-272687_1_al?day=1&hbhhour=15&lang=en-us\",\"Link\":\"http://www.accuweather.com/en/pt/covilha/1-272687_1_al/hourly-weather-forecast/1-272687_1_al?day=1&hbhhour=15&lang=en-us\"},{\"DateTime\":\"2019-12-05T16:00:00+00:00\",\"EpochDateTime\":1575561600,\"WeatherIcon\":2,\"IconPhrase\":\"Mostly sunny\",\"HasPrecipitation\":false,\"IsDaylight\":true,\"Temperature\":{\"Value\":49.0,\"Unit\":\"F\",\"UnitType\":18},\"PrecipitationProbability\":0,\"MobileLink\":\"http://m.accuweather.com/en/pt/covilha/1-272687_1_al/hourly-weather-forecast/1-272687_1_al?day=1&hbhhour=16&lang=en-us\",\"Link\":\"http://www.accuweather.com/en/pt/covilha/1-272687_1_al/hourly-weather-forecast/1-272687_1_al?day=1&hbhhour=16&lang=en-us\"},{\"DateTime\":\"2019-12-05T17:00:00+00:00\",\"EpochDateTime\":1575565200,\"WeatherIcon\":2,\"IconPhrase\":\"Mostly sunny\",\"HasPrecipitation\":false,\"IsDaylight\":true,\"Temperature\":{\"Value\":46.0,\"Unit\":\"F\",\"UnitType\":18},\"PrecipitationProbability\":0,\"MobileLink\":\"http://m.accuweather.com/en/pt/covilha/1-272687_1_al/hourly-weather-forecast/1-272687_1_al?day=1&hbhhour=17&lang=en-us\",\"Link\":\"http://www.accuweather.com/en/pt/covilha/1-272687_1_al/hourly-weather-forecast/1-272687_1_al?day=1&hbhhour=17&lang=en-us\"},{\"DateTime\":\"2019-12-05T18:00:00+00:00\",\"EpochDateTime\":1575568800,\"WeatherIcon\":34,\"IconPhrase\":\"Mostly clear\",\"HasPrecipitation\":false,\"IsDaylight\":false,\"Temperature\":{\"Value\":45.0,\"Unit\":\"F\",\"UnitType\":18},\"PrecipitationProbability\":0,\"MobileLink\":\"http://m.accuweather.com/en/pt/covilha/1-272687_1_al/hourly-weather-forecast/1-272687_1_al?day=1&hbhhour=18&lang=en-us\",\"Link\":\"http://www.accuweather.com/en/pt/covilha/1-272687_1_al/hourly-weather-forecast/1-272687_1_al?day=1&hbhhour=18&lang=en-us\"},{\"DateTime\":\"2019-12-05T19:00:00+00:00\",\"EpochDateTime\":1575572400,\"WeatherIcon\":33,\"IconPhrase\":\"Clear\",\"HasPrecipitation\":false,\"IsDaylight\":false,\"Temperature\":{\"Value\":43.0,\"Unit\":\"F\",\"UnitType\":18},\"PrecipitationProbability\":0,\"MobileLink\":\"http://m.accuweather.com/en/pt/covilha/1-272687_1_al/hourly-weather-forecast/1-272687_1_al?day=1&hbhhour=19&lang=en-us\",\"Link\":\"http://www.accuweather.com/en/pt/covilha/1-272687_1_al/hourly-weather-forecast/1-272687_1_al?day=1&hbhhour=19&lang=en-us\"}";

        when(requester.getResponse()).thenReturn(expected_JSON);

        assertArrayEquals(new String[]{"13","49.0","Partly sunny"},helper.parseJSON(0));
    }

    @Test
    public void testInputChecker_true(){ // test the class responsible for analyzing the text inputs (true case)

        boolean expected = true;
    }

}
