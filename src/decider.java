import java.util.HashMap;


public class decider {

    Thompson n = new Thompson();
    static HashMap<String,NFA> NFAused = new HashMap<>();
    static HashMap<String,NFA> NFAneeded = new HashMap<>();
    static HashMap<String,String> NFAsub = new HashMap<>();


    String seperated[] = new String[3];

    public decider(String[] seperated) {
        this.seperated = seperated;
    }

    public NFA decide(String separated []){
        NFA nfa_input = new NFA();
        if(separated[2] == "{"){
            boolean start = false;
            String temp =new String();
            temp = "";
            for(int  x = 0;x < separated[0].toCharArray().length;x++){
                char x2 = separated[0].toCharArray()[x];

                if((x2 == ' '&& start && separated[0].toCharArray()[x+1] != ' ') || separated[0].toCharArray()[x] == '}'){
                    if(temp!=""){
                        start = false;
                        NFA input = n.generateNFA(temp,separated[2]);
                        NFAused.put(separated[0].substring(0,separated[0].length()-1),input);
                        temp = "";

                    }
                }

                else {
                    temp += x2;
                    start = true;

                }

            }
        }

        else{
            System.out.println(separated[0]);
            System.out.println(separated[1]);

            String subLine = n.contain(separated[1]);

            nfa_input = n.generateNFA(subLine,separated[2]);
            if(separated[2].equals(":")){
                NFAused.put(separated[0], nfa_input);
            }
            else{
                NFAneeded.put(separated[0], nfa_input);
                NFAsub.put(separated[0],separated[1]);
            }


        }
        return nfa_input;
    }
}