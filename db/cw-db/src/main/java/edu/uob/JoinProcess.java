package edu.uob;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;

public class JoinProcess {
    private Table table1;
    private Table table2;
    private String joinAttr1;
    private String joinAttr2;



    public JoinProcess(Table table1, Table table2,String attr1, String attr2,String tableName1,String tableName2) throws IOException {
        this.table1=table1;
        this.table2=table2;
        table1.inputToObjWithAttribute(tableName1);
        table2.inputToObjWithAttribute(tableName2);
        this.joinAttr1=attr1;
        this.joinAttr2=attr2;

    }


    public void checkAttExist() throws Exception {
        List<String> attList1=table1.getAttributes();
        List<String> attList2=table2.getAttributes();

        if(!attList1.contains(joinAttr1)||!attList2.contains(joinAttr2)){
            throw new Exception("not this attribute");
        }
    }



    public List<LinkedHashMap<String,String>> generateTable(){
        List<LinkedHashMap<String,String>> tableMap=new ArrayList<>();
        //改名字

        for(int i=0;i<table1.getRow().size();i++) {

            Set<Map.Entry<String, String>> entrySet1 = table1.getRow().get(i).entrySet();
            @SuppressWarnings("unchecked")
            Iterator<Map.Entry<String, String>> iterator1 = entrySet1.iterator();
            while (iterator1.hasNext()) {
                Object obj = iterator1.next();
                @SuppressWarnings("unchecked")
                Map.Entry<String, String> entry = (Map.Entry<String, String>) obj;
                String table1Key=entry.getKey().toString();
                String table1Val=entry.getValue().toString();
                for(int j=0;j<table2.getRow().size();j++) {
                    Set<Map.Entry<String, String>> entrySet2 = table2.getRow().get(j).entrySet();
                    Iterator<Map.Entry<String, String>> iterator2 = entrySet2.iterator();
                    while (iterator2.hasNext()) {
                        Object obj2 = iterator2.next();
                        @SuppressWarnings("unchecked")
                        Map.Entry<String, String> entry2 = (Map.Entry<String, String>) obj2;
                        if(table1Key.toString().contains(joinAttr1)&&entry2.getKey().contains(joinAttr2)){
                            if(table1Val.equals(entry2.getValue())){
                                //put int to new table
                                LinkedHashMap<String,String> combinMap=new LinkedHashMap<>();
                                combinMap.putAll(table1.getRow().get(i));
                                combinMap.putAll(table2.getRow().get(j));
                                tableMap.add(combinMap);
                            }
                        }
                    }
                }
            }
        }
        return  tableMap;
    }

    public List<LinkedHashMap<String,String>> eleRepeat(){
        List<LinkedHashMap<String,String>> tableMap=generateTable();
        //id in table2 is delete auto
        for(int i=0;i<tableMap.size();i++){
            Set<String> keySet = tableMap.get(i).keySet();
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                String str=iterator.next();
                if(str.contains(joinAttr1)||str.contains(joinAttr2)){
                    iterator.remove();
                }
            }
        }
        int id=0;
        List<LinkedHashMap<String,String>> idResult=new ArrayList<>();
        for(int i=0;i< tableMap.size();i++){
            LinkedHashMap<String,String> tmp=new LinkedHashMap<>();
            tmp.put("id",String.valueOf(++id));
            tmp.putAll(tableMap.get(i));
            idResult.add(tmp);
        }
        return idResult;
    }
    public List<String> generateColResult(){
        List<String> result=new ArrayList<>();
        result.add("id");
        for(int i=0;i<table1.getAttributes().size();i++){
            if(table1.getAttributes().get(i).contains("id")||table1.getAttributes().get(i).contains(joinAttr1)){
                table1.getAttributes().remove(i);
            }
        }
        result.addAll(table1.getAttributes());
        for(int i=0;i<table2.getAttributes().size();i++){
            if(table2.getAttributes().get(i).contains("id")||table2.getAttributes().get(i).contains(joinAttr2)){
                table2.getAttributes().remove(i);
            }
        }
        result.addAll(table2.getAttributes());
        return result;
    }
    public String outputAns(){
        StringBuilder buildAns=new StringBuilder();
        List<LinkedHashMap<String,String>> tableMap=eleRepeat();
        List<String> resultCol=generateColResult();
        buildAns.append("\n");
        buildAns.append(table1.attriToString(resultCol));
        for(int i=0;i<tableMap.size();i++){
            buildAns.append(table1.specificRowtoStr(tableMap.get(i),resultCol));
        }

        return buildAns.toString();

    }

}
