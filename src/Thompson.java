import java.util.Stack;

public class Thompson {

    public static NFA kleene(NFA current){
        NFA newNFA = new NFA(current.states.size()+2);
        newNFA.transitions.add(new trans(0,1,'~'));

        for (trans t :current.transitions) {

            newNFA.transitions.add(new trans(t.stateFrom + 1, t.stateTo + 1, t.symp));

        }

        newNFA.transitions.add(new trans(current.states.size() , current.states.size() + 1 , '~'));
        newNFA.transitions.add(new trans(current.states.size(),1 ,'~'));
        newNFA.transitions.add(new trans(0 , current.states.size() + 1 , '~'));
        newNFA.finalState = current.states.size() + 1;

        return newNFA;

    }

    public static NFA question(NFA current){
        NFA newNFA =  new NFA(current.states.size() + 2);
        newNFA.transitions.add(new trans(0,1,'~'));
        for (trans t : current.transitions){
            newNFA.transitions.add(new trans(t.stateFrom + 1 ,t.stateTo + 1,t.symp));

        }

        newNFA.transitions.add(new trans(current.states.size() ,current.states.size() + 1 ,'~'));
        newNFA.transitions.add(new trans(0 ,current.states.size() + 1 ,'~'));
        newNFA.finalState=current.states.size() + 1;

        return newNFA;
    }

    public static NFA concat(NFA first,NFA second){
        second.states.remove(0);
        for (trans item :second.transitions){

            first.transitions.add(new trans(item.stateFrom + first.states.size() - 1 , item.stateTo + first.states.size()-1,item.symp));

        }

        for (Integer state :second.states){

            first.states.add(state + first.states.size() + 1);
        }

        first.finalState = (first.finalState - 1) + (second.finalState -1);

        return first;

    }

    public static NFA positive(NFA current){

        NFA newNFA = new NFA(current.states.size()+2);
        newNFA.transitions.add(new trans(0,1,'~'));

        for (trans t :current.transitions) {

            newNFA.transitions.add(new trans(t.stateFrom + 1, t.stateTo + 1, t.symp));

        }

        newNFA.transitions.add(new trans(current.states.size() , current.states.size() + 1 , '~'));
        newNFA.transitions.add(new trans(current.states.size(),1 ,'~'));

        newNFA.finalState = current.states.size() + 1;

        return newNFA;

    }


    public static NFA union(NFA first,NFA second){

        NFA unioned = new NFA(first.states.size() + second.states.size() + 2);
        unioned.transitions.add(new trans(0,1,'~'));

        for (trans  t:first.transitions){

            unioned.transitions.add(new trans(t.stateFrom + 1,t.stateTo + 1,t.symp ));

        }

        unioned.transitions.add(new trans(first.states.size(),first.states.size()+second.states.size()+1,'~'));



        unioned.transitions.add(new trans(0,first.states.size()+1,'~'));

        for (trans  t:second.transitions){

            unioned.transitions.add(new trans(t.stateFrom + first.states.size() +1,t.stateTo + first.states.size() + 1,t.symp ));

        }

        unioned.transitions.add(new trans(first.states.size()+ second.states.size(),first.states.size() + second.states.size()+1,'~'));

        unioned.finalState = first.finalState + second.finalState + 1;

        return unioned;

    }
    public static boolean alpha(char c) {return c >= 'a' && c <= 'z';}
    public static boolean Calpha(char c) {return c >= 'A' && c <='Z';}
    public static boolean isOperand(char c) {return alpha(c) || Calpha(c)|| Character.isDigit(c) || c == '~'; }
    public static boolean isOperator(char c){
        return c == '(' || c == ')' || c == '*' || c == '+' || c == '|' ;

    }

    public static boolean validateChar(char c){

        return isOperand(c) || isOperator(c);
    }

    public static boolean validateRegEx(String regEx){

        if(regEx.isEmpty()){
            System.out.print("your regular expression is empty ya rania bla4 8abawa!!!");
            return false;

        }

        for(char c: regEx.toCharArray()){

            if(!validateChar(c)){
                System.out.print("aktby sa7 ya bta3t 100% ya rania bla4 8abawa!!!");
                return false;

            }

        }

        return true;
    }

    public static NFA generateNFA(String regex){

        if(!validateRegEx(regex)){
            System.out.println("INVALID regular expression");
            return new NFA();
        }

        Stack <Character> operators = new Stack<>();
        Stack <NFA> operands = new Stack<>();
        Stack <NFA> waitedNFA = new Stack<>();


    }
}
