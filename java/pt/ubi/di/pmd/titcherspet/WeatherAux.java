package pt.ubi.di.pmd.titcherspet;

public class WeatherAux {

    private GetData weather;
    private String resultados;

    public WeatherAux(String cidade){

        weather = new GetData();
        weather.execute(cidade);
        resultados = weather.result;
    }

    public String getResultados(){

        weather = new GetData();
        weather.execute("Covilhã");
        resultados = weather.result;
        return resultados;
    }

    public void setResultados(String resultados){

        this.resultados = resultados;
    }
}
