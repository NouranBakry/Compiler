import java.io.*;
import java.util.*;


public class main {
    public static Map<String,String> lines= new LinkedHashMap<>();
    public static ArrayList<String> fileIndex = new ArrayList<>();
    public static LinkedHashMap<String,String[]> Fmap = new LinkedHashMap<>();

    public static void main(String[] args) throws IOException {

        String fileName = "input.txt";
        File file = new File(fileName);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                fileIndex.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        constructor myConst = new constructor(fileIndex);
        myConst.separateLines();
        CFG myCFG = new CFG();
        CFG lastCFG = myCFG.controlCFG();

//        System.out.println("START STATE : "+lastCFG.start);
//        System.out.println("TERMINALS : "+lastCFG.terminal);
//        System.out.println("NON-TERMINALS : "+lastCFG.nonTerminal);
//        System.out.println("PRODUCTIONS : "+lastCFG.productions);
        LL1 l = new LL1(lastCFG);
        CFG ambuguity_free = l.eliminate();
        first_follow fst=new first_follow(ambuguity_free);
        for(String k:ambuguity_free.nonTerminal){
            ArrayList<String> temp = new ArrayList<>();
//            System.out.println("first of "+k+" : ");
            fst.first(k,temp);
            //System.out.println(temp);
            String temp2[] = new String[temp.size()];
            temp2 = temp.toArray(temp2);
            Fmap.put(k,temp2);
        }

        fst.follow();
//        parserTable myTable = new parserTable(ambuguity_free,fst);
//        myTable.createTable();


    }
}
