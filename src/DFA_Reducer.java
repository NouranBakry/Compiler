
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class DFA_Reducer {

    public DFA dfa;
    public ArrayList<DFA_State> states;
    public ArrayList<String> symbols = new ArrayList<>();
    public ArrayList<Integer> accepting_states = Main.accept_states;
    public boolean[][] twoD;
    public ArrayList<ArrayList<HashSet<Point>>> P;
    public HashSet<Integer> acceptStates = new HashSet<>();
    //public ArrayList<ArrayList> newStates = new ArrayList<ArrayList>();

    public DFA_Reducer(DFA dfa) {
        this.dfa = dfa;
        this.states = dfa.states;
    }

//    public void set_trans(){
//
//        //int num=states.size();
//        //ArrayList<Integer>[]transitions=new ArrayList[num];
//        int k = 0;
//        for (DFA_State d : states) {
//            transitions[k] = d.stateTo;
//            k++;
//        }
//
//    }

    public void set_Symbols() {
        for (DFA_State d : states) {
            for (String k : d.symbol) {
                if(!symbols.contains(k)){
                symbols.add(k);}
            }
        }
    }

    public void set_hashset() {
        acceptStates.addAll(accepting_states);
        // System.out.print("accept states Nouran "+accepting_states);
    }


    public void BFS() { //remove unreachable states (states that have equal transitions)
        int num = states.size();
        ArrayList<Integer>[] transitions = new ArrayList[num];
        int k = 0;
        for (DFA_State d : states) {
            transitions[k] = d.stateTo;
            k++;

        }

        boolean[] visited = new boolean[transitions.length];
        Queue<Integer> visitedQueue = new LinkedList<>();
        visitedQueue.add(1);
        visited[1] = true;
        while (!visitedQueue.isEmpty()) {
            int visit = visitedQueue.remove();
            ArrayList<Integer> visitedStates = transitions[visit];
            for (int neighbour : visitedStates) {
                if (neighbour == 0) {         //dead state
                    break;
                }
                if (!visited[neighbour]) {
                    visitedQueue.add(neighbour);
                }
            }

            visited[visit] = true;

        }

        for (int i = 0; i < visited.length; i++) {
            if (!visited[i]) {
                transitions[i] = null;
                // System.out.println("un visited states " + i);

            }
            // System.out.println("transitions of BFS "+transitions[i]);
        }
    }


    public void partition() {  // {{0},{3},{1,2}}
        int num = states.size();
        ArrayList<Integer>[] transitions = new ArrayList[num];
        int k = 0;
        for (DFA_State d : states) {
            transitions[k] = d.stateTo;
            k++;

        }

        set_hashset();

        twoD = new boolean[transitions.length][transitions.length];
        P = new ArrayList<ArrayList<HashSet<Point>>>();


        for (int i = 0; i < transitions.length; i++) {
            ArrayList<HashSet<Point>> innerList = new ArrayList<HashSet<Point>>();


            for (int j = 0; j < transitions.length; j++) {
                Arrays.fill(twoD[i], false);
                innerList.add(new HashSet<Point>());
            }
            P.add(innerList);
        }


        for (int i = 0; i < transitions.length; i++) {
            for (int j = i + 1; j < transitions.length; j++) {
                if (acceptStates.contains(i) != acceptStates.contains(j)) {
                    twoD[i][j] = true;
                }
            }
        }


        for (int i = 0; i < transitions.length; i++) {
            for (int j = i + 1; j < transitions.length; j++) {
                // only pairs that are as of yet indistinguishable
                if (twoD[i][j]) {
                    continue;
                }

                ArrayList<Integer> qi = transitions[i];
                ArrayList<Integer> qj = transitions[j];


                if (qi == null || qj == null) {
                    continue;
                }


                boolean distinguished = false;
                for (int z = 0; z < qi.size(); z++) {
                    int m = qi.get(z);
                    int n = qj.get(z);


                    if (twoD[m][n] || twoD[n][m]) {
                        sameTrans(i, j);
                        distinguished = true;
                        break;
                    }
                }

                if (!distinguished) {

                    for (int z = 0; z < qi.size(); z++) {
                        int m = qi.get(z);
                        int n = qj.get(z);

                        if (m < n && !(i == m && j == n)) {
                            P.get(m).get(n).add(new Point(i, j));
                        } else if (m > n && !(i == n && j == m)) {
                            P.get(n).get(m).add(new Point(i, j));
                        }
                    }
                }

            }

            // System.out.println("transitions of Partition "+transitions[i]);
        }

        try {
            mergeStates();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void mergeStates() throws IOException {  //merge states and sort it by smallest
        int num = states.size();
        ArrayList<Integer>[] transitions = new ArrayList[num];
        int k = 0;
        for (DFA_State d : states) {
            transitions[k] = d.stateTo;
            k++;

        }
        set_hashset();

        ArrayList<ArrayList> newStates = new ArrayList<ArrayList>();
        HashSet<Integer> newAcceptStates = new HashSet<Integer>();
        HashMap<Integer, Integer> merged = new HashMap<Integer, Integer>();
        ArrayList<ArrayList<Integer>> mergeGroups = new ArrayList<ArrayList<Integer>>();

        for (int i = 0; i < twoD.length; i++) {
            if (merged.get(i) != null || transitions[i] == null) {
                continue;
            }

            ArrayList<Integer> state = transitions[i];
            //System.out.println("transitions from partitions "+transitions[i]);

            ArrayList<Integer> toMerge = new ArrayList<Integer>();
            for (int j = i + 1; j < twoD.length; j++) {
                if (!twoD[i][j]) {
                    toMerge.add(j);
                    merged.put(j, i);
                }
            }


            for (int j = 0; j < state.size(); j++) {
                Integer transition = state.get(j);
                if (merged.containsKey(transition)) {
                    state.set(j, merged.get(transition));
                }

            }

            if (acceptStates.contains(i)) {
                newAcceptStates.add(i);
            }
            toMerge.add(i);
            mergeGroups.add(toMerge);
            newStates.add(state);

            // System.out.println("transition after merge "+transitions[i]);
        }


        renumberStates(mergeGroups, newAcceptStates);

        System.out.print("merge group AFTER renumber " + mergeGroups);
        System.out.print("\nnew accept states after renumber " + newAcceptStates);


        ArrayList<Integer>[] newStatesArray = new ArrayList[newStates.size()];
        newStatesArray = newStates.toArray(newStatesArray);
        transitions = newStatesArray;
        acceptStates = newAcceptStates;

        FileWriter writer = new FileWriter("minimized_output.txt");
        set_Symbols();
        System.out.println("\nminimization transition table");
        for(int i : acceptStates){
            writer.write(i+" ");
        }
        writer.append(System.lineSeparator());
        writer.write("s ");
        System.out.print("s ");
        for(String s: symbols){
            System.out.print(s+" ");
            writer.write(s+" ");
        }
        System.out.println();
        writer.append(System.lineSeparator());
        for (int r = 0; r < transitions.length; r++) {

            System.out.println(r+ " "+ transitions[r]);
            writer.write(r+" ");
            for(int l=0;l<transitions[r].size();l++){
                writer.write(transitions[r].get(l)+" ");
            }
            writer.append(System.lineSeparator());

        }
        writer.close();
    }


    private void renumberStates(ArrayList<ArrayList<Integer>> groups, HashSet<Integer> newAcceptStates) {

        int num = states.size();
        ArrayList<Integer>[] transitions = new ArrayList[num];
        int k = 0;
        for (DFA_State d : states) {
            transitions[k] = d.stateTo;
            k++;


        }

        for (int i = 0; i < groups.size(); i++) {
            ArrayList<Integer> group = groups.get(i);
            // System.out.println("I "+i);

            //  System.out.println("merged groups from merg fun. "+group);
            for (ArrayList<Integer> state : transitions) {
                if (state == null) {
                    continue;
                }


                for (int j = 0; j < state.size(); j++) {
                    Integer val = state.get(j);
                    if (group.contains(val)) {
                        state.set(j, i);

                    }
                }


//                 for(int q=0;q<states.size();q--) {
//                     for (int x = 0; x < group.size(); x++) {
//                         for (int y = x + 1; y < group.size(); y++) {
//
//                             // transitions[x].remove();
//                             // group.get(x);
//
//
//                             // group.remove(x);
//                             //group.contains(x);
//                             //state.get(x);
//
//                             state = transitions[group.remove(x)];
//
//                             int c = state.get(x);
//                             state.remove(c);
//                             // states.size()
//
//
//                             // System.out.println("testtt "+group.remove(x));
//                             // System.out.println("tran "+state);
//                             // System.out.println(state.get(x));
//                             //System.out.println(state.remove(c));
//
//
//                         }
//                     }
//                 }
            }


            // System.out.println("groups "+group);
            //System.out.println("TEST "+transitions[i]);

            for (Integer state : new HashSet<Integer>(newAcceptStates)) {
                if (group.contains(state)) {
                    newAcceptStates.remove(state);
                    newAcceptStates.add(i);
                }
            }

            // System.out.println("RENUMBER STATES "+i);


        }


    }


    private void sameTrans(int i, int j) {
        //System.out.print("hello NOT same trans\n");
        _sameTrans(new Point(i, j), new HashSet<>());

    }

    private void _sameTrans(Point point, HashSet<Point> visited) { //if {1,2} is NOT same transition put them together in new point
        if (visited.contains(point)) {
            return;
        }
        int i = point.x, j = point.y;
        twoD[i][j] = true;
        visited.add(point);
        for (Point pair : P.get(i).get(j)) {
            _sameTrans(pair, visited);
        }
    }

//    public void display_DFA() throws IOException {
//
//        try (FileWriter writer = new FileWriter("Reduction_output.txt")) {
//            System.out.println("Reduction:");
//            System.out.println("Dead State: 0");
//            System.out.print("s ");
//
//            for (int i : acceptStates) {
//                writer.write(i + " ");
//            }
//
//            writer.append(System.lineSeparator());
//            writer.write("s  ");
//            ArrayList<String> inputs = new ArrayList<>();
//
//            for (DFA_State d : states) {
//
//                for (String s : d.symbol) {
//                    if (!inputs.contains(s)) {
//                        inputs.add(s);
//                        //writer.write(s.replace('[',' ').replace(']',' '));
//                        writer.write(s+"  ");
//                    }
//                }
//            }
//
//            writer.append(System.lineSeparator());
//            System.out.println(inputs);
//
//
//            writer.close();
//        }
//
//        System.out.print("\n");
//    }


}
