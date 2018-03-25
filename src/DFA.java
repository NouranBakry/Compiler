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
        for (DFA_State t : states) {
            System.out.println("( state_id: " + t.id + ", nfa_states:  " + t.nfa_states + ", state_to:  " + t.stateTo +", symbol:  "+ t.symbol + ")");
        }
    }

}
