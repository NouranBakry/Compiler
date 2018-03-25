import java.util.HashMap;
import java.util.Scanner;
import java.io.*;



public class Main {

    public static void main(String[] args) throws IOException {
        NFA last = new NFA();
        //BufferedReader reader=null;
        String fileName = "input2.txt";
        File file = new File(fileName);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line=reader.readLine())!= null){
                separator sep = new separator(line);
                String separated[];
                separated = sep.separate(line);
                decider worker = new decider(separated);
                last = worker.decide(separated);


            }

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("NFA :");
        last.display();

        DFA dfa = new DFA(last);
        Subset_Constructor s = new Subset_Constructor(last);
        DFA dfa_output = s.generate_DFA();
        dfa_output.display_DFA();


    }
}
