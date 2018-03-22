import java.util.Scanner;

public class Main {

    public static void main(String args[]){
        Thompson n = new Thompson();
        Scanner userExpressin = new Scanner(System.in);
        String line;
        System.out.println("Enter you expression");
        line = userExpressin.nextLine();
        if(line.equals(":Q")){
            System.exit(10);
        }
        separator reader = new separator(line);
        String separated[];
        separated = reader.separate(line);
        decider worker = new decider(separated);
        worker.decide(separated);
        System.out.println("Enter you expression");
        NFA nfa_input  = n.generateNFA(line);
        System.out.println("\nNFA:");
        nfa_input.display();
        DFA dfa = new DFA(nfa_input);
        dfa.set_accepting_states();
        Subset_Constructor s = new Subset_Constructor(nfa_input);
        DFA dfa_output = s.generate_DFA();


    }




}