import java.util.ArrayList;

public class DFA_State {

    public ArrayList<Integer> nfa_states;
    public int id;
    public ArrayList<Integer> stateTo;
    public ArrayList <String> symbol;

    public DFA_State(int id,ArrayList<Integer>states){
        this.id = id;
        this.nfa_states = new ArrayList<>(states);
        this.stateTo = new ArrayList<>();
        this.symbol = new ArrayList<>();

    }
    public DFA_State(int id,ArrayList<Integer>states,int stateTo){
        this.id = id;
        this.nfa_states = new ArrayList<>(states);
        //this.stateTo = stateTo;
        //this.symbol = new ArrayList<>(symbol);

    }
    public DFA_State(int id,ArrayList<Integer>states,int stateTo, ArrayList<String> c){
        this.id = id;
        this.nfa_states = new ArrayList<>(states);
        //this.stateTo = stateTo;
        //this.symbol = new ArrayList<>(c);

    }
}
