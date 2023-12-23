package edu.uob;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MultiAns {

    private List<LinkedHashMap<String,String>> rowAns;
    private List<String> colCondition;
    private Table table;

    public MultiAns(List<LinkedHashMap<String,String>> rowAns,List<String> filtCol,Table table) throws IOException {
        this.rowAns=rowAns;
        this.colCondition=filtCol;
        this.table=table;
        table.inputToObj();
    }

    public List<String> filtCol(){
        List<String> col=new ArrayList<>();
        if(colCondition.contains("*")){
            col=table.getAttributes();
        }else {
            for(int i=0;i<this.colCondition.size();i++){
                for(String tmp:table.getAttributes()){
                    if(this.colCondition.get(i).equalsIgnoreCase(tmp)){
                        col.add(tmp);
                    }
                }
            }
        }
        return col;
    }

    public String result(){
        StringBuilder selectAns=new StringBuilder();
        selectAns.append("\n");
        List<String> col=filtCol();
        String att=table.attriToString(col);
        List<LinkedHashMap<String,String>> tmp= rowAns;
        selectAns.append(att);
        for(int i=0;i<this.rowAns.size();i++){
            selectAns.append(table.specificRowtoStr(rowAns.get(i),col));
        }
        return selectAns.toString();
    }
}
