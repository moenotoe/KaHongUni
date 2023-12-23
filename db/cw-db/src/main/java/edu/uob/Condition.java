package edu.uob;


import java.io.IOException;
import java.util.*;

public class Condition {
    private List<String> colCon;

    //rowCon can be null
    private List<String> rowCon;

    private String comparator;

    private Table tab;
    private List<String> choseAttri;
    private List<String> choseRow;


    public Condition(List<String> colCon,List<String> rowCon,Table tab) throws IOException {
        this.colCon=colCon;
        this.rowCon=rowCon;
        this.tab=tab;
        this.choseRow=new ArrayList<>();
        tab.inputToObj();
    }
    public Condition(){

    }
    public Condition(List<String> colCon){
        this.colCon=colCon;
    }


    public void nameValueJudge() throws Exception {
        //loop attri and hash map key, if not contain,false
        List<LinkedHashMap<String,String>> tmp=nameValueListProcess();
        //iterator get key-->key not contains in the List

        if(tmp.size()==1){
            Set<String> keySet = tmp.get(0).keySet();

            Iterator<String> iterator = keySet.iterator();

            while (iterator.hasNext()) {

                String str = iterator.next();

                if (!tab.getAttributes().contains(str)) {
                    throw new Exception("table not have this attribute");
                }
            }
        }else {
            for (int i = 0; i < tmp.size(); i++) {
                Set<String> keySet = tmp.get(i).keySet();
                Iterator<String> iterator = keySet.iterator();
                while (iterator.hasNext()) {
                    String str = iterator.next();
                    if (!tab.getAttributes().contains(str)) {
                        throw new Exception("table not have this attribute");
                    }
                }
            }
        }

    }

    public List<LinkedHashMap<String,String>> nameValueListProcess(){
        //sepe list

        int start=0;
        List<LinkedHashMap<String,String>> tmp=new ArrayList<>();

        if(colCon.size()==3){
            LinkedHashMap<String,String> map=new LinkedHashMap<>();
            map.put(colCon.get(0).toString(),colCon.get(2).toString());
            tmp.add(map);
        }else {
            for (int i = 0; i < colCon.size(); i++) {
                LinkedHashMap<String, String> map = new LinkedHashMap<>();
                if (colCon.get(i).equals(",")) {

                    map.put(colCon.get(i - 3), colCon.get(i - 1));
                    tmp.add(map);
                    start = i;
                }
            }
            //put last one
            LinkedHashMap<String,String> map=new LinkedHashMap<>();
            map.put(colCon.get(start+1), colCon.get(colCon.size()-1));
            tmp.add(map);
        }

        return tmp;
    }


    public List<LinkedHashMap<String,String>> generateRowResult()  {
        separateRowCon();
        return getRowConAns(this.rowCon);
    }
    public List<String> generateColResult() {
        separateAttr();
        return filterCol();
    }
    public List<String> colResultWithMulti(List<List<String>>andCon,int i){
        List<String> tmp=new ArrayList<>();
        if(i==andCon.size()-1){
            separateAttr();
            return filterCol();
        }else {
            return tmp;
        }
    }
    public void separateAttr() {

        if (this.colCon.contains("*")) {
            //all attri
            this.choseAttri=tab.getAttributes();
        }else{
            this.choseAttri=this.colCon;
        }
    }
    //break the row and setting the row bool or com
     public void separateRowCon(){
        if(this.rowCon.contains("==")){
            this.comparator="==";
        }else if(this.rowCon.contains(">=")){
            this.comparator=">=";
        } else if (this.rowCon.contains("!=")) {
            this.comparator="!=";
        } else if (this.rowCon.contains("<=")) {
            this.comparator="<=";
        } else if (this.rowCon.contains(">")) {
            this.comparator=">";
        } else if (this.rowCon.contains("<")) {
            this.comparator="<";
        } else if (this.rowCon.contains("LIKE")) {
            this.comparator="LIKE";
        }else if (this.rowCon.contains("=")){
            this.comparator="=";
        }

         int rowAttriIndex=this.rowCon.indexOf(this.comparator)-1;
        int rowConditValInd=this.rowCon.indexOf(this.comparator)+1;
        if(this.rowCon.contains("flag")){
            this.choseRow.addAll(this.rowCon);
        }else {
            this.choseRow.add(this.rowCon.get(rowAttriIndex));
            this.choseRow.add(this.comparator);
            this.choseRow.add(this.rowCon.get(rowConditValInd));
            //OK:System.out.println("ans"+this.choseRow);
        }
    }

