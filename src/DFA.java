import java.util.ArrayList;
import java.util.Stack;


    public class DFA {

        public ArrayList <DFA_State> states;
        public ArrayList <Integer> accepting_states;
        public NFA nfa;
        public DFA (){
            this.states = new ArrayList<DFA_State>();
        }
        public DFA (NFA nfa) {
                this.states = new ArrayList<DFA_State>();
                this.accepting_states = new ArrayList<>();
                this.nfa = nfa ;
        }
        public void set_accepting_states(){
            accepting_states = new ArrayList<>(is_accepting());
            System.out.println("Accepting States");
            for(int i:accepting_states){
                System.out.println(i);
            }
        }

        public void add_new_state(DFA_State d){
            this.states.add(d);
        }


        public ArrayList<Integer> is_accepting(){
            ArrayList<Integer> accepting = new ArrayList<>();
            ArrayList<trans>states;
            states= new ArrayList<>(nfa.transitions);
            Stack<trans> accepting_stack = new Stack<>();
            for(trans t: states){
                if(t.stateTo == nfa.finalState && t.symp.contains('~')){
                    accepting.add(t.stateFrom);
                    accepting_stack.push(t);
                }
            }
            while (!accepting_stack.isEmpty()){
                trans i =accepting_stack.pop();
                for(trans t: states){
                    if(t.stateTo == i.stateFrom && t.symp.contains('~')){
                        accepting_stack.push(t);
                        accepting.add(t.stateFrom);
                    }
                }
            }

            return accepting;
        }
        public void display_DFA() {
            System.out.println("DFA:");
            for (DFA_State t : states) {
                System.out.println("( state_id: " + t.id + ", nfa_states:  " + t.nfa_states + ", state_to:  " + t.stateTo +", symbol:  "+ t.symbol + ")");
            }
        }
}
