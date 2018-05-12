import java.util.ArrayList;
import java.util.Arrays;


public class parserTable {

    CFG tempCFG;
    first_follow tempFirstFollow;
    public static String table[][];

    public parserTable(CFG tempCFG,first_follow tempFirstFollow) {
        this.tempCFG = tempCFG;
        this.tempFirstFollow = tempFirstFollow;
    }

    private void startTable(int nonTrerminalRows, int terminalColumns){

        for(int i = 0 ;i < nonTrerminalRows ;i++){

            this.table[i+1][0] = new String(this.tempCFG.nonTerminal.get(i));

        }
        int j;
        for (j= 0; j < terminalColumns ; j++){

            this.table[0][j+1] = new String(this.tempCFG.terminal.get(j));

        }
        this.table[0][j+1] = new String("$");

    }

    public void createTable(){

        int terminal = this.tempCFG.terminal.size();
        int nonTerminal = this.tempCFG.nonTerminal.size();

        table  = new String[nonTerminal+1][terminal + 2];
        startTable(nonTerminal,terminal);
        fillTable(nonTerminal,terminal,table,tempCFG);




        for(int l = 0;l<table.length ;l++){
            System.out.println(Arrays.toString(table[l]));
        }

        //System.out.println(Arrays.deepToString(table));

    }

    public static void fillTable(int nonTerminalRows, int terminalColumns,String table[][],CFG last){

        //Set<String> keys = main.Fmap.keySet();
        //int i = 1;
        //for(String LHS : keys){
        for (int i = 0 ; i < nonTerminalRows ; i++ ){

            String LHS = table[i+1][0];
            if(!main.Fmap.containsKey(LHS)){
                continue;
            }
            String first[] = main.Fmap.get(LHS);
            //System.out.println(first[0]);
            for (int j = 0 ; j < terminalColumns ; j++) {
                String value = table[0][j+1];
                //if(first.contains(value)){

                for (int s = 0; s < first.length; s++) {
                    if (first[s].equals(value)) {
                        String temp = getProduction(LHS, value, last);
                        if (temp == null) {
                            System.out.println("ERROR happened");
                            System.exit(90);
                        }
                        table[i+1][j+1] = temp;
                         System.out.println( table[i+1][0]+" -> "+table[0][j+1]+" -> "+temp);
                        break;

                    }
                }
            }

        }

    }



    private static String getProduction(String nonTerminal, String terminal,CFG last){
        String production= new String();
       ArrayList <String>fullPro = new ArrayList<>();
        boolean moreThanOne = false;

        ArrayList temp = last.productions.get(nonTerminal);
        ArrayList <String>temp2 = new ArrayList<>();      
        for(int value = 0 ; value<temp.size();value++){
            fullPro.add(temp.get(value).toString()); // System.out.println(" fullPro  "+fullPro);
            String parts[] = temp.get(value).toString().split(" ");  
            for (int part = 0 ; part<parts.length ; part++){             
                temp2.add(new String(String.valueOf(value)+"-"+parts[part]));
            }

        }
        if(temp2.size()>1)
            moreThanOne = true;
        int flag=0;
        for(int i = 0 ; i < temp2.size();i++){
            String[] k = temp2.get(i).toString().split("-", 2);  //System.out.println(" k : "+k[1]);
            int result = found(k[1],terminal);  
            if(result == 1){
                if(moreThanOne){
                    production=fullPro.get(Integer.parseInt(k[0]));  //System.out.println(" production  "+production);
                    moreThanOne = false;

                }
                else{
                    production = k[1];
                }
                 return production;
            }
            else if(result == 3){
               // if()
                production = fullPro.get(Integer.parseInt(k[0])); 
                 return production;
            }
            else if(result == 0)
                continue;
                
           
        }

        return null;

    }

    private static int found(String nonTerminal, String terminl){

      if(main.Fmap.get(nonTerminal) == null){  // System.out.println(nonTerminal+"   "+Main.Fmap.get(nonTerminal));
            return 3;
        }
        String first[] = main.Fmap.get(nonTerminal);
        for(int i = 0 ; i < first.length ; i++){
           //System.out.println(nonTerminal+"   "+terminl+"   "+first[i].equals(terminl));
            if(first[i].equals(terminl))
                return 1;}

        return 0;
    }
}