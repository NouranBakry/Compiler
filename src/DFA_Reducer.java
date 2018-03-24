import java.awt.*;
import java.util.*;

public class DFA_Reducer {

    public DFA dfa;
    public Subset_Constructor s;
    public ArrayList<DFA_State> states;
    public ArrayList<Integer> accepting_states;
    public boolean[][] twoD;
    public ArrayList<ArrayList<HashSet<Point>>> P;
    public HashSet<Integer> acceptStates = new HashSet<>();
    public ArrayList<Integer>[] transitions;

    public DFA_Reducer(DFA dfa){
        this.dfa = dfa;
        this.states = dfa.states;
        this.accepting_states = s.accepting_states;
    }
    public void set_transitions(){
        int i =0;
        for(DFA_State d: states){
            transitions[i] = d.stateTo;
            i++;
        }
    }
    public void set_hashset(){
        for(int d:accepting_states){
            acceptStates.add(d);
        }
    }
    public void BFS() { //remove unreachable states (states that have equal transitions)
        System.out.println("Hello");
//        Queue<DFA_State> visitedQueue=new LinkedList<>();
//        for(DFA_State d: states){
//            visitedQueue.add(d);
//        }
//        ArrayList<DFA_State> visitedStates = new ArrayList<>();
//        boolean[] visited=new boolean[states.size()];
//        //Queue<Integer> visitedQueue=new LinkedList<Integer>();
//        //visitedQueue.add(0);
//        while (!visitedQueue.isEmpty()) {
//            //int visit = visitedQueue.remove();
//            DFA_State s = visitedQueue.remove();
//            int visit = s.id;
//            visited[s.id]=true;
//            visitedStates.add(s);
//            //DFAState visitedStates = states[visit];
//            for(DFA_State d: visitedStates){
//                for(int i: d.stateTo){
//                    if(!visited[i]){
//                        for(DFA_State f: states) {
//                            if(f.id==i){
//                                visitedQueue.add(f);
//                            }
//                        }
//                    }
//                }
//            }
////            for (int neighbour : visitedStates.transitions) {
////                if (!visited[neighbour]) {
////                    visitedQueue.add(neighbour);
////                }
////            }
//
//            visited[visit] = true;
//        }
//
//        for(int i=0;i<visited.length;i++){
//            if(!visited[i])
//            {
//                states.remove(i);
//            }
//        }
        ArrayList<Integer>[] transitions = null;
        set_transitions();
        boolean[] visited=new boolean[transitions.length];
        Queue<Integer> visitedQueue=new LinkedList<Integer>();
        visitedQueue.add(0);
        visited[0]=true;
        while (!visitedQueue.isEmpty()) {
            int visit = visitedQueue.remove();
            ArrayList<Integer> visitedStates = transitions[visit];
            for (int neighbour : visitedStates) {
                if (!visited[neighbour]) {
                    visitedQueue.add(neighbour);
                }
            }

            visited[visit] = true;
        }

        for(int i=0;i<visited.length;i++){
            if(!visited[i])
            {
                transitions[i]=null;
            }
        }

    }

    public void partition() {  // {{0},{3},{1,2}}
        set_hashset();
        ArrayList<Integer>[] transitions = null;
        set_transitions();
        //boolean[][] twoD;
       // ArrayList<ArrayList<HashSet<Point>>> P;

        twoD=new boolean[transitions.length][transitions.length];
        P=new ArrayList<ArrayList<HashSet<Point>>>(); //{{0,3},{2}}
        for(int i=0;i<transitions.length;i++){
            ArrayList<HashSet<Point>> inner=new ArrayList<HashSet<Point>>();

            for(int j=0;j<transitions.length;j++){
                Arrays.fill(twoD[i],false);
                inner.add(new HashSet<Point>());
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
                        //sameTrans(i,j);
                        together=true;
                        break;
                    }
                }

                if(!together){
                    for(int w=0;w<si.size();i++){ //if we have {{0,3},{2}} it will be {{0},{3},{2}}
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
        }

        mergeStates();

    }
    private void mergeStates() { //merge states and sort it
        set_hashset();
        ArrayList<Integer>[] transitions = null;
        set_transitions();

        twoD=new boolean[transitions.length][transitions.length];
        P=new ArrayList<ArrayList<HashSet<Point>>>(); //{{0,3},{2}}

        ArrayList<Integer> newStates= new ArrayList<Integer>();
        HashSet<Integer> newaccepedStates=new HashSet<Integer>();
        HashMap<Integer,Integer> merged=new HashMap<Integer, Integer>();
        ArrayList<ArrayList<Integer>> MergeGroup=new ArrayList<ArrayList<Integer>>();

        for(int i=0;i<twoD.length;i++){
            if(merged.get(i)!=null || transitions[i]==null){
                continue;
            }
            //DFAState state=states[i];
            ArrayList<Integer> state =transitions[i];
            ArrayList<Integer> toMerge=new ArrayList<Integer>();
            for(int j=i+1;j<twoD.length;j++){
                if(!twoD[i][j]){
                    toMerge.add(j);
                    merged.put(j,i);
                }
            }

            for(int j=0;j<state.size();j++){
                int tran=state.get(j);
                if(merged.containsKey(tran)){
                    state.set(j,merged.get(tran));
                }
            }

            if(acceptStates.contains(i)){
                newaccepedStates.add(i);
            }
            toMerge.add(i);
            MergeGroup.add(toMerge);
            for(int y: state){
                newStates.add(y);
            }
            //newStates.add(state);

        }

        renumberStates(MergeGroup,newaccepedStates);

        //DFAState [] newGroupedStates=new DFAState[newStates.size()];
        ArrayList<Integer>[] newGroupedStates = new ArrayList [newStates.size()];
        newGroupedStates =newStates.toArray(newGroupedStates);
        transitions=newGroupedStates;
        acceptStates=newaccepedStates;

    }

    private void renumberStates(ArrayList<ArrayList<Integer>> mergeGroup, HashSet<Integer> newaccepedStates) {
        for(int i=0;i<mergeGroup.size();i++){
            ArrayList<Integer> group=mergeGroup.get(i);
            for(ArrayList state:transitions){
                if(state==null){
                    continue;
                }
                for(int j=0;j<state.size();j++){
                    //Integer val= state.get(j);
                    int val = state.indexOf(j); //questionable
                    if(group.contains(val)){
                        state.set(j,i);
                    }
                }
            }

            for(Integer state: new HashSet<Integer>(newaccepedStates)){
                if(group.contains(state)){
                    newaccepedStates.remove(state);
                    newaccepedStates.add(i);
                }
            }

        }
    }

    private void sameTrans(int i, int j) {
        _sameTrans(new Point(i,j),new HashSet<Point>());
    }

    private void _sameTrans(Point point,HashSet<Point>visited) { //if {1,,2} is same transition put them together in new point
        if(visited.contains(point)){
            return;
        }
        int i=point.x;
        int j=point.y;
        twoD[i][j]=true;
        visited.add(point);
        for(Point pair:P.get(i).get(j)){
            _sameTrans(pair,visited);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        for(ArrayList state:transitions){
            for(int i=0;i<state.size();i++){
                //Integer val=state.get(i);
                int val = state.indexOf(i);
                if (i < state.size() - 1) {
                    formatter.format("%d ", val);
                }
                else {
                    formatter.format("%d\n", val);
                }
                // System.out.println(val);

            }
        }
        return sb.toString();
    }
}
