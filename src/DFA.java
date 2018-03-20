import java.util.ArrayList;
import java.util.Stack;


    public class DFA {

        public ArrayList <Integer> states;
        public ArrayList <trans> transitions;
        public ArrayList <Integer> marked;
//        public int final_state;
        public int state_id;
        public NFA nfa;
        public Stack<trans> stack = new Stack<>();
        public DFA (NFA nfa) {
              this.state_id = state_id;
              this.transitions = new ArrayList<>();
              this.states = new ArrayList<>();
//            this.Transitions = new ArrayList<>();
              this.marked = new ArrayList<>();
//            this.final_state = 0;
              this.nfa = nfa ;
        }
        public void mark_state(int state_id){
            this.marked.add(state_id);
        }

        public void print(ArrayList<trans>t){
            for (trans a : t) {
                System.out.println("(" + a.stateFrom + ", " + a.symp +
                        ", " + a.stateTo + ")");
            }
        }
      public ArrayList<trans> epsilon_closure (ArrayList<trans>e){

            trans t;
//          ArrayList<trans> state_transitions;
            ArrayList<trans> epsilon_states = new ArrayList<trans>();
//          state_transitions = new ArrayList<>(nfa.transitions);
            for(trans a: e){
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

        public ArrayList<trans> get_initial_transitions(ArrayList<trans>state_transitions){
            ArrayList<trans> start_state;
            start_state= new ArrayList<>();
            for(trans s: state_transitions){
                if(s.stateFrom==0){
                    start_state.add(s);
                }

            }
            System.out.println("Initial States:\n");
            print(start_state);
            System.out.println("final state:" + nfa.finalState);
            return start_state;

        }

        public ArrayList<trans> move (ArrayList<trans>e,ArrayList<Character>c){
            trans t;
            ArrayList<trans> states = new ArrayList<>();
            for(trans a: e){
                stack.push(a);
            }
            while(!stack.isEmpty()){
                t = stack.pop();
                if(t.symp.equals(c) && !states.contains(t)){
                    states.add(t);
                    stack.push(t);
                }
            }
            System.out.println("Function Move:");
            print(states);
            return states;


        }
        public void generate_DFA (){
            ArrayList<trans>initial = get_initial_transitions(nfa.transitions);
//            ArrayList<trans> nfa_start = new ArrayList<>();
//            nfa_start.add(nfa.transitions.get(0));
            ArrayList<trans>Result = epsilon_closure(initial);
            this.state_id =0;
            this.transitions = Result;
            for(trans t: Result){
                ArrayList<Character> c;
                c = new ArrayList<>(t.symp);
                ArrayList<trans> n = move(Result,c);
            }
//            ArrayList<Character> input;
//            input = new ArrayList<>();
//            input.add('a');


//            ArrayList<trans>R = move(nfa.transitions,input);
            ArrayList<Integer>a = is_accepting();
        }
        public ArrayList<Integer> is_accepting(){
            ArrayList<Character> input;
            input = new ArrayList<>();
            input.add('~');

            ArrayList<Integer> accepting = new ArrayList<>();
            ArrayList<trans>states;
            states= new ArrayList<>(nfa.transitions);

            Stack<trans> accepting_stack = new Stack<>();

            for(trans t: states){
                if(t.stateTo == nfa.finalState && t.symp.equals(input)){
                    accepting.add(t.stateFrom);
                    accepting_stack.push(t);
                }
            }
            while (!accepting_stack.isEmpty()){
                trans i =accepting_stack.pop();
                for(trans t: states){
                    if(t.stateTo == i.stateFrom && t.symp.equals(input)){
                        accepting_stack.push(t);
                        accepting.add(t.stateFrom);
                    }
                }
            }

            for(int i: accepting){
                System.out.println("final state:" + nfa.finalState);
                System.out.println("Accepting States");
                System.out.println(i);
            }
            return accepting;
        }

}
