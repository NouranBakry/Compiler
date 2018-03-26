//Construct a partition
import java.awt.*;
import java.util.*;

public class DFA_Reducer {

    public DFA dfa;
    public ArrayList<DFA_State> states;
    public ArrayList<String>symbols=new ArrayList<>();
    // public ArrayList<Integer> accepting_states;
    public ArrayList<Integer> accepting_states = Main.accept_states;
    public boolean[][] twoD;
    public ArrayList<ArrayList<HashSet<Point>>> P;
    public HashSet<Integer> acceptStates = new HashSet<>();
    // public ArrayList<Integer>[] transitions;
    // public int id;

    public DFA_Reducer(DFA dfa){
        this.dfa = dfa;
        this.states = dfa.states;
        //  this.id=id;

    }

    public void set_Symbols() {
        for (DFA_State d : states) {
            for (String k : d.symbol) {
                symbols.add(k);
            }
        }
    }

    public void set_hashset(){
        acceptStates.addAll(accepting_states);
        System.out.print("accept states Nouran "+accepting_states);
    }

    public void BFS() { //remove unreachable states (states that have equal transitions)
        System.out.println("Hello BFS\n");
        // set_Symbols();
        int num=states.size();
        ArrayList<Integer>[]transitions=new ArrayList[num];
        int k = 0;
        for (DFA_State d : states) {
            transitions[k] = d.stateTo;
            k++;

        }
        // transitions[0]=new ArrayList<>(0);
        // transitions[0].add(0);
        //transitions[0].add(0);
        boolean[] visited=new boolean[transitions.length];
        Queue<Integer> visitedQueue= new LinkedList<>();
        visitedQueue.add(1);
        visited[1]=true;
        while (!visitedQueue.isEmpty()) {
            int visit = visitedQueue.remove();
            ArrayList<Integer> visitedStates = transitions[visit];
            for (int neighbour : visitedStates) {
                if(neighbour==0){         //dead state
                    break;
                }
                if (!visited[neighbour]) {
                    visitedQueue.add(neighbour);
                }
                //  System.out.println("neighbour " + neighbour);
            }

            visited[visit] = true;

        }

        for(int i=0;i<visited.length;i++){
            if(!visited[i])
            {
                transitions[i]=null;
                System.out.println("un visited states " + i);

            }
            System.out.println("transitions of BFS "+transitions[i]);
        }
    }

    public void partition() {  // {{0},{3},{1,2}}
        System.out.print("Hello partition\n");
        set_hashset();
        int num=states.size();
        ArrayList<Integer>[]transitions=new ArrayList[num];
        int k = 0;
        for (DFA_State d : states) {
            transitions[k] = d.stateTo;
            k++;
        }
        // transitions[0]=new ArrayList<>(0);

        twoD=new boolean[transitions.length][transitions.length];
        P= new ArrayList<>(); //{{0,3},{2}}
        for(int i=0;i<transitions.length;i++){
            ArrayList<HashSet<Point>> inner= new ArrayList<>();

            for(int j=0;j<transitions.length;j++){
                Arrays.fill(twoD[i],false);
                inner.add(new HashSet<>());
            }
            P.add(inner);
        }

        for(int i=0;i<transitions.length;i++){
            for(int j=i+1;j<transitions.length;j++){
                if(acceptStates.contains(i)!=acceptStates.contains(j)){
                    twoD[i][j]=true;
                }
            }


        }
        for(int i=0;i<transitions.length;i++){
            for(int j=i+1;j<transitions.length;j++){
                if(twoD[i][j]){
                    continue;
                }

                ArrayList<Integer> si=transitions[i];
                ArrayList<Integer> sj=transitions[j];

                if(si==null || sj==null){
                    continue;
                }

                boolean together=false;
                for(int w=0;w<si.size();w++){
                    int m=si.get(w);
                    int n=sj.get(w);

                    if(twoD[m][n]||twoD[n][m]){ //same transitions
                        sameTrans(i,j);
                        // System.out.println("test NOT same states "+i+j);
                        together=true;
                        break;
                    }
                }

                if(!together){
                    for(int w=1;w<si.size();w++){ //if we have {{0,3},{2}} it will be {{0},{3},{2}}
                        int m=si.get(w);
                        int n=sj.get(w);
                        if(m<n && !(i==m && j==n)){
                            P.get(m).get(n).add(new Point(i,j));
                        }
                        else if(m>n && !(i==n && j==m)){
                            P.get(n).get(m).add(new Point(i,j));

                        }
                    }

                }
            }
            // System.out.print("partition"+transitions[i]);
        }
        mergeStates();

    }
    private void mergeStates() { //merge states and sort it by smallest
        System.out.print("\nHello merge States\n");
        ArrayList<ArrayList<Integer>> newStates= new ArrayList<>();
        HashSet<Integer> newaccepedStates= new HashSet<>();
        HashMap<Integer,Integer> merged= new HashMap<>();
        ArrayList<ArrayList<Integer>> MergeGroup= new ArrayList<>();

        set_hashset();
        int num=states.size();
        ArrayList<Integer>[]transitions=new ArrayList[num];
        int k = 0;
        for (DFA_State d : states) {
            transitions[k] = d.stateTo;
            k++;
        }
        //  transitions[0]=new ArrayList<>(0);
        twoD=new boolean[transitions.length][transitions.length];
        P= new ArrayList<>(); //{{0,3},{2}}

        for(int i=0;i<twoD.length;i++){
            if(merged.get(i)!=null || transitions[i]==null){
                continue;
            }

            ArrayList<Integer> state =transitions[i];
            ArrayList<Integer> toMerge=new ArrayList<Integer>();
            for(int j=i+1;j<twoD.length;j++){
                if(!twoD[i][j]){
                    toMerge.add(j);
                    merged.put(j,i);
                }
            }

            for(int j=0;j<state.size();j++){
                Integer tran=state.get(j);
                if(merged.containsKey(tran)){
                    state.set(j,merged.get(tran));
                }
            }

            if(acceptStates.contains(i)){
                newaccepedStates.add(i);
            }
            toMerge.add(i);
            MergeGroup.add(toMerge);
            newStates.add(state);
        }

        renumberStates(MergeGroup,newaccepedStates);

        ArrayList<Integer>[] newGroupedStates = new ArrayList [newStates.size()];
        newGroupedStates =newStates.toArray(newGroupedStates);
        transitions = newGroupedStates;
        acceptStates=newaccepedStates;

        System.out.print("merge group"+MergeGroup);
        System.out.println("New accepted states"+newaccepedStates);

    }

