import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String args[]){
        Thompson n = new Thompson();
        Scanner userExpressin = new Scanner(System.in);
        String line;
        System.out.println("Enter you expression");
        line = userExpressin.nextLine();

        NFA nfa_input  = n.generateNFA(line);
        System.out.println("\nNFA:");
        nfa_input.display();

    }




}