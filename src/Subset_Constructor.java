import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;


public class Subset_Constructor {


    public static NFA nfa;
    public static Stack <trans> stack = new Stack<>();
    public ArrayList <Integer> accepting_states = new ArrayList<>();
    public ArrayList <Integer> nfa_accepting_states = new ArrayList<>();

    public Subset_Constructor(NFA nfa){

        this.nfa = nfa;
    }


    public static ArrayList<Integer> epsilon_closure (ArrayList<trans>e){
        trans t;
        ArrayList <Integer> epsilon_states = new ArrayList<>();

        for(trans a: e){
            stack.push(a);
        }

        while(!stack.isEmpty()){
            t = stack.pop();
            int next = t.stateTo;

            if(t.symp.contains('~')&&!epsilon_states.contains(next)){
                if(!epsilon_states.contains(t.stateFrom)) {
                    epsilon_states.add(t.stateFrom);
                }

                for(trans b:nfa.transitions){
                    if(b.stateFrom==next){
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

        return start_state;
    }


    public static String getStringRepresentation(ArrayList<Character> list)
    {
        StringBuilder builder = new StringBuilder(list.size());

        for(Character ch: list)
        {
            builder.append(ch);
        }

        return builder.toString();
    }


    public static ArrayList<Integer> move (ArrayList<Integer> e,String c){
        trans t;
        ArrayList<Integer> states = new ArrayList<>();

        for(trans a: nfa.transitions){
            stack.push(a);
        }

        while(!stack.isEmpty()) {
            t = stack.pop();
            String temp = getStringRepresentation(t.symp);
            int next = t.stateTo;

            if ((temp.equals(c) || temp.contains(c)) && !states.contains(next)) {
                if(e.contains(t.stateFrom)){
                    states.add(next);
                }

                for (trans p : nfa.transitions) {
                    if (p.stateFrom == next) {
                        stack.push(p);
                    }
                }
            }
        }

        return states;
    }


    public  void is_accepting(){  //nfa accepting states
        ArrayList<Integer> accepting = new ArrayList<>();
        ArrayList<trans>states;
        states= new ArrayList<>(nfa.transitions);
        Stack<trans> accepting_stack = new Stack<>();
        int flag;

        for(trans t: states){
            if(t.stateTo == nfa.finalState && t.symp.contains('~')&& t.stateFrom!=0){
                accepting.add(t.stateFrom);
                accepting_stack.push(t);

            }

            else if(t.stateTo==nfa.finalState && !accepting.contains(nfa.finalState)){

                accepting_stack.push(t);
            }

        }

        while (!accepting_stack.isEmpty()){
            trans i =accepting_stack.pop();

            for(trans t: states){
                flag = 0;
                if(t.stateTo == i.stateFrom && t.symp.contains('~')&&t.stateFrom!=0){
                    flag=1;
                    accepting_stack.push(t);
                    accepting.add(t.stateFrom);
                }

                else if(flag==0&&!accepting.contains(nfa.finalState)) {
                    accepting.add((nfa.finalState));
                    break;
                }

            }
        }

        nfa_accepting_states = accepting;
        //System.out.println("final state:"+ nfa.finalState);
        //System.out.println("NFA Accepting States");
        //System.out.println(nfa_accepting_states);

    }


    public void set_accept_states(DFA dfa){
        for(int i: nfa_accepting_states){
            for(DFA_State d:dfa.states){
                if(d.nfa_states.contains(i)&&!accepting_states.contains(d.id)){
                    accepting_states.add(d.id);
                }
            }
        }
        System.out.println("DFA Accepting States: ");
        System.out.println(accepting_states);
    }


    public DFA generate_DFA(){
        HashMap <Integer, String> input = new HashMap<>();
        int count = 0;

        for(trans t: nfa.transitions){

            for(char c: t.symp){
                if(c!='~'&&!input.containsValue(Character.toString(c))){
                    count++;
                    input.put(count,Character.toString(c));
                }
            }
        }

        DFA dfa = new DFA();
        DFA_State dead_state = new DFA_State(0);

        for(String s: input.values()){
            dead_state.symbol.add(s);
            dead_state.stateTo.add(0);
        }

        dfa.add_new_state(dead_state);
        int state_id = 1;
        ArrayList<trans>initial = new ArrayList<>(get_initial_transitions(nfa.transitions));
        //return epsilon closure transitions of initial state.
        ArrayList<Integer> Result = new ArrayList<>(epsilon_closure(initial)) ;
        DFA_State dfa_start_state;

        if(Result.size()==0){
            for(trans f: nfa.transitions){
                if(f.stateFrom==0){
                    Result.add(0);
                }
            }
            dfa_start_state = new DFA_State(state_id,Result);
            dfa.add_new_state(dfa_start_state);
        }

        else {
            dfa_start_state = new DFA_State(state_id,Result);
            dfa.add_new_state(dfa_start_state);
        }

        Stack<DFA_State> stack = new Stack<>();
        stack.push(dfa_start_state);

        while(!stack.isEmpty())
        {
            DFA_State new_state = null;
            DFA_State current = stack.pop();

            for(String s: input.values()) {
                int found =0;
                ArrayList<Integer> to_remove_epsilon = move(current.nfa_states, s);
                // dead state
                if (to_remove_epsilon.size() == 0) {
                    current.stateTo.add(0);
                    current.symbol.add(s);
                    found=1;


                }
                ArrayList<trans> to_epsilon = new ArrayList<>();

                for (trans t : nfa.transitions) {
                    for (int c : to_remove_epsilon) {
                        if (t.stateFrom == c && !to_epsilon.contains(t)) {
                            to_epsilon.add(t);
                        }
                    }
                }
                ArrayList<Integer> merged = epsilon_closure(to_epsilon);

                for(int i: to_remove_epsilon){
                    if(!merged.contains(i)){
                        merged.add(i);
                    }
                }

                if (merged.size() == 0) {
                    merged.addAll(to_remove_epsilon);
                }

                for (int i = 0; i < dfa.states.size(); i++) {
                    DFA_State pointer = dfa.states.get(i);
                    if (pointer.nfa_states.containsAll(merged)) {
                        found = 1;
                        break;
                    }
                }

                if (found == 0) {
                        state_id++;
                        new_state = new DFA_State(state_id, merged);
                        stack.push(new_state);
                        dfa.add_new_state(new_state);

                }
            }
        }

        for(DFA_State d: dfa.states) {

            if (d.stateTo.size() != input.size() || d.symbol.size() != input.size()) {
                state_id = d.id;
                d.stateTo.clear();
                d.symbol.clear();

                for (String a : input.values()) {
                    int dead = 0;
                    ArrayList<Integer> to_remove_epsilon = move(d.nfa_states, a);

                    // dead state
                    if (to_remove_epsilon.size() == 0) {
                        d.stateTo.add(0);
                        d.symbol.add(a);
                        dead = 1;


                    }

                    if (dead == 0) {
                        ArrayList<trans> to_epsilon = new ArrayList<>();
                        for (trans t : nfa.transitions) {
                            for (int c : to_remove_epsilon) {
                                if (t.stateFrom == c && !to_epsilon.contains(t)) {
                                    to_epsilon.add(t);
                                }
                            }
                        }

                        ArrayList<Integer> merged = epsilon_closure(to_epsilon);

                        for (int i : to_remove_epsilon) {
                            if (!merged.contains(i)) {
                                merged.add(i);
                            }
                        }

                        if (merged.size() == 0) {
                            merged.addAll(to_remove_epsilon);
                        }

                        for (int i = 0; i < dfa.states.size(); i++) {
                            DFA_State pointer = dfa.states.get(i);
                            if (pointer == d && d.nfa_states.containsAll(merged)) {
                                d.stateTo.add(state_id);
                                d.symbol.add(a);
                                break;
                            } else if (pointer.nfa_states.containsAll(merged)) {
                                d.stateTo.add(pointer.id);
                                d.symbol.add(a);

                                break;
                            }
                        }
                    }
                }
            }
        }

        is_accepting();

        if(dfa!=null) {
            set_accept_states(dfa);
        }

        return dfa;
    }

}
