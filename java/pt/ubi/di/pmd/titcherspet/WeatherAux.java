package pt.ubi.di.pmd.titcherspet;

public class WeatherAux {

    private GetData weather;
    private String resultados;
    private int first_id;
    private int second_id;
    private int third_id;
    private int forth_id;

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

    public int getFirst_id() {
        return first_id;
    }

    public void setFirst_id(int first_id) {
        this.first_id = first_id;
    }

    public int getSecond_id() {
        return second_id;
    }

    public void setSecond_id(int second_id) {
        this.second_id = second_id;
    }

    public int getThird_id() {
        return third_id;
    }

    public void setThird_id(int third_id) {
        this.third_id = third_id;
    }

    public int getForth_id() {
        return forth_id;
    }

    public void setForth_id(int forth_id) {
        this.forth_id = forth_id;
    }

    public void setResultados(String resultados){

        this.resultados = resultados;
    }
}