    private void renumberStates(ArrayList<ArrayList<Integer>> mergeGroup, HashSet<Integer> newaccepedStates) {
        System.out.print("Hello renumber states\n");
        int num=states.size()+1;
        ArrayList<Integer>[]transitions=new ArrayList[num];
        int k = 0;
        for (DFA_State d : states) {
            transitions[k] = d.stateTo;
            k++;
        }
        // transitions[0]=new ArrayList<>(0);
        for(int i=0;i<mergeGroup.size();i++){
            ArrayList<Integer> group=mergeGroup.get(i);
            for(ArrayList<Integer> state:transitions){
                if(state==null){
                    continue;
                }
                for(int j=0;j<state.size();j++){
                    Integer val=state.get(j);
                    if(group.contains(val)){
                        state.set(j,i);
                    }
                }


            }

            //  System.out.print("renumber"+group);

            for(Integer state: new HashSet<Integer>(newaccepedStates)){
                if(group.contains(state)){
                    newaccepedStates.remove(state);
                    newaccepedStates.add(i);
                }
            }

        }
    }

    private void sameTrans(int i, int j) {
        //System.out.print("hello NOT same trans\n");
        _sameTrans(new Point(i,j), new HashSet<>());
    }

    private void _sameTrans(Point point,HashSet<Point>visited) { //if {1,,2} is same transition put them together in new point
        if (visited.contains(point)) {
            return;
        }
        int i = point.x;
        int j = point.y;
        twoD[i][j] = true;
        visited.add(point);
        for (Point pair : P.get(i).get(j)) {
            _sameTrans(pair, visited);
        }
    }

    public void display() {
        System.out.println("\ntest print\n");
        HashMap<ArrayList<DFA_State>, ArrayList<DFA_State>> test=new HashMap<>();
        // ArrayList<Integer>=new ArrayList<>();


        int num=states.size()+1;
        ArrayList<Integer>[]transitions=new ArrayList[num];
        int k = 1;
        for (DFA_State d : states) {
            transitions[k] = d.stateTo;
            k++;
        }
        transitions[0]=new ArrayList<>(0);

        set_Symbols();
        System.out.print("s ");
        for(String s: symbols){
            System.out.print(s);
        }
        System.out.print("\n");
        for(DFA_State f: dfa.states){
            System.out.println(f.id);
        }

//        for(ArrayList<Integer>state:transitions) {
//            for (int i = 0; i < state.size(); i++) {
//                Integer val = state.get(i);
//               // System.out.print(val);
//               System.out.print(transitions[i]);
//            }
//        }

        // test.put(dfa.states.id,states);

        //  System.out.print(test);

    }



    public String toString() {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);

        int num=states.size()+1;
        ArrayList<Integer>[]transitions=new ArrayList[num];
        int k = 1;
        for (DFA_State d : states) {
            transitions[k] = d.stateTo;
            k++;
        }
        transitions[0]=new ArrayList<>(0);

        //System.out.print("Accepted States\n");
        ArrayList<Integer> acceptable = new ArrayList<>(acceptStates);
        Collections.sort(acceptable);
        formatter.format("%d ", acceptable.size());
        for (int i = 0; i < acceptable.size(); i++) {
            Integer val = acceptable.get(i);
            if (i < acceptable.size() - 1) {
                formatter.format("%d ", val);
            } else {
                formatter.format("%d\n", val);
            }
        }
//
//        set_Symbols();          btt7asb 3`alt ???????????
//        System.out.print("s ");
//        for(String s: symbols){
//            System.out.print(s);
//        }
        System.out.print("\n");
        for(DFA_State f: dfa.states){
            System.out.println(f.id);
        }

        for(ArrayList<Integer> state:transitions){
            for(int i=0;i<state.size();i++){
                Integer val=state.get(i);
                if (i < state.size()-1) {
                    formatter.format("%d ", val);
                }
                else {
                    formatter.format("%d\n", val);
                }

            }
        }
        return sb.toString();
    }
}
