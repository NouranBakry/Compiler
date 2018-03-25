public class separator {
    public String complete = new String();

    public separator(String complete) {
        this.complete = complete;
    }

    public String[] separate(String one) {
        int k=0;
        boolean end = false;
        if (one.equals("")) {
            System.out.println("l line fady ya rania abtadena nstahbel :D");
            System.exit(9);

        }
        String separated[] = new String[3];
        separated[0] = "";
        separated[1] = "";
        separated[2] = "";
        for (int i = 0; i < one.length(); i++) {
            if (one.charAt(i) == ' ') {
                //k=i;
                continue;
            }
            else {
                k=i;
                break;
            }

        }

        char x = one.charAt(k);

        if (x == '{') {
            separated[0] = one.substring(k + 1, one.length());
            separated[2] = "{";

        } else {
            for (char c : one.toCharArray()) {


                if (c == ' ') {
                } else {
                    if (!end) {
                        if (c == ':' || c == '=') {
                            if (c == ':')
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