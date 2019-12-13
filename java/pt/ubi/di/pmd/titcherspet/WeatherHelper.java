package pt.ubi.di.pmd.titcherspet;

public class WeatherHelper {

    // dependency
    private HTTPRequester requester;

    public WeatherHelper(){}

    public String[] parseJSON(int index){ // get the hour, temperature and description for one entry (given by "index")

        while(true) {

            String aux = requester.getResponse();

            if(aux.equals("Erro!")){}

            else if(!aux.equals("")) { // we have data to work with

                String corrected = "}," + aux + ",{";
                String[] split = corrected.split("},\\{");

                String[] final_split = new String[4];
                int j = 0;
                for (int i = 2; i < split.length - 1; i += 2) {
                    final_split[j] = split[i];
                    j++;
                    if(j==4)
                        break;
                }

                // get the hour
                String date_aux = final_split[index].split("\"DateTime\":\"")[1];
                String date_time = "";
                int i = 0;
                while(date_aux.charAt(i)!='\"'){

                    if(i>=11 && i<=12){

                        date_time += date_aux.charAt(i);
                    }
                    i++;
                }

                // get the description
                String icon_aux = final_split[index].split("\"IconPhrase\":\"")[1];
                String icon_phrase = "";
                i = 0;
                while(icon_aux.charAt(i)!='\"'){

                    icon_phrase += icon_aux.charAt(i);
                    i++;
                }

                // get the temperature
                String value_aux = final_split[index].split("\"Value\":")[1];
                String value = "";
                i = 0;
                while(value_aux.charAt(i)!=','){

                    value += value_aux.charAt(i);
                    i++;
                }

                return(new String[]{date_time,value,icon_phrase});
            }
        }
    }
}