    public List<LinkedHashMap<String,String>> getRowConAns(List<String> singalRowCon){
        List<LinkedHashMap<String,String>> rowAns=new ArrayList<>();
        //regular expression
        WhereProcess pro=new WhereProcess();
        if(singalRowCon.size()!=1) {
            if(this.comparator.equals("!=")&&!pro.isDigitCombine(singalRowCon.get(2))){
                notEqu(singalRowCon,rowAns);
            }
            if (this.comparator.equals("LIKE")) {
                likeCon(singalRowCon,rowAns);
            }
            if ((!pro.isDigitCombine(singalRowCon.get(2)) && !this.comparator.equals("=="))) {
                return rowAns;
            }
        }
        //all row
        if(singalRowCon.contains("flag")||singalRowCon.contains("*")){
            rowAns=tab.getRow();
        }else {
            //condition
            if(this.comparator.equals("==")) {
                equalCon(singalRowCon,rowAns);
            } else if (this.comparator.equals(">")) {
                greater(singalRowCon,rowAns);
            } else if (this.comparator.contains(">=")){
                greaterEqu(singalRowCon,rowAns);
            } else if (this.comparator.equals("<")) {
                lessThan(singalRowCon,rowAns);
            } else if (this.comparator.equals("<=")) {
                lessEqu(singalRowCon,rowAns);
            }else if (this.comparator.equals("!=")) {
                notEqu(singalRowCon,rowAns);
            }

        }
        return rowAns;
    }
    public void likeCon(List<String> singalRowCon,List<LinkedHashMap<String,String>> rowAns){
        //for loop, get value , contain singalRowCon.get(0);
        for(int i=0;i<tab.getRow().size();i++) {
            Set<Map.Entry<String, String>> entrySet = tab.getRow().get(i).entrySet();
            Iterator<Map.Entry<String, String>> iterator2 = entrySet.iterator();
            while (iterator2.hasNext()) {
                Object obj = iterator2.next();
                @SuppressWarnings("unchecked")
                Map.Entry<String, String> entry = (Map.Entry<String, String>) obj;
                if(entry.getKey().equals(singalRowCon.get(0))&&entry.getValue().contains(singalRowCon.get(2)))
                    rowAns.add(tab.getRow().get(i));
            }
        }
    }
    public void notEqu(List<String> singalRowCon,List<LinkedHashMap<String,String>> rowAns){
        WhereProcess pro=new WhereProcess();
        String targetKey=singalRowCon.get(0);
        String targetVal=singalRowCon.get(2);
        if(!pro.isDigitCombine(singalRowCon.get(2))){
            for(int i=0;i<tab.getRow().size();i++){
                Set<Map.Entry<String, String>> entrySet = tab.getRow().get(i).entrySet();
                Iterator<Map.Entry<String, String>> iterator2 = entrySet.iterator();
                while(iterator2.hasNext())
                {
                    Object obj=iterator2.next();
                    @SuppressWarnings("unchecked")
                    Map.Entry<String, String> entry=(Map.Entry<String, String>) obj;
                    if(entry.getKey().equals(targetKey)){
                        if(!entry.getValue().equals(targetVal)){
                            rowAns.add(tab.getRow().get(i));
                        }
                    }

                }
            }
        } else{
            for (int i = 0; i < tab.getRow().size(); i++) {
                if (singalRowCon.get(2).contains(".")) {
                    Double oriVal = Double.parseDouble(tab.getRow().get(i).get(singalRowCon.get(0)));
                    Double tarVal = Double.parseDouble(singalRowCon.get(2));
                    if (oriVal != tarVal) {
                        rowAns.add(tab.getRow().get(i));
                    }
                } else {
                    int oriVal = Integer.parseInt(tab.getRow().get(i).get(singalRowCon.get(0)));
                    int tarVal = Integer.parseInt(singalRowCon.get(2));
                    if (oriVal != tarVal) {
                        rowAns.add(tab.getRow().get(i));
                    }
                }
            }
        }
    }

