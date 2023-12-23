package edu.uob;

import java.io.*;

import java.util.*;


public class ExeCom {
    public void updateCommand(Table tab, Condition con)throws IOException{


        List<LinkedHashMap<String,String>> whereCon=con.generateRowResult();
        List<LinkedHashMap<String,String>> updateAttri=con.nameValueListProcess();
        //ok, update and instead into original table file
        for(int i=0;i<updateAttri.size();i++) {
            Set<Map.Entry<String, String>> entrySet1 = updateAttri.get(i).entrySet();
            Iterator<Map.Entry<String, String>> iterator1 = entrySet1.iterator();
            while (iterator1.hasNext()) {
                Object obj = iterator1.next();
                @SuppressWarnings("unchecked")
                Map.Entry<String, String> entry = (Map.Entry<String, String>) obj;
                String updateKey=entry.getKey().toString();
                String updateVal=entry.getValue().toString();
                for(int j=0;j<whereCon.size();j++) {
                    Set<Map.Entry<String, String>> entrySet2 = whereCon.get(j).entrySet();
                    Iterator<Map.Entry<String, String>> iterator2 = entrySet2.iterator();
                    while (iterator2.hasNext()) {
                        Object obj2 = iterator2.next();
                        @SuppressWarnings("unchecked")
                        Map.Entry<String, String> entry2 = (Map.Entry<String, String>) obj2;
                        if(entry2.getKey().equals(updateKey)){
                            entry2.setValue(updateVal);
                        }
                    }
                }
            }
        }

        updateToTable(whereCon,tab);

    }

    public void updateToTable(List<LinkedHashMap<String,String>> whereCon, Table tab) throws IOException {
        //update to original tab-->use key
        String updateVal="";
        String updateKey="";
            Set<Map.Entry<String, String>> entrySet1 = whereCon.get(0).entrySet();
            @SuppressWarnings("unchecked")
            Iterator<Map.Entry<String, String>> iterator1 = entrySet1.iterator();
            while (iterator1.hasNext()) {
                Object obj = iterator1.next();
                @SuppressWarnings("unchecked")
                Map.Entry<String, String> entry = (Map.Entry<String, String>) obj;

                if(entry.getKey().equals("id")){
                    updateKey=entry.getKey();
                    updateVal=entry.getValue();
                }
                for(int j=0;j<tab.getRow().size();j++) {
                    Set<Map.Entry<String, String>> entrySet2 = tab.getRow().get(j).entrySet();
                    Iterator<Map.Entry<String, String>> iterator2 = entrySet2.iterator();
                    while (iterator2.hasNext()) {
                        Object obj2 = iterator2.next();
                        @SuppressWarnings("unchecked")
                        Map.Entry<String, String> entry2 = (Map.Entry<String, String>) obj2;
                        if(entry2.getKey().equals(updateKey)){
                            if(entry2.getValue().equals(updateVal)) {
                                tab.getRow().set(j, whereCon.get(0));
                            }
                        }
                    }
                }
            }

        tab.outputToFile();
    }

    public String deleteCommand(Table tab,Condition con) throws IOException {
        List<LinkedHashMap<String,String>> resultRow=con.generateRowResult();
        tab.deleteRow(resultRow);

        //output to file
        tab.outputToFile();
        return "delete success";
    }


    public String selectCommand(Table tab,Condition con) throws IOException {

        StringBuilder selectAns=new StringBuilder();
        selectAns.append("\n");
        List<LinkedHashMap<String,String>> resultRow=con.generateRowResult();
        List<String> resultCol=con.generateColResult();
        String att=tab.attriToString(resultCol);
        selectAns.append(att);

        if(!(con.getColCon().contains("*"))) {
            //combine result have select
            List<LinkedHashMap<String,String>> tmpMapList = new ArrayList<>();
            for (int i = 0; i < resultRow.size(); i++) {
                LinkedHashMap<String,String> tmpMap = new LinkedHashMap<>();
                for (int j = 0; j < resultCol.size(); j++) {
                    Set<Map.Entry<String, String>> entrySet = (resultRow.get(i)).entrySet();
                    @SuppressWarnings("unchecked")
                    Iterator<Map.Entry<String, String>> iterator2 = entrySet.iterator();
                    while (iterator2.hasNext()) {
                        Object obj = iterator2.next();
                        @SuppressWarnings("unchecked")
                        Map.Entry<String, String> entry = (Map.Entry<String, String>) obj;
                        if (resultCol.get(j).equalsIgnoreCase(entry.getKey().toString())) {
                            tmpMap.put(entry.getKey().toString(), entry.getValue().toString());
                        }
                    }
                }
                tmpMapList.add(tmpMap);
            }
            for(int i=0;i<tmpMapList.size();i++){
                selectAns.append(tab.specificRowtoStr(tmpMapList.get(i),resultCol));
            }
        }else{// for *
            for (int i = 0; i < resultRow.size(); i++) {
                selectAns.append(tab.rowToString(resultRow.get(i)));
            }
        }
        return selectAns.toString();
    }

    public void AlterCommand(String attriVal, File tabPath, String alterType) throws Exception{
        //read curr attribute to obj first
        Table tab=new Table(tabPath);
        tab.inputToObj();

        //check alter attribute whether exist in the table
        if(alterType.equalsIgnoreCase("ADD")){
            //add attribute
            for(String tmp:tab.getAttributes()){
                if(tmp.equalsIgnoreCase(attriVal)){
                    throw new Exception("repeat attribute");
                }
            }
            tab.appendSingalAttri(attriVal);
            tab.outputToFile();
        } else if (alterType.equalsIgnoreCase("DROP")) {
            //del attribute
            tab.deleteAttri(attriVal);
            tab.outputToFile();
        }
    }
    public void createTable(ArrayList<String> newAtt,String Path) throws IOException {
        Table tab=new Table(new File(Path));
        tab.setId();
        tab.setAttribute(newAtt);
        tab.outputToFile();
    }

    public void deleteDb(File dbFile){
        dbFile.delete();
    }
    public void deleteTab(File tabFile){
        tabFile.delete();
    }

    public void insertCommand(ArrayList<String> rowVal, File tab) throws Exception {
        //get attrib and val to obj first
        Table table=new Table(tab);
        table.inputToObj();
        //id++ and put into the first
        table.addRow(rowVal);
        table.outputToFile();
        return;
    };


}
