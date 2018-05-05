import java.util.*;

public class Left_Recursion {

    public CFG grammar;
    public ArrayList<String> terminals;
    public ArrayList<String> non_terminals;
    public Map<String, ArrayList<String>> productions;
    public Map<String, ArrayList<String>> new_productions;

    public Left_Recursion(CFG grammar){
        this.grammar = grammar;
        this.terminals = grammar.terminal;
        this.non_terminals = grammar.nonTerminal;
        this.productions = grammar.productions;
    }
    public void eliminate() {

        ArrayList<String> left = new ArrayList<>();
        Boolean Recursive;
        for (String s : productions.keySet()) {
            left.add(s);
        }
        new_productions = new LinkedHashMap<>();
        ArrayList<String> right = new ArrayList<>();
        ArrayList<String> p = new ArrayList<>();
        ArrayList<String>to_remove = new ArrayList<>();
        System.out.println("Elimination of Left Recursion:");
        for (int i = 0; i < left.size(); i++) {
            /*Non immediate left recursion Elimination.*/
            for(int k = 1; k< left.size(); k++){
                String temp = "";
                if(left.get(i)!=left.get(k) ) {
                    right = productions.get((left.get(k)));
                    System.out.println(right + " " + left.get(i));
                    for (String s : right) {
                        if (s.equals(left.get(i))) {
                            System.out.println(s + " " + left.get(i));
                            System.out.println(right);
                            to_remove.add(s);
                            temp = s.replaceAll("\\b" + left.get(i) + "\\b??", "").trim();
                            System.out.println(temp);
                            p = productions.get(left.get(i));
                            System.out.println(p);
                            System.out.println(right);
                        }
                    }
                    right.removeAll(to_remove);
                    for(String q: p){
                        right.add(q+temp);
                    }
                    System.out.println(right);
                }
            }
            right = productions.get(left.get(i));
            Recursive = false;
            String new_left = "";
            String alpha = "";
            String Beta = "";
            ArrayList<String> A = new ArrayList<>();
            ArrayList<String> new_rules = new ArrayList<>();
            for(int j=0;j<right.size();j++){
                /*Immediate left recursion Elimination.*/
                if(right.get(j).startsWith(left.get(i))){
                    Recursive = true;
                    System.out.println("Left i: "+left.get(i)+"  Right j: "+right.get(j));
                    new_left = left.get(i) +"`";
                    alpha = right.get(j).replaceAll("\\b" +left.get(i) + "\\b??", "").trim();
                    System.out.println("new left: "+ new_left+"   alpha:"+alpha);
                    alpha += " ";
                    alpha += new_left;
                    new_rules.add(alpha);
                }
                if(Recursive &&!right.get(j).startsWith(left.get(i))){
                    Beta += right.get(j)+" ";
                    Beta += new_left;
                    System.out.println("Beta:"+Beta);
                    if (!Beta.isEmpty()){
                        A.add(Beta);
                        Beta="";
                    }
                }
            }
            if(!A.isEmpty()&&!new_left.isEmpty()){
                new_rules.add("~"); // epsilon
                new_productions.put(left.get(i),A);
                new_productions.put(new_left, new_rules);
            }
            else{
                new_productions.put(left.get(i),right);
            }
        }
        System.out.println("PRODUCTIONS: "+ new_productions);
    }
}

