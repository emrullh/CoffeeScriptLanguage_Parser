package coffee.project;

import coffee.IdentifierList;
import coffee.TokenList;
import coffee.datatypes.Keyword;
import coffee.datatypes.Operator;
import coffee.datatypes.Token;
import coffee.syntax.Keywords;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by egencoglu on 12/12/16.
 */
public class Parser {
    private ArrayList<String> strParse =new ArrayList<String>();
    private  ArrayList<String> str = new ArrayList<String>();
    // Parses the lexer result and prints *ONLY* the parsing result.

    /**
     *
     */
    public void parse() {
        IdentifierList identifierList = IdentifierList.getInstance();
        TokenList tokenList = TokenList.getInstance();

        StringBuilder strparse=new StringBuilder();
        ArrayList<String> strExpression =new ArrayList<String>();

        int parentC=0;
        for(Token token : tokenList.getAllTokens()) {
            StringBuilder str= new StringBuilder(token.toString());
            String subStr=str.substring(findIndex(str.toString()));
            if( subStr.equals("(") )
                parentC++;
            else if( subStr.equals(")") )
                parentC--;
            strparse.append(subStr);
            strparse.append(" ");

            if(parentC == 0) {
                strExpression.add(strparse.toString());
                strparse = new StringBuilder();
            }
        }
        //System.out.println("result => "+strparse.toString());//(deffun sumup (x)(if (equal x 0) then 1 else (+ x (sumup (- x 1)))))
        for(int i=0;i<strExpression.size();++i){
          //  System.out.println("result => "+strExpression.get(i));
            strParse=strSplit2(strExpression.get(i));
            System.out.println("START -> INPUT");
            for(int j=strParse.size()-1;j>=0;--j)
                shiftReduceParse(strParse.get(j)); // System.out.println("Result => " + strParse.get(j));

            for(int s=0;s<str.size();++s)
                System.out.println("      -> "+str.get(s));

            strParse =new ArrayList<String>();
            str=new ArrayList<String>();

        }

    }



    /**
     * parse yapilan yer
     * @param line girilen bir expression
     */
    public void shiftReduceParse(String line){

        ArrayList<String> strTemp =new ArrayList<String>();
        ArrayList<String> strTemp2 =new ArrayList<String>();
        Lexer lex =new Lexer();

        String[] parts = line.split(" ");
       /* for(int i=0;i<parts.length;++i)
            System.out.println(" parts > "+i+" "+parts[i]);*/
        String exp1 = lex.findType(parts[1]);
        String exp2 = lex.findType(parts[2]);
        if(lex.isOperator(parts[1]) || isKeyword(parts[1])) {
            if (str.isEmpty() ) {
               // str.add(exp1);
                str.add("(" + parts[1] + " " + exp1 + " " + exp1 + " )");
                str.add("(" + parts[1] + " " + exp1 + " " + exp2 + " )");
                str.add("(" + parts[1] + " " + exp2 + " " + parts[3] + " )");
                str.add("(" + parts[1] + " " + parts[2] + " " + parts[3] + " )");
            } else {
                for (int j = 0; j < str.size(); ++j) {
                    strTemp.add(str.get(j));
                }
                int j;
                str = new ArrayList<String>();
                str.add(exp1);
                str.add("(" + parts[1] + " " + exp1 + " " + exp1 + " )");
                for (j = 0; j < strTemp.size(); ++j)
                    str.add("(" + parts[1] + " " + exp1 + " " + strTemp.get(j) + " )");

                strTemp2 = strSplit2(str.get(j+1));
                str.add("(" + parts[1] + " " + exp2 + "   " + strTemp2.get(1) + " )");
                str.add("(" + parts[1] + " " + parts[2] + "    " + strTemp2.get(1) + " )");
            }
        }else if(parts[1].equals(Keywords.DEFFUN)){
                parts[3]=strParse.get(1);

           /* if (str.isEmpty()) {
                str.add(0, "(" + parts[1] + " " + exp1 + " " + exp1 + " " + exp1 + " )");
                str.add(1, "(" + parts[1] + " " + exp1 + " " + exp1 + " " + exp2 + " )");
                str.add(2, "(" + parts[1] + " " + exp1 + " " + exp2 + " " + parts[3] + " )");
                str.add(3, "(" + parts[1] + " " + exp1 + " " + parts[3] + " " + parts[4] + " )");
                str.add(4, "(" + parts[1] + " " + exp2 + " " + parts[3] + " " + parts[4] + " )");
                str.add(5, "(" + parts[1] + " " + parts[2] + " " + parts[3] + " " + parts[4] + " )");
            } else {*/
                for (int j = 0; j < str.size(); ++j) {
                    strTemp.add(str.get(j));
                }

                int j;
                str = new ArrayList<String>();
                str.add(exp1);
                str.add("(" + parts[1] + " " + exp1 + " " + exp1 + "  " + exp1 + " )");
                for (j = 0; j < strTemp.size(); ++j)
                    str.add("(" + parts[1] + " " + exp1 +  " " + exp1 +"  " + strTemp.get(j) + " )");

                strTemp2 = strSplit2(str.get(j+1));//

                str.add("(" + parts[1] + " " + exp1 + " " +  exp2 + "    " + strTemp2.get(1) + " )");
                str.add("(" + parts[1] + " " + exp1 + " " +  parts[3] + " " + strTemp2.get(1) + " )");
                str.add("(" + parts[1] + " " + exp2 + "   " +  parts[3] + " " + strTemp2.get(1) + " )");
                str.add("(" + parts[1] + " " + parts[2] + " "+ parts[3]+ " " + strTemp2.get(1) + " )");

        }else if (parts[1].equals(Keywords.NOT)){
            if (str.isEmpty() ) {
                str.add(exp1);
                str.add("(" + parts[1] + " " + exp1 + " )");
                str.add("(" + parts[1] + " " + exp2 + ")");
                str.add("(" + parts[1] + " " + parts[2] + " )");

            } else {
                for (int j = 0; j < str.size(); ++j) {
                    strTemp.add(str.get(j));
                }
                int j;
                str = new ArrayList<String>();
                str.add(exp1);
                str.add("(" + parts[1] + " " + exp1 + " )");
                for (j = 0; j < strTemp.size(); ++j)
                    str.add("(" + parts[1] + " " + strTemp.get(j) + " )");

              /* strTemp2 = strSplit2(str.get(j+1));
                str.add("(" + parts[1] + " " + exp2 + "   " + strTemp2.get(1) + " )");
                str.add("(" + parts[1] + " " + parts[2] + "    " + strTemp2.get(1) + " )");*/
            }
        }

    }



