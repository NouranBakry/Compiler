
import java.util.HashMap;

public class decider {
    Thompson n = new Thompson();
    static HashMap<String,NFA> NFAused = new HashMap<>();
    static HashMap<String,NFA> NFAneeded = new HashMap<>();

    String seperated[] = new String[3];

    public decider(String[] seperated) {
        this.seperated = seperated;
    }
    public void decide(String separated []){
        if(separated[2] == "{"){
            boolean start = false;
            String temp =new String();
            temp = "";
            for(int  x = 0;x < separated[0].toCharArray().length;x++){
                char x2 = separated[0].toCharArray()[x];

                if((x2 == ' '&& start && separated[0].toCharArray()[x+1] != ' ') || separated[0].toCharArray()[x] == '}'){
                    start = false;
                    NFA input = n.generateNFA(temp);
                    NFAused.put(separated[0].substring(0,separated[0].length()-1),input);
                    temp = "";
                    System.out.println("\nNFA:");
                    input.display();
                    System.out.println("USED :");
                    for (String name : NFAused.keySet()) {

                        String key = name;
                        NFA x3 = NFAused.get(name);
                        System.out.println(key + " = ");
                        x3.display();
                    }
                    System.out.println("NEEDED:");
                    for (String name : NFAneeded.keySet()) {

                        String key = name;
                        NFA x3 = NFAneeded.get(name);
                        System.out.println(key + " = ");
                        x3.display();


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

            NFA nfa_input = n.generateNFA(separated[1]);
            if(separated[2].equals(":")){
                NFAused.put(separated[0], nfa_input);
            }
            else{
                NFAneeded.put(separated[0], nfa_input);
            }
            System.out.println("\nNFA:");
            nfa_input.display();
            System.out.println("USED :");
            for (String name : NFAused.keySet()) {

                String key = name;
                NFA x = NFAused.get(name);
                System.out.println(key + " = ");
                x.display();
            }
            System.out.println("NEEDED:");
            for (String name : NFAneeded.keySet()) {

                String key = name;
                NFA x = NFAneeded.get(name);
                System.out.println(key + " = ");
                x.display();


            }
        }
    }
}
