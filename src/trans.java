public class trans {

    public int stateFrom, stateTo;
    public char symp;

    public trans(int from, int to, char symp) {
        this.stateFrom = from;
        this.stateTo = to;
        this.symp = symp;
    }
    public int get_next_state(char c){
        if(symp == c)
        {
            return this.stateTo;
        }
        else return -1 ;
    }
}