    /**
     * her parantez arasini arraylis icinde ic ten disa dogru tutma
     * ic ice expressionlar icin
     * @param line expression satiri
     * @return arraylist te tutma
     */
    public  ArrayList<String>  strSplit2(String line){
        ArrayList<String> keepExp = new ArrayList<String>();
        int expC=0;
        for(int i=0;i<line.length();++i)
            if(line.charAt(i)=='(')
                expC++;

        int arr[]=new int[2*expC];
        int index=0,index2=expC;
        int parentC=0;

        for(int i=0;i<line.length();++i) {
            if (line.charAt(i) == '(') {
                arr[index] = i;
                if (index < expC - 1)
                    index++;
                for (int j = i; j < line.length(); j++) {
                    if (line.charAt(j) == '(')
                        parentC++;
                    else if (line.charAt(j) == ')')
                        parentC--;
                    if ( parentC == 0) {
                        arr[index2] = j+1;
                        if (index2 < expC * 2 - 1)
                            index2++;
                        break;
                    }
                }
            }
        }
        for(int i=0;i<expC;i++)
            keepExp.add(getParentCont(line,arr[i],arr[i+expC]));
        return keepExp;
    }
    /**
     * iki expression alan keywordler
     * @param str string
     * @return boolean
     */
    public boolean isKeyword(String str){
        boolean result=false;
        if(str.equals(Keywords.OR))
            result=true;
        else if(str.equals(Keywords.AND))
            result = true;
        else if(str.equals(Keywords.CONCAT))
            result = true;
        else if(str.equals(Keywords.SET))
            result =true;
        else if(str.equals(Keywords.EQUAL))
            result =true;
        else if(str.equals(Keywords.APPEND))
            result =true;

        return result;
    }
    /**
     * belirli bir karakterin indexini bulma
     * @param line string
     * @return index
     */
    public int findIndex(String line){
        int i;
        for(i=line.length()-1;i>=0;--i){
            if(line.charAt(i) == '_')
                break;
        }
        return i+1;
    }

    /**
     *
     * @param str string
     * @param num ilk index
     * @param num2 ikinci index
     * @return iki index arasindaki string
     */
    public static String getParentCont(String str,int num,int num2){
        return str.substring(num,num2);
    }

}


