package edu.uob;


import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Table{
    private ArrayList<String>attribute;
    private ArrayList<LinkedHashMap<String,String>> Row;

    private int rowId;
    private File filename;


    public Table(File filename) {
        this.filename= filename;
        this.attribute = new ArrayList<String>();
        this.Row=new ArrayList<LinkedHashMap<String,String>>();
        this.rowId = 0;
    }


    public void addRow(ArrayList<String> subStr){
        LinkedHashMap<String,String> tmp=new LinkedHashMap<>();
        tmp.put(this.attribute.get(0), String.valueOf(++rowId));
        for(int i=0;i<subStr.size();i++){
            tmp.put(this.attribute.get(i+1),subStr.get(i));
        }
        this.Row.add(tmp);
    }
    public String inputFromFile(File tableFile) throws IOException {
        //let the file element to become a string
        StringBuilder tmp=new StringBuilder();
        FileReader fileReader=new FileReader(tableFile);
        BufferedReader reader=new BufferedReader(fileReader);
        String line;
        while((line=reader.readLine())!=null){
            tmp.append(line);
            tmp.append("\n");
        }
        fileReader.close();
        return tmp.toString();
    }

    public void outputToFile() throws IOException {
        try {
            FileWriter fileWriter = new FileWriter(this.filename);
            //output attribute
            for(String attri : this.attribute){
                fileWriter.write(attri);
                fileWriter.write("\t");
            }
            fileWriter.write("\n");

            //process value
            for(int i=0;i<this.Row.size();i++){
                for(int j=0;j<this.Row.get(i).size();j++){
                    fileWriter.write(this.Row.get(i).get(this.attribute.get(j)));
                    if(this.attribute.size()>j+1){
                        fileWriter.write("\t");
                    }
                }
                fileWriter.write("\n");
            }

            fileWriter.close();
        }catch(IOException e){
            throw new IOException("output failed");
        }
    }

    public String rowToString(LinkedHashMap<String,String> row){
        StringBuilder rowStr=new StringBuilder();
        for(int i=0;i<row.size();i++){  //1-->this.att.get(0)==id
            //put into the string
            rowStr.append(row.get(this.attribute.get(i)));
            if(row.size()>1+i){
                rowStr.append("\t");
            }
        }
        rowStr.append("\n");
        return rowStr.toString();
    }

    public String specificRowtoStr(LinkedHashMap<String,String> row,List<String> resultCol){
        StringBuilder rowStr=new StringBuilder();
        //get row key directory
        Set<Map.Entry<String, String>> entry=row.entrySet();
        Iterator<Map.Entry<String,String>> it=entry.iterator();
        while(it.hasNext())
        {
            Map.Entry<String,String> e=it.next();
            String key=e.getKey();
            rowStr.append(row.get(key));
            rowStr.append("\t");
        }

        rowStr.append("\n");
        return rowStr.toString();
    }

    public String attriToString(List<String> ans){
        StringBuilder attriString = new StringBuilder();
        for(String attri : ans) {
            attriString.append(attri);
            attriString.append("\t");
        }
        attriString.append("\n");
        return attriString.toString();
    }
    public void oriStringToAttri(String oriString){
        String[] tmp=oriString.split("\n");
        String Attri=tmp[0];
        String[] diviAttris=Attri.split("\t");
        this.attribute.addAll(List.of(diviAttris));
        return;
    }


    public void setId(){
        this.attribute.add("id");
    }
    public void setAttribute(ArrayList<String> subStr){
        this.attribute.addAll(subStr);
    }
    public void appendSingalAttri(String attriValue){
        this.attribute.add(attriValue);
    }
    public void deleteAttri(String attriValue) throws Exception {
        if(!this.attribute.contains(attriValue)||attriValue.equals("id")){
            throw new Exception("id can not delete");
        }
        for(int i=0;i<getAttributes().size();i++){
            if(getAttributes().get(i).equals(attriValue)){
                getAttributes().remove(attriValue);
            }
        }
        //as well as corrporate row
        for (int i = 0; i < getRow().size(); i++) {
            Set<String> keySet=getRow().get(i).keySet();
            Iterator<String> iterator=keySet.iterator();
            while (iterator.hasNext()){
                String str=iterator.next();
                if(str.equals(attriValue)){
                    iterator.remove();
                }
            }
        }
    }

    public  void deleteRow(List<LinkedHashMap<String,String>> resultRow) throws IOException {

        for(int i=0;i<this.Row.size();i++){
            for(int j=0;j<resultRow.size();j++){
                if(this.Row.get(i).equals(resultRow.get(j))){
                    this.Row.remove(i);
                }
            }
        }
    }
    public ArrayList<String> getAttributes(){
        return this.attribute;
    }

    //use only have attribute
    public void oriStringToRow(String oriString){
        String[] tmp=oriString.split("\n");
        for(int i=1;i< tmp.length;i++){
            LinkedHashMap<String,String> row=new LinkedHashMap<>();
            ArrayList<String> dynamRow=new ArrayList<>();
            String oneRow=tmp[i];
            String[] diviRow=oneRow.split("\t");
            for(int j=0;j<diviRow.length;j++) {
                dynamRow.add(diviRow[j]);
                row.put(this.attribute.get(j), dynamRow.get(j));
            }
            this.Row.add(row);
            this.rowId++;
        }
    }
    public ArrayList<LinkedHashMap<String,String>> getRow(){
        return this.Row;
    }

    //use when i need to get file to become objects in table
    public void inputToObj() throws IOException {
        String totalData=inputFromFile(this.filename);
        oriStringToAttri(totalData);
        oriStringToRow(totalData);
    }


    public void inputToObjWithAttribute(String tableName) throws IOException {
        String totalData=inputFromFile(this.filename);

        oriStringToAttriChangeAtt(totalData,tableName);
        oriStringToRowChangeRow(totalData,tableName);
    }
    public void oriStringToAttriChangeAtt(String oriString,String tableName){
        String[] tmp=oriString.split("\n");
        String Attri=tmp[0];
        String[] diviAttris=Attri.split("\t");
        for (int i = 0; i < diviAttris.length; i++) {
            String modifiedAttri = tableName+"."+diviAttris[i]; // modifyString為修改字符串的方法
            diviAttris[i] = modifiedAttri;
        }
        String modifiedAttriStr = String.join("\t", diviAttris);
        this.attribute.addAll(List.of(diviAttris));
        return;
    }
    public void oriStringToRowChangeRow(String oriString,String tableName){
        String[] tmp=oriString.split("\n");
        for(int i=1;i< tmp.length;i++){
            LinkedHashMap<String,String> row=new LinkedHashMap<>();
            ArrayList<String> dynamRow=new ArrayList<>();
            String oneRow=tmp[i];
            String[] diviRow=oneRow.split("\t");
            for(int j=0;j<diviRow.length;j++) {
                dynamRow.add(diviRow[j]);
                row.put(this.attribute.get(j), dynamRow.get(j));
            }
            this.Row.add(row);
            this.rowId++;
        }
    }


    public  void setRow(ArrayList<LinkedHashMap<String,String>> tmp){
        this.Row=tmp;
    }
    public void setCol(ArrayList<String> tmp){
        this.attribute=tmp;
    }

    public void removeRow(){
        for(int i=0;i<this.Row.size();i++){
            this.Row.remove(i);

        }
    }
    public void removeCol(){
        for(int i=0;i<this.attribute.size();i++){
            this.attribute.remove(i);

        }
    }





}


