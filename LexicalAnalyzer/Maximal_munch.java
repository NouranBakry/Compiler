import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Maximal_munch {
    private static int state=0;
    private static int i = 0;
    private static int forward;
    private static int token_beginning =0;
    private static int lexical_value;
    private static String test = new String();
    private static String start = "1";
    private static String dead = "0";
    private static ArrayList<String>states = new ArrayList<>();
    private static ArrayList<String>accept= new ArrayList <>();
    private static ArrayList<String>keywords= new ArrayList<>();
    private static ArrayList<String>relop= new ArrayList<>();
    private static ArrayList<String>punctuations= new ArrayList<>();
    private static ArrayList<String>addop= new ArrayList<>();
    private static ArrayList<String>mulop=new ArrayList<>();
    private static char c;
    private static String[][] table;
    private static Stack s=new Stack();
    private static String [][] last_accept=new String [10][10];



    public Maximal_munch(){

    }


    public static void initToken(){

        String[] str1={"if","while","else","boolean","int","float"};
        String[] str2={"==","<=",">=","!=","<",">"};
        String[] str3={",",";","(",")","{","}","[","]"};
        String[] str4={"+","-"};
        String[] str5={"*","/"};
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
            String q=start;
            String d="";

            last_accept[0][0]="error";
            last_accept[0][1]=String.valueOf(i);
            s.push(last_accept);

            while(i<test.length() && !dead.equals(q)){
                int flag=0;
                if(accept.contains(q))
                    s.clear();
                last_accept[0][0]=q;
                last_accept[0][1]=String.valueOf(i);
                s.push(last_accept);
                c=test.charAt(i);

                int cols=table[0].length;
                for (int k=1;k<cols;k++){
                    if(c==' ')
                    {   q=dead;
                        flag=1;
                        test=test.substring(0, i) + test.substring(i+1);
                        break;
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

                if(flag==0){
                    System.out.println("Unidentified character "+c);
                    return "Unidentified character "+c;
                }


                i++;
            }

            if(i+1>=test.length() && accept.contains(q) ){
                for (int t = token_beginning; t<i; t++){
                    d=d+test.charAt(t);
                }getToken(d);

                return "Success";
            }

            while(!accept.contains(q) && !s.isEmpty()){

                if(!s.isEmpty())
                    last_accept=(String[][]) s.pop();
                q=last_accept[0][0];
                i=Integer.parseInt(last_accept[0][1]);

                if(last_accept[0][0]=="error"){
                    System.out.println("Failed: unable to tokenize");
                    return "Failed: unable to tokenize";
                }

                for (int t = token_beginning; t<i; t++){
                    d=d+test.charAt(t);
                    token_beginning =i;}

                if(i>test.length()){

                    for (int t = token_beginning; t<i; t++){
                        d=d+test.charAt(t);
                    }

                    getToken(d);
                    return "Success";}
            }

            getToken(d);
        }
    }

    private static void getToken(String token){

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
            System.out.println("num= "+token);
        else
            System.out.println("id= "+token);

    }


    private static void readAcceptStates(String lines){

        String[] a=lines.split(" ");
        for(int r=0;r<a.length;r++){
            accept.add(a[r]);

        }


    }


    public static void tokenize() throws FileNotFoundException {

        Scanner input = new Scanner (new File("minimized_output.txt"));
        String text = new Scanner(new File("minimized_output.txt")).useDelimiter("\\A").next();
        String[] lines = text.split("\\r?\\n");
        String[] s=lines[1].split(" ");
        table=new String[lines.length-1][s.length];
        readAcceptStates(lines[0]);

        input.nextLine();


        for (int r=0;r<lines.length-1;r++){
            s=lines[r+1].split(" ");
            for (int j=0;j<s.length;j++){

                table[r][j]=s[j];
                System.out.print(table[r][j]+" ");


            }   System.out.println();
        }

        test = new Scanner(new File("program.txt")).useDelimiter("\\A").next();
        test = test.replace("\n", " ").replace("\r", "");

        initToken();

        String token= maximalMunch();

    }





}