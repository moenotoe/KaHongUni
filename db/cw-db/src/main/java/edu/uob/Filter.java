package edu.uob;



import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Filter {

    private List<String> tokens;
    private int index=0;

    public Filter(List<String> tokens){
        this.tokens=tokens;
    }

    public boolean isBooleanLiteral(){
        if(this.tokens.get(curIndex()).equalsIgnoreCase("TRUE")||this.tokens.get(curIndex()).equalsIgnoreCase("FALSE")){
            return true;
        }
        return false;
    }

    public boolean isNameValueList(){
        if(tokens.size()-curIndex()==1){
            return true;
        }
        if(isNameValuePair()){
            if(tokens.size()-curIndex()==1){
                return true;
            }
            NextToken();
            if(tokens.get(curIndex()).equals(",")){
                NextToken();
                return isNameValueList();
            }
        } else if (isNameValuePair()) {
            return true;

        }
        return false;
    }


    public boolean isNameValuePair(){
        if(tokens.size()-curIndex()==1){
            return true;
        }
        if(isAttriName()){
            NextToken();
            if(tokens.get(curIndex()).equals("=")){
                NextToken();
                if(isValue()){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isGeneralCondition(){
        if(tokens.size()-curIndex()==1){
            return true;
        }
        if (isAttriName()) {
            NextToken();
            if (isComparator()) {
                NextToken();
                if (isValue()) {
                    return true;
                }
            }
        }else if(tokens.get(curIndex()).equals("(")){
            NextToken();
            if (isAttriName()) {
                NextToken();
                if(isComparator()){
                    NextToken();
                    if(isValue()){
                        NextToken();
                        if(tokens.get(curIndex()).equals(")")){
                            return true;
                        }
                    }
                }
            }else if(isGeneralCondition()){
                NextToken();
                if(isBoolOperator()){
                    NextToken();
                    if(isGeneralCondition()){
                        NextToken();
                        if(tokens.get(curIndex()).equals(")")){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    public boolean isComparator(){
        if (this.tokens.get(curIndex()).equals("<")&&this.tokens.get(curIndex()+1).equals("=")){
            NextToken();
            return true;
        }else if (this.tokens.get(curIndex()).equals(">")&&this.tokens.get(curIndex()+1).equals("=")){
            NextToken();
            return true;
        }else if (this.tokens.get(curIndex()).equals("=")&&this.tokens.get(curIndex()+1).equals("=")){
            NextToken();
            return true;
        } else if (this.tokens.get(curIndex()).equals("!")&&this.tokens.get(curIndex()+1).equals("=")){
            NextToken();
            return true;
        }else if(this.tokens.get(curIndex()).equals(">")||this.tokens.get(curIndex()).equals("<")){
            return true;
        } else if (this.tokens.get(curIndex()).equals("=")||this.tokens.get(curIndex()).equals("LIKE")){
            return true;
        } else
        return false;
    }
    public boolean isBoolOperator(){
        if(this.tokens.get(curIndex()).equalsIgnoreCase("AND")||this.tokens.get(curIndex()).equalsIgnoreCase("OR")){
            return true;
        }
        return false;
    }

    //first one must be '/(contain)digi/boo/null/-/+
    public boolean isValue(){

        if(tokens.size()-curIndex()==1){
            return true;
        }
        if(this.tokens.get(curIndex()).equals("'")){
            NextToken();
            NextToken();
            if(this.tokens.get(curIndex()).equals("'")){
                return true;
            }
        }else if(isBooleanLiteral()){
            return true;
        } else if (this.tokens.get(curIndex()).equalsIgnoreCase("NULL")) {
            return true;
        } else if (isDigi()){
            if(tokens.get(curIndex()+1).equals(".")){
                if(isFloat()){
                    return true;
                }
            }else { return true;}
        }else if(this.tokens.get(curIndex()).contains("-")||this.tokens.get(curIndex()).contains("+")){
            if(tokens.get(curIndex()+1).equals(".")){
                if(tokens.get(curIndex()+1).equals(".")){
                    if(isFloat()){
                        return true;
                    }
                }
            }else {
                return true;
            }
        }
        return false;
    }

    public boolean isInteger(){

        if(isDigi()||this.tokens.get(curIndex()).contains("-")||this.tokens.get(curIndex()).contains("+")){
            return true;
        }
        return false;
    }
    public boolean isFloat(){
        if(isDigi()||this.tokens.get(curIndex()).contains("-")||this.tokens.get(curIndex()).contains("+")){
            NextToken();
            if(tokens.get(curIndex()).equals(".")){

                NextToken();
                if(isDigi()){
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isValueList(){
        //terminal con


        if(tokens.size()-curIndex()==1){
            return true;
        }
        if(isValue()){
            if(tokens.size()-curIndex()==1){
                return true;
            }
            NextToken();
            if(tokens.get(curIndex()).equals(",")){
                NextToken();
                return isValueList();
            }
        } else if (isValue()) {
            return true;
        }
        return false;
    }


    public boolean isDigi(){

        if (this.tokens.get(curIndex()).startsWith("0")||this.tokens.get(curIndex()).startsWith("1")) {
            return true;
        }else if (this.tokens.get(curIndex()).startsWith("2")||this.tokens.get(curIndex()).startsWith("3")) {
            return true;
        }else if (this.tokens.get(curIndex()).startsWith("4")||this.tokens.get(curIndex()).startsWith("5")) {
            return true;
        } else if (this.tokens.get(curIndex()).startsWith("6")||this.tokens.get(curIndex()).startsWith("7")) {
            return true;
        } else if (this.tokens.get(curIndex()).startsWith("8")||this.tokens.get(curIndex()).startsWith("9")) {
            return true;
        }
        return false;
    }




    public boolean checkRepeatAtt(List<String> attList,String tabName) {
        Set<String> set=new HashSet<String>();
        for (String str:attList) {
            if (!(set.add(str))&&!str.equals(".")&&!str.equals(tabName)) {
                return false;
            }
        }
        return true;
    }
    public boolean isWildAttribList(){
        if(tokens.size()-curIndex()==1){
            return true;
        }
        if(tokens.get(0).equals("*")||tokens.get(1).equals("*")){
            return true;
        } else if(isAttribList()) {
            return true;
        }
        return false;
    }

    public boolean isSymbol(){
        if(!isPlainTest()){
            return true;
        }
        return false;
    }

    public boolean isPlainTest(){
        if(tokens.get(curIndex()).contains("!")||tokens.get(curIndex()).contains("#")){
            return false;
        }else if( tokens.get(curIndex()).contains("$")||tokens.get(curIndex()).contains("%")){
            return false;
        }else if( tokens.get(curIndex()).contains("&")||tokens.get(curIndex()).contains("(")){
            return false;
        }else if( tokens.get(curIndex()).contains(")")||tokens.get(curIndex()).contains("*")){
            return false;
        }else if( tokens.get(curIndex()).contains("+")||tokens.get(curIndex()).contains("-")){
            return false;
        }else if( tokens.get(curIndex()).contains(",")||tokens.get(curIndex()).contains("/")){
            return false;
        }else if( tokens.get(curIndex()).contains(":")||tokens.get(curIndex()).contains(";")){
            return false;
        }else if( tokens.get(curIndex()).contains(">")||tokens.get(curIndex()).contains("<")){
            return false;
        }else if( tokens.get(curIndex()).contains("=")||tokens.get(curIndex()).contains("?")) {
            return false;
        }else if( tokens.get(curIndex()).contains("@")||tokens.get(curIndex()).contains("[")) {
            return false;
        } else if( tokens.get(curIndex()).contains("\\")||tokens.get(curIndex()).contains("]")) {
            return false;
        } else if( tokens.get(curIndex()).contains("^")||tokens.get(curIndex()).contains("_")) {
            return false;
        }else if( tokens.get(curIndex()).contains("'")||tokens.get(curIndex()).contains("{")) {
            return false;
        } else if( tokens.get(curIndex()).contains("}")||tokens.get(curIndex()).contains("~")) {
            return false;
        }
        return true;
    }

    public boolean isTableName(String tableName){
        if (tokens.get(curIndex()).equals(tableName)) {
            return true;
        }
        return false;
    }

    public boolean checkFirstCre(){
        if(!(tokens.get(curIndex()).equals(","))) {
            return false;
        }
        return true;
    }


    //check is attribute list
    public boolean isAttribList(){
        //terminal con
        if(tokens.size()-curIndex()==1){
            return true;
        }
        if(isAttriName()){
            NextToken();
            if(tokens.get(curIndex()).equals(",")||tokens.get(curIndex()).equals(".")){
                NextToken();
                return isAttribList();
            }
        } else if (isAttriName()) {
            return true;
        }
        return false;
    }
    public boolean isAttriName(){
        if(isPlainTest()){
            return true;
        } else if (isTableName()) {
            NextToken();
            if(tokens.get(curIndex()).equals(".")){
                NextToken();
                if(isPlainTest()){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isTableName(){
        if(isPlainTest()){
            return true;
        }
        return false;
    }




    public List<String> sparateQuo(List<String> attList){
        //TEST
        ArrayList<String> tmpList=new ArrayList<>();
        String tmp="^'.*'$";
        Pattern pattern=Pattern.compile(tmp);

        for(String ans:attList){
            Matcher mat=pattern.matcher(ans);
            if(mat.matches()==true){
                String tmp2[]= ans.split("'");
                tmpList.add("'");
                tmpList.add(tmp2[1].trim());
                tmpList.add("'");
            }else {
                tmpList.add(ans);
            }
        }
        return tmpList;
    }

    public void NextToken(){
        this.index++;
    }
    public void previousToken(){
        this.index--;
    }
    public int curIndex(){
        return this.index;
    }
    public  List<String> getTokens(){
        return this.tokens;
    }


}
