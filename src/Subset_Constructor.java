import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Subset_Constructor {

    public static NFA nfa;
    public static Stack <trans> stack = new Stack<>();
    public Subset_Constructor(NFA nfa){
        this.nfa = nfa;
    }
    public static void print_list(ArrayList<trans>t){
        for (trans a : t) {
            System.out.println("(" + a.stateFrom + ", " + a.symp + ", " + a.stateTo + ")");
        }
    }
    public static ArrayList<Integer> epsilon_closure (ArrayList<trans>e){
        trans t; //stack pop in here
        ArrayList <Integer> epsilon_states = new ArrayList<>();
        for(trans a: e){
            stack.push(a);
        }
        while(!stack.isEmpty()){
            t = stack.pop();
            //System.out.println("t: "+ t.stateFrom +" "+ t.stateTo);
            //System.out.println("epsiloooon lol : "+ epsilon_states);
            int next = t.stateTo;
            //System.out.println("nexxxxxt : " +next);
            //System.out.println("symbooooool: "+ t.symp);
            if(t.symp.contains('~')&&!epsilon_states.contains(next)){
                //System.out.println("HELLO FROM THE OTHER SIDE");
                if(!epsilon_states.contains(t.stateFrom)) {
                    epsilon_states.add(t.stateFrom);
                }
                //t.symp.clear();
                for(trans b:nfa.transitions){
                    if(b.stateFrom==next){
                        //System.out.println("b: "+b.stateFrom);
                        stack.push(b);
                    }
                }
                epsilon_states.add(next);
            }
        }

        return epsilon_states;

    }
    public static ArrayList<trans> get_initial_transitions(ArrayList<trans>state_transitions){
        ArrayList<trans> start_state;
        start_state= new ArrayList<>();
        for(trans s: state_transitions){
            if(s.stateFrom==0){
                start_state.add(s);
            }
        }
        System.out.println("Initial States:");
        print_list(start_state);
        System.out.println("final state:" + nfa.finalState);
        return start_state;

    }
    public static ArrayList<Integer> move (ArrayList<trans>e,String c){
        trans t;
        ArrayList<Integer> states = new ArrayList<>();
        for(trans a: e){
            stack.push(a);
        }
        while(!stack.isEmpty()){
            t = stack.pop();
            int next = t.stateTo;
            //System.out.println("symbol: "+ t.symp+ "c:"+c);
            if(t.symp.toString().equals(c)&&!states.contains(next)){
                states.add(next);
                for(trans p: nfa.transitions){
                    if(p.stateFrom==next){
                        stack.push(p);
                    }
                }

            }
        }
        System.out.println("Function Move:");
        for(int s: states){
            System.out.println("move to:" + s);
        }

        return states;
    }
    public DFA generate_DFA(){
        int state_id = 0;
        HashMap<Integer,String> input = new HashMap<>();
        for(trans t: nfa.transitions){
            if(!t.symp.contains('~')&&!input.containsValue(t.symp.toString())){
                System.out.println("input symbol insertion: "+ t.symp);
                input.put(t.stateFrom,t.symp.toString());
            }
        }
        ArrayList<trans>initial = new ArrayList<>(get_initial_transitions(nfa.transitions));
        //return epsilon closure transitions of initial state.
        ArrayList<Integer> Result = new ArrayList<>(epsilon_closure(initial)) ;

        for(int t: Result){
            System.out.println("epsilon closure removed: "+ t);
        }
        DFA dfa = new DFA();
        DFA_State dfa_start_state = new DFA_State(state_id,Result);
        dfa.add_new_state(dfa_start_state);
        //unmarked.add(dfa_start_state);
        ArrayList<trans> states_to_go = new ArrayList<>();
        ArrayList<Integer> to_remove_epsilon = new ArrayList<>();
        ArrayList<trans> to_epsilon = new ArrayList<>();
        //boolean found_flag = false;
        //while(!unmarked.isEmpty()){
        //DFA_State state = unmarked.removeLast();
        for(String s : input.values()){
            dfa_start_state.symbol.add(s);  //adding symbol to state
            state_id++;
            System.out.println(dfa_start_state.nfa_states);
            ArrayList<Integer> current_states = new ArrayList<>(dfa_start_state.nfa_states);
            for (trans t : nfa.transitions) {
                for (int c : current_states) {
                    //System.out.println("t:"+t.stateFrom+" c:"+ c);
                    if (t.stateFrom == c && dfa_start_state.id != c) {
                        states_to_go.add(t);
                    }
                }
            }
            to_remove_epsilon = move(states_to_go, s);
            System.out.println(to_remove_epsilon);
            if(to_remove_epsilon.isEmpty()){
                dfa.display_DFA();
                break;
            }
            for (trans t : nfa.transitions) {
                for (int c : to_remove_epsilon) {
                    if (t.stateFrom == c && !to_epsilon.contains(t.stateFrom)) {
                        to_epsilon.add(t);
                        System.out.println("to_epsilon adding: " + t.stateFrom);
                    }
                }
            }
                ArrayList <String> c = new ArrayList<>(); //Array of String to add current symbol
                ArrayList <Integer> q = epsilon_closure(to_epsilon);
                c.add(s);
                DFA_State second = new DFA_State(state_id, q);
                dfa_start_state.stateTo.add(state_id);
                for(int d: q){
                    System.out.println("epsilon closure removed: "+ d);
                }
                    dfa.add_new_state(second);
                    dfa.display_DFA();

        }
                return dfa;
    }
}




