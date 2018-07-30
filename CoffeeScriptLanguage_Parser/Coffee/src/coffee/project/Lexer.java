package coffee.project;

import coffee.IdentifierList;
import coffee.REPL;
import coffee.TokenList;
import coffee.datatypes.*;
import coffee.syntax.Keywords;
import coffee.syntax.Operators;

import java.util.ArrayList;

/**
 * Created by ft on 10/14/15.
 */
public class Lexer implements REPL.LineInputCallback {

    private TokenList tokenlist = TokenList.getInstance();
    private IdentifierList identifierList =IdentifierList.getInstance();

    public ArrayList<String> getOpArr() {
        return opArr;
    }

    private ArrayList<String> opArr = new ArrayList<String>();


    public String lineInput(String line) throws REPL.InvalidSyntaxException {
        // use your own lexer
        Lexer lex =new Lexer();
        lex.keywordsAdd();
        lex.myToken(line);
        return null;
    }


    /**
     * string in token islemi yapilmakta
     * @param line
     */
    public void myToken(String line){
        line= line.toLowerCase();
        String str="";
        for(int i =0; i < line.length() ;++i){
            str =str + line.charAt(i);
            if(isOperator(str)){
                Operator operator= new Operator(str);
                tokenlist.addToken(operator);
                str="";
            }else{
                boolean flag =false;
                for(int k=0;k<opArr.size();++k) {
                    flag = false;
                    int m=0;
                    for (int j = i; j-i < opArr.get(k).length() && m < opArr.get(k).length(); j++) {

                        if (opArr.get(k).charAt(m) != line.charAt(j))
                            break;

                        if(opArr.get(k).length()-1 == m){
                            // System.out.println("keyword str  "+opArr.get(k));
                            Keyword keyword =new Keyword(opArr.get(k));
                            tokenlist.addToken(keyword);
                            flag=true;
                            i +=(m);
                        }
                        ++m;
                    }
                    if(flag)
                        break;
                }
                if(!flag){
                    String strId="";
                    int j=i;
                    while( line.charAt(j) != ' ' ){
                        String strT = ""+line.charAt(j);
                        if(isOperator(strT)){
                            j--;
                            break;
                        }
                        strId += line.charAt(j);
                        j++;
                    }
                    i=j;
                    if(!strId.isEmpty()){
                        if(isNumeric(strId)) {
                            int number = Integer.parseInt(strId);
                            ValueInt valueInt = new ValueInt(number);
                            tokenlist.addToken(valueInt);
                        }else if(strId.equals("true")){
                            ValueBinary valueBinary=new ValueBinary(true);
                            tokenlist.addToken(valueBinary);
                        }else if (strId.equals("false")){
                            ValueBinary valueBinary=new ValueBinary(false);
                            tokenlist.addToken(valueBinary);
                        }
                        else {
                            Identifier identifier= new Identifier(strId);
                            tokenlist.addToken(identifier);
                            identifierList.addIdentifier(strId);
                        }
                    }

                }
                str="";
            }

        }

    }

    /**
     * convert string to integer
     * @param str
     * @return
     */
    public  boolean isNumeric(String str)
    {
        for (int i=0;i<str.length();++i)
        {
            if (!Character.isDigit(str.charAt(i))) return false;
        }
        return true;
    }

    /**
     *
     * @param token stringin operator kontrolu
     * @return boolean type
     */
    public boolean isOperator(String token){

        if(Operators.LEFT_PARENTHESIS.equals(token))
            return true;
        else if(Operators.RIGHT_PARENTHESIS.equals(token))
            return true;
        else if(Operators.ASTERISK.equals(token))
            return true;
        else if(Operators.MINUS.equals(token))
            return true;
        else if(Operators.PLUS.equals(token))
            return true;
        else if (Operators.SLASH.equals(token))
            return true;
        else
            return false;

    }

    /**
     * tekrar tekrar ayni seyleri yazmamak icin bir yerde tuttum
     */
    public void keywordsAdd(){

        opArr.add(Keywords.AND);
        opArr.add(Keywords.APPEND);
        opArr.add(Keywords.CONCAT);
        opArr.add(Keywords.DEFFUN);
        opArr.add(Keywords.ELSE);
        opArr.add(Keywords.EQUAL);
        opArr.add(Keywords.FOR);
        opArr.add(Keywords.FALSE);
        opArr.add(Keywords.IF);
        opArr.add(Keywords.NOT);
        opArr.add(Keywords.OR);
        opArr.add(Keywords.SET);
        opArr.add(Keywords.THEN);
        opArr.add(Keywords.TRUE);
        opArr.add(Keywords.WHILE);
    }
    public String findType (String search){
        TokenList tokens = TokenList.getInstance();
        String result=null;
        int i;
        String[] parts=null;
        for( i=0;i < tokens.getAllTokens().size();++i) {
           // System.out.println(tokens.getAllTokens().get(i).toString());
            parts= tokens.getAllTokens().get(i).toString().split("_");
            if(parts[parts.length-1].equals(search)) {
                if (parts.length == 3)
                    parts[0] = parts[0] + "_" + parts[1];
                break;
            }
        }

        if(parts[0].equals("Operator"))
            result="EXPI";
        else if(parts[0].equals("Keyword"))
            result="EXPB";
        else if(parts[0].equals("IDENTIFIER"))
            result="Id";
        else if(parts[0].equals("VALUE_INT"))
            result="Id";
        return result;
    }

}
