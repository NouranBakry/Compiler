public class separator {

    public String complete = new String();

    public separator(String complete) {
        this.complete = complete;
    }

    public String[] separate(String one){
        boolean end = false;
        String separated[] = new String[3];
        separated[0] = "";
        separated[1] = "";
        separated[2] = "";
        char x = one.charAt(0);

        if(x == '{'){
            separated[0] = one.substring(1,one.length());
            separated[2] = "{";

        }
        else {
            for (char c : one.toCharArray()) {


                if (c == ' ') {
                } else {
                    if (!end) {
                        if (c == ':' || c == '=') {
                            if(c == ':')
                                separated[2] = ":";
                            else
                                separated[2] = "=";
                            end = true;
                            //separated[] += c;

                        } else {
                            separated[0] += c;
                        }

                    } else {

                        separated[1] += c;

                    }
                }
            }
        }
        return separated;
    }

}


