import java.util.ArrayList;
import java.util.Stack;


    public class DFA {

//        public ArrayList <Integer> states;
        public ArrayList <trans> Transitions;
        public ArrayList <Integer> marked;
        public int final_state;
        public NFA nfa;
        public Stack<trans> stack = new Stack<>();

        public DFA (NFA nfa) {
//            this.states = new ArrayList<>();
            this.Transitions = new ArrayList<>();
            this.marked = new ArrayList<>();
            this.final_state = 0;
            this.nfa = nfa ;
        }
        public void mark_state(int state_id){
            this.marked.add(state_id);
        }
//        public void add_state(int state_id){
//            this.states.add(state_id);
//        }
       // public boolean is_acceptance_state(){

        //}
        public void print(ArrayList<trans>t){
            for (trans a : t) {
                System.out.println("(" + a.stateFrom + ", " + a.symp +
                        ", " + a.stateTo + ")");
            }
        }
      public ArrayList<trans> epsilon_closure (){

            trans t;
            ArrayList<trans> state_transitions;
            ArrayList<trans> epsilon_states = new ArrayList<trans>();
            state_transitions = new ArrayList<>(nfa.transitions);
            for(trans a: state_transitions){
                stack.push(a);
            }
            while(!stack.isEmpty()){
                 t = stack.pop();
                 if(t.symp.contains('~') && !epsilon_states.contains(t)){
                     epsilon_states.add(t);
                     stack.push(t);
                 }
            }
            System.out.println("Epsilon");
            print(epsilon_states);
            return epsilon_states;

        }
        public void move_state(){
            int i = 0;
            trans start_state;
            ArrayList<trans> state_transitions;
            state_transitions = new ArrayList<>(nfa.transitions);
            start_state = state_transitions.get(i);
        }
        public void get_initial_transitions(){
            ArrayList<trans> state_transitions;
            state_transitions = new ArrayList<>(nfa.transitions);
            ArrayList<trans> start_state;
            start_state= new ArrayList<>();
            for(trans s: state_transitions){
                if(s.stateFrom==0){
                    start_state.add(s);
                }

            }
            System.out.println("Initial States:\n");
            print(start_state);
//            System.out.println(start_state);
        }


        //public DFA generate_DFA (NFA nfa){








      //  }


}
