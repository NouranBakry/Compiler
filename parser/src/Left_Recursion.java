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
        /*Immediate left recursion Elimination.*/
        System.out.println("Elimination of Immediate Left Recursion");
        for (int i = 0; i < left.size(); i++) {
            ArrayList<String> right = new ArrayList<>(productions.get(left.get(i)));
//            System.out.println("Left: "+left.get(i)+" Right: "+ right);
            Recursive = false;
            String new_left = "";
            String alpha = "";
            String Beta = "";
            ArrayList<String> A = new ArrayList<>();
            ArrayList<String> new_rules = new ArrayList<>();
            for(int j=0;j<right.size();j++){
                if(j==0 && right.get(j).startsWith(left.get(i))){
                    Recursive = true;
//                    System.out.println("Left i: "+left.get(i)+"  Right j: "+right.get(j));
                    new_left = left.get(i) +"`";
                    alpha = right.get(j).replaceAll("\\b" +left.get(i) + "\\b *", "").trim();
//                    System.out.println("new left: "+ new_left+"   alpha:"+alpha);
                    new_rules.add(alpha);
                    new_rules.add(new_left);
                    new_rules.add("~"); // epsilon
                }
                if(Recursive && j!=0){
                    Beta += right.get(j);
//                    System.out.println("Beta:"+Beta);
                    if (!Beta.isEmpty()){
                        A.add(Beta);
                    }
                }

            }
            if(!alpha.isEmpty()&&!Beta.isEmpty()&&!new_left.isEmpty()){
                A.add(new_left);
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