    public void lessEqu(List<String> singalRowCon,List<LinkedHashMap<String,String>> rowAns){
        for(int i=0;i<tab.getRow().size();i++){
            if(singalRowCon.get(2).contains(".")){
                Double oriVal=Double.parseDouble(tab.getRow().get(i).get(singalRowCon.get(0)));
                Double tarVal=Double.parseDouble(singalRowCon.get(2));
                if(oriVal<=tarVal){
                    rowAns.add(tab.getRow().get(i));
                }
            }else {
                int oriVal=Integer.parseInt(tab.getRow().get(i).get(singalRowCon.get(0)));
                int tarVal=Integer.parseInt(singalRowCon.get(2));
                if(oriVal<=tarVal){
                    rowAns.add(tab.getRow().get(i));
                }
            }
        }
    }
    public void lessThan(List<String> singalRowCon,List<LinkedHashMap<String,String>> rowAns){
        for(int i=0;i<tab.getRow().size();i++){
            if(singalRowCon.get(2).contains(".")){
                Double oriVal=Double.parseDouble(tab.getRow().get(i).get(singalRowCon.get(0)));
                Double tarVal=Double.parseDouble(singalRowCon.get(2));
                if(oriVal<tarVal){
                    rowAns.add(tab.getRow().get(i));
                }
            }else {
                int oriVal=Integer.parseInt(tab.getRow().get(i).get(singalRowCon.get(0)));
                int tarVal=Integer.parseInt(singalRowCon.get(2));
                if(oriVal<tarVal){
                    rowAns.add(tab.getRow().get(i));
                }
            }
        }
    }



    public void greaterEqu(List<String> singalRowCon,List<LinkedHashMap<String,String>> rowAns){

        for(int i=0;i<tab.getRow().size();i++){
            if(singalRowCon.get(2).contains(".")){
                Double oriVal=Double.parseDouble(tab.getRow().get(i).get(singalRowCon.get(0)));
                Double tarVal=Double.parseDouble(singalRowCon.get(2));
                if(oriVal>=tarVal){
                    rowAns.add(tab.getRow().get(i));
                }
            }else {
                int oriVal=Integer.parseInt(tab.getRow().get(i).get(singalRowCon.get(0)));
                int tarVal=Integer.parseInt(singalRowCon.get(2));
                if(oriVal>=tarVal){
                    rowAns.add(tab.getRow().get(i));
                }
            }
        }
    }

    public void greater( List<String> singalRowCon,List<LinkedHashMap<String,String>> rowAns){
        for(int i=0;i<tab.getRow().size();i++){
            if(singalRowCon.get(2).contains(".")){
                Double oriVal=Double.parseDouble(tab.getRow().get(i).get(singalRowCon.get(0)));
                Double tarVal=Double.parseDouble(singalRowCon.get(2));
                if(oriVal>tarVal){
                    rowAns.add(tab.getRow().get(i));
                }
            }else {
                int oriVal=Integer.parseInt(tab.getRow().get(i).get(singalRowCon.get(0)));
                int tarVal=Integer.parseInt(singalRowCon.get(2));
                if(oriVal>tarVal){
                    rowAns.add(tab.getRow().get(i));
                }
            }
        }
    }

