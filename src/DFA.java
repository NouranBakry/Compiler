import java.util.ArrayList;


public class DFA {

    public ArrayList <DFA_State> states;
    public NFA nfa;
    public DFA (){
        this.states = new ArrayList<>();
    }
    public DFA (NFA nfa) {
        this.states = new ArrayList<>();
        this.nfa = nfa ;
    }

    public void add_new_state(DFA_State d){
        this.states.add(d);
    }

    public void display_DFA() {
        System.out.println("DFA:");
        System.out.println("Dead State: 0" );
        System.out.print("s ");
        ArrayList<String> inputs = new ArrayList<>();
        for(DFA_State d : states){
            for(String s: d.symbol) {
                if (!inputs.contains(s)) {
                    inputs.add(s);
                } }
        }
        System.out.println(inputs);
        for(DFA_State d : states){
            System.out.println(d.id+" "+d.stateTo);
        }

        System.out.print("\n");
        for (DFA_State t : states) {

            System.out.println("( state_id: " + t.id + ", nfa_states:  " + t.nfa_states + ", state_to:  " + t.stateTo +", symbol:  "+ t.symbol + ")");
        }
    }

}
