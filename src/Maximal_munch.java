import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;
import java.lang.NullPointerException;
import java.util.ArrayList;


public class Maximal_munch {

    public static int state =0, i=0,forward,token_begining=0;
    public static int lexical_value;
    public static String test = new String();
    public static String start=new String();
    public static ArrayList<String>  states= new ArrayList<String> (),accept= new ArrayList <String>(),dead= new ArrayList <String>(),
            keywords= new ArrayList<String> (),relop= new ArrayList<String> (),punctuations= new ArrayList<String> (),
            addop= new ArrayList<String> (),mulop=new ArrayList<String> ();
    public static char c;
    public static String[][] table;
    public static Stack s=new Stack();
    public static String [][] last_accept=new String [10][10];


    public static void initToken(){

        String[] str1={"if","while","else","boolean","int","float"}, str2={"==","<=",">=","!=","<",">"},
                str3={",",";","(",")","{","}","[","]"},str4={"+","-"},str5={"*","/"};
        for (String s : str1)
            keywords.add(s);
        for (String s : str2)
            relop.add(s);
        for (String s : str3)
            punctuations.add(s);
        for (String s : str4)
            addop.add(s);
        for (String s : str5)
            mulop.add(s);


    }

    public static String maximalMunch(){
        i=0;
        while(true){
            String q=table[1][0];
            String d="";

            last_accept[0][0]="error";
            last_accept[0][1]=String.valueOf(i);
            s.push(last_accept);
            while(i<test.length() && !dead.contains(q)){
                int flag=0;
                if(accept.contains(q))
                    s.clear();
                last_accept[0][0]=q;
                last_accept[0][1]=String.valueOf(i);
                s.push(last_accept);
                c=test.charAt(i);

                int cols=table[0].length;
                for (int k=0;k<cols;k++){
                    if(c==' ')
                    {  if(table[0][k].equals("ws")){
                        for (int rows=0;rows<table.length;rows++){
                            if(table[rows][0].equals(q)){
                                q=table[rows][k];
                                flag=1; break;
                            }
                        }
                    }
                    }

                    else if(table[0][k].equals(Character.toString(c))){
                        for (int rows=0;rows<table.length;rows++){
                            if(table[rows][0].equals(q)){
                                q=table[rows][k];
                                flag=1; break;
                            }
                        }
                    }

                }
                if(c=='\n')  {
                    q=dead.get(0);
                    flag=1;
                    i++;}
                if(flag==0){
                    System.out.println("Unidentified character "+c);
                    return "Unidentified character "+c;
                }


                i++;
            }

            if(i+1>=test.length() && accept.contains(q) ){
                for (int t=token_begining;t<i;t++){
                    d=d+test.charAt(t);
                }getToken(d);

                return "Success";
            }
            while(!accept.contains(q)){
                if(!s.isEmpty())
                    last_accept=(String[][]) s.pop();
                q=last_accept[0][0];
                i=Integer.parseInt(last_accept[0][1]);
                if(last_accept[0][0]=="error"){
                    System.out.println("Failed: unable to tokanize");
                    return "Failed: unable to tokanize";}

                for (int t=token_begining;t<i;t++){
                    d=d+test.charAt(t);
                    token_begining=i;}

                if(i>test.length()){
                    for (int t=token_begining;t<i;t++){
                        d=d+test.charAt(t);
                    }
                    getToken(d);
                    return "Success";}
            }

            getToken(d);
        }
    }

    public static void getToken(String token){
        if(keywords.contains(token))
            System.out.println(token);
        else if(relop.contains(token))
            System.out.println("relop");
        else if(punctuations.contains(token))
            System.out.println(token);
        else if(token.equals("="))
            System.out.println("assign");
        else if(token.equals(" "))
            ;
        else if(addop.contains(token))
            System.out.println("addop");
        else if(mulop.contains(token))
            System.out.println("mulop");
        else if(Character.isDigit(token.charAt(0)))
            System.out.println("num");
        else
            System.out.println("id");

    }

    public static void readAcceptStates(){

        int rows=table.length;
        for(int r=1;r<rows;r++){
            if(table[r][0].charAt(0)=='a'){
                accept.add(table[r][0].substring(1));
                table[r][0]=table[r][0].substring(1);  }
        }


    }

    public static void readDeadStates(){

        int rows=table.length;
        for(int r=1;r<rows;r++){
            if(table[r][0].charAt(0)=='x'){
                dead.add(table[r][0].substring(1));
                table[r][0]=table[r][0].substring(1);  }
        }


    }
    public static void readStartState(){

        int rows=table.length;
        for(int r=1;r<rows;r++){
            if(table[r][0].charAt(0)=='s'){
                start=table[r][0].substring(1);
                table[r][0]=table[r][0].substring(1);  }
        }


    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner (new File("table.txt"));
        String text = new Scanner(new File("table.txt")).useDelimiter("\\A").next();
        String[] lines = text.split("\\r?\\n");
        String[] s=lines[0].split(" ");
        table=new String[lines.length][s.length];


        while (input.hasNext()){
            for (int r=0;r<lines.length;r++){
                s=lines[r].split(" ");
                for (int j=0;j<s.length;j++){

                    table[r][j]= input.next();
                }
            }
        }

        initToken();
        test = new Scanner(new File("test.txt")).useDelimiter("\\A").next();
        test = test.replace("\n", " ").replace("\r", " ");
        readAcceptStates();
        readDeadStates();
        readStartState();
        String token= maximalMunch();


    }


}