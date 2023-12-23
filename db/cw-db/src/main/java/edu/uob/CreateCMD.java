package edu.uob;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

public class CreateCMD extends DBcmd{
    private String storageFolderPath;
    private List<String> tokens;
    final private String extension = ".tab";



    public CreateCMD(List<String> tokens){
        this.tokens=tokens;
    }


    @Override
    String query(DBServer s) throws Exception {
        String commandType2=this.tokens.get(1);

        if(!(commandType2.equalsIgnoreCase("DATABASE")||commandType2.equalsIgnoreCase("TABLE"))){
            throw new Exception("invalid query");
        }

        if (commandType2.equalsIgnoreCase("DATABASE")) {
            //create databases(folder in path);
            return createDb();
        } else if (commandType2.equalsIgnoreCase("TABLE")) {
            //table should base on curr db
            return createTab(s);
        }
        return "";
    }

    public String createTab(DBServer s) throws Exception {
        String tabName = this.tokens.get(2).toLowerCase();
        if(s.getCurDb().equals(null)){
            throw new Exception("Please chose database");
        }

        String Path=Paths.get("databases").toAbsolutePath().toString()+File.separator+s.getCurDb().toString()+File.separator+tabName+extension;

        File tab=new File(Path);
        if(tab.exists()){
            throw new Exception("table already exist");
        }
        //with ()..
        if(tokens.contains("(")){
            creTabWithAttri(this.tokens, s,Path,tabName);
            return "create table success";
        }
        //create table
        if(tabName.equals(";") || tabName.equals(null)){
            throw new Exception("missing table name");
        }
        if(!tab.exists()) {
            tab.createNewFile();
            //insert id in the begin
            Table idInsert=new Table(tab);
            idInsert.setId();
            idInsert.outputToFile();
            return "create table success";
        }
        return "";
    }

    public String createDb() throws Exception {
        String dbName = this.tokens.get(2).toLowerCase();
        if(this.tokens.size()<4){
            throw new Exception("invalid create database");
        }
        storageFolderPath = Paths.get("databases").toAbsolutePath().toString();
        String newPath = storageFolderPath + File.separator + dbName;
        File file = new File(newPath);
        //the exception
        if(file.exists()){
            throw new Exception("DB already exist");
        } else if (!file.exists()) {
            file.mkdir();
            return "build database success";
        }
        return "build database success";
    }
    public void creTabWithAttri(List<String> tokens,DBServer s,String Path,String tabName) throws Exception {

        int startIndex=tokens.indexOf("(");
        int endIndex=tokens.indexOf(")");
        //get the att list
        List<String> attList = tokens.subList(startIndex+1, endIndex);
        Filter checkAttList=new Filter(attList);

        //check subStr is attribute list
        if(checkAttList.isAttribList()==false){
            throw new Exception("attribute error");
        }
        attList.removeIf(coma->coma.equals(","));

        //check weather have repeat attribute(after remove ,)
        if(checkAttList.checkRepeatAtt(attList,tabName)==false){
            throw new Exception("repeat attribute");
        }
        ArrayList<String> newAtt=new ArrayList<>(attList);
        ExeCom exe=new ExeCom();
        exe.createTable(newAtt,Path);
    }
}