    public void equalCon( List<String> singalRowCon,List<LinkedHashMap<String,String>> rowAns){
        for (LinkedHashMap<String,String> tmp : tab.getRow()) {
            if (tmp.get(singalRowCon.get(0)).equals(singalRowCon.get(2))) {
                rowAns.add(tmp);
            }
        }

    }
    public List<String> filterCol(){
        List<String> result=new ArrayList<>();
        for(int i=0;i<this.choseAttri.size();i++){
            for(String tmp:tab.getAttributes()){
                if(this.choseAttri.get(i).equalsIgnoreCase(tmp)){
                    result.add(tmp);
                }
            }
        }
        return result;
    }

    public List<String> getColCon(){
        return this.colCon;
    }
        

    public boolean topCheck(List<List<String>> rowCon,LinkedHashMap<String,String> map){
        Deque<String> stack=new ArrayDeque<>();
        for(int i=0;i<rowCon.size();i++){
            List<String> temp=rowCon.get(i);
            if(temp.size()==1){
                stack.offerFirst(temp.get(0));
            }else{
                boolean check = check(temp, map);
                stack.offerFirst(String.valueOf(check));
            }
        }
        while(stack.size()!=1){
            String s1=stack.pollLast();
            String s2=stack.pollLast();
            String s3=stack.pollLast();
            if(s2.equalsIgnoreCase("AND")){
                boolean temp=Boolean.valueOf(s1)&&Boolean.valueOf(s3);
                stack.offerLast(String.valueOf(temp));
            }else{
                boolean temp=Boolean.valueOf(s1)||Boolean.valueOf(s3);
                stack.offerLast(String.valueOf(temp));
            }
        }
        return Boolean.valueOf(stack.pollFirst());

    }
    public boolean check(List<String> tmp, LinkedHashMap<String,String> values){
        WhereProcess pro=new WhereProcess();
        String comparator=tmp.get(1);
        String left="";
        String right=tmp.get(2);
        for(Map.Entry<String,String> entry:values.entrySet()){
            if(entry.getKey().equalsIgnoreCase(tmp.get(0))){
                left=entry.getValue();
                break;
            }
        }

        if(left.equalsIgnoreCase("null")^right.equalsIgnoreCase("null")){
            return false;
        } else if ((pro.isDigitCombine(left)||pro.isFloat(left) )^ (pro.isDigitCombine(right)||pro.isFloat(right))) {
            return false;
        } else if (pro.isString(left) ^ pro.isString(right)) {
            return false;
        } else if (pro.isBool(left) ^ pro.isBool(right)) {
            return false;
        }


        if(comparator.equals("==")||comparator.equals("!=")){
            return checkEqualOrNot(left,comparator,right);
        }else if(comparator.equals(">=")||comparator.equals("<=") ||comparator.equals(">")||comparator.equals("<")){
            return checkEqualOrNot(left,comparator,right);
        }else if(comparator.equalsIgnoreCase("LIKE")){
            return checkEqualOrNot(left,comparator,right);
        }
        return true;
    }

    public boolean checkEqualOrNot(String left,String comparator,String right) {
        WhereProcess pro=new WhereProcess();
        if(comparator.equals("==")) {
            if(left.equalsIgnoreCase("null")) {
                return true;
            }else if(pro.isString(left)){
                return left.equals(right);
            }else if(left.equalsIgnoreCase("TRUE")||left.equalsIgnoreCase("FALSE")) {
                return left.equalsIgnoreCase(right);
            }else{
                float f1=Float.parseFloat(left);
                float f2=Float.parseFloat(right);
                return f1==f2;
            }
        }else {
            if(pro.isString(left)){
                return !left.equals(right);
            }else if(left.equalsIgnoreCase("TRUE")) {
                return !left.equalsIgnoreCase(right);
            }else{
                float f1=Float.parseFloat(left);
                float f2=Float.parseFloat(right);
                return f1!=f2;
            }
        }

    }
    public List<String> getColcon(){
        return this.colCon;
    }

}
