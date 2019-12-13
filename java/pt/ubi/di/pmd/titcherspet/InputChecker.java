package pt.ubi.di.pmd.titcherspet;

import java.util.regex.Pattern;

public class InputChecker {

    public InputChecker(){}

    public boolean inputCheck(String input, String type){ // check if the given input is according to it's supposed type

        switch(type){

            case "phone_number":
                if(input.length()!=9) // too few or too many characters
                    return false;
                if(input.contains("[a-zA-Z]+")) // a letter
                    return false;
                Pattern p = Pattern.compile("\\[%º,.`´+?'\\ *@#€$&/()=ª:><!±§_-]+"); // a special character
                if(p.matcher(input).find())
                    return false;
                break;

            case "email":
                if(input.indexOf('@')<=0) // lacking the @ character
                    return false;
                if(!input.contains("hotmail") || !input.contains("gmail") || !input.contains("outlook") || !input.contains("ubi")
                    || !input.contains("di.ubi")) // not a usual email provider
                    return false;
                if(!input.contains(".pt") || !input.contains(".com")) // lacking the domain
                    return false;
                break;

            default:
                return false;
        }

        return true;
    }
}
