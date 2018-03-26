import java.util.ArrayList;
import java.io.*;



public class Main {
    public static ArrayList <Integer>accept_states;
    public static void main(String[] args) throws IOException {
        NFA last = new NFA();
        String fileName = "input.txt";
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
        accept_states = s.accepting_states;
        DFA dfa_output = s.generate_DFA();
        dfa_output.display_DFA();
        //DFA_Reducer reducer = new DFA_Reducer(dfa_output);
        //reducer.BFS();
        //reducer.partition();
        System.out.println("Maximal Munch: ");
        Maximal_munch.tokenize();


    }
}
