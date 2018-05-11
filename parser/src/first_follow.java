import java.util.*;

public class first_follow {
    public CFG cfg;
    public ArrayList<String> terminals;
    public ArrayList<String> nonterminals;
    public Map<String, ArrayList<String>> production;
    int termnal_length, nonterminal_length;
    public Map<String,ArrayList<String>> First_set=new LinkedHashMap<>();

    public first_follow(CFG cfg) {

        this.cfg = cfg;
        this.terminals = cfg.terminal;
        this.nonterminals = cfg.nonTerminal;
        this.production = cfg.productions;
        this.termnal_length = cfg.terminal.size();
        this.nonterminal_length = cfg.nonTerminal.size();

        System.out.println("terminals " + terminals);
        System.out.println("non terminals " + nonterminals);
        System.out.println("productions " + production);


    }

    public String first(String firstToken){
        String str="";
        ArrayList<String>LHS=new ArrayList<>();
        ArrayList<String>nonTERMINAL=new ArrayList<>();
        ArrayList<String>TERMINAL=new ArrayList<>();
        ArrayList<String>first=new ArrayList<>();
        ArrayList<String>tokens=new ArrayList<>();
        for(String z:nonterminals){
            nonTERMINAL.add(z);
        }
        for(String k:production.keySet()){
            LHS.add(k);
        }
        for(String t:terminals){
            TERMINAL.add(t);
        }
        firstToken=firstToken.split(" ")[0];
        tokens.add(firstToken);

        if(TERMINAL.contains(firstToken)){
            str=firstToken;
            first.add(str);
            System.out.println(first);

        }

        else if(firstToken.equals("~")|firstToken.equals("epsilon")){
            str=firstToken;
            first.add(str);
            System.out.println(first);
        }

        else{
            if(nonTERMINAL.contains(firstToken)){
                for(String m:production.get(firstToken)){
                    //System.out.println("right = "+m);
                    first(m);
                }
            }
        }

        return str;

    }

}
