import java.util.ArrayList;
import java.util.Stack;


    public class DFA {

        public ArrayList <Integer> states;
        public ArrayList <trans> Transitions;
        public ArrayList <Integer> marked;
        public int final_state;
        public NFA nfa;
        public Stack<Integer> stack;

        public DFA (NFA nfa) {
            this.states = new ArrayList<>();
            this.Transitions = new ArrayList<>();
            this.marked = new ArrayList<>();
            this.final_state = 0;
            this.nfa = nfa ;
        }
        public void mark_state(int state_id){
            this.marked.add(state_id);
        }
        public void add_state(int state_id){
            this.states.add(state_id);
        }
        public boolean is_acceptance_state(){

        }
        public void epsilon_closure (){

            ArrayList<trans> transitions = nfa.transitions;
            ArrayList<Integer> st = nfa.states;
            //int last_state = nfa.finalState;
            this.stack = new Stack<>();
            for(int i : st){
                stack.push(i);
            }

            while (!stack.isEmpty()) {

                int top_state = stack.pop();


                }




        }

        public DFA generate_DFA (NFA nfa){








        }


}
