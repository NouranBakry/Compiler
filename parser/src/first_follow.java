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

        // this.LHS = cfg.productions.keySet();


        System.out.println("terminals " + terminals);
        System.out.println("non terminals " + nonterminals);
        System.out.println("productions " + production);

        // System.out.println("length of terminals " + termnal_length);
        //System.out.println("length of non terminals " + nonterminal_length);
        // System.out.println("name of productions " + LHS);


//        for (String k : LHS) {
//            RHS = production.get(k);
//            //System.out.println("Productions for "+k+" is "+RHS);
//
//            for (String w : RHS) {
//
//                //System.out.println(w);
//
//                //System.out.println("the first word in production "+w.split(" ")[0]);
//
//            }
//
//        }

    }


//    public String setNonterminals() {
//        String listString = "";
//        for (String non : nonterminals) {
//            listString += non + "\t";
//        }
//
//        return listString;
//    }

    public String first() {

        ArrayList<String>LHS=new ArrayList<>();
        ArrayList<String>non=new ArrayList<>();
        String test="";
        String str="";
        for(String z:nonterminals){
            non.add(z);
            // System.out.println(z);
        }
        for(String k:production.keySet()){
            LHS.add(k);
            //System.out.println(k);
        }
        //System.out.println(LHS);
        for(int i=0;i<LHS.size();i++){
//            System.out.println(production.get(LHS.get(i)));
            ArrayList<String>RHS=new ArrayList<>(production.get(LHS.get(i)));
//            System.out.println(RHS);
            for(int j=0;j<RHS.size();j++){
                System.out.println(RHS.get(j).split(" ")[0]);
                if(non.contains(RHS.get(j).split(" ")[0])){
                      System.out.println(production.get(RHS.get(j).split(" ")[0]));
//                    int m=non.indexOf(RHS.get(j).split(" ")[0]);
//                    System.out.println(m);
//                    System.out.println("tryyyy "+RHS.get(m).split(" ")[0]);
                    // System.out.println(x);
                    // test=test+str;

                }
                else
                    test=test +"\t"+RHS.get(j).split(" ")[0];
            }
            // System.out.println("first of "+ LHS.get(i) + " "+test);
        }
        System.out.println("First :"+test);

        return test;



    }

}