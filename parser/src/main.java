import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


public class main {
    public static Map<String,String> lines= new LinkedHashMap<>();
    public static ArrayList<String> fileIndex = new ArrayList<>();

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


        System.out.println("PRODUCTIONS : "+lastCFG.productions);
        LL1 l = new LL1(lastCFG);
        CFG ambuguity_free = l.eliminate();

//        first_follow fst=new first_follow(ambiguity_free);
//        for(String k:ambiguity_free.nonTerminal){
//            System.out.println("first of non terminal "+k+" : ");
//            fst.first(k);
//        }


    }
}