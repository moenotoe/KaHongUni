package edu.uob;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class updateCMD extends DBcmd{
    private List<String> tokens;
    private String storageFolderPath;



    public updateCMD(List<String> tokens){
        this.tokens=tokens;
        this.storageFolderPath= Paths.get("databases").toAbsolutePath().toString();

    }

    @Override
    public String query(DBServer s) throws Exception {
        tokens.remove(";");
        //checkException();
        List<String> tokenTmp=new ArrayList<>();
        tokenTmp=tokens;

        String tableName=tokens.get(1).toLowerCase();
        String tablePath=storageFolderPath+ File.separator+s.getCurDb()+ File.separator+tableName+".tab";
        File file=new File(tablePath);
        checkPath(file);


        //sape namelist and condition and check illegal and remove ' '
        List<String> nameList=generateNameList(tokenTmp);
        List<String> conditionList=generateConditionList(tokenTmp);

        //sape namelist and condition
        //singal condition:
        Table table=new Table(file);


        Condition con=new Condition(nameList,conditionList,table);

        //sperate namevallist
        //check att exist

        con.nameValueJudge();

        ExeCom exe=new ExeCom();
        exe.updateCommand(table,con);


        return "update success";
    }

    public List<String> generateConditionList(List<String> tokentmp) throws Exception {
        WhereProcess pro=new WhereProcess();
        Filter fil=new Filter(tokentmp);
        for(int i=0;i<tokentmp.size();i++){
            if(tokentmp.get(i).equals("(")||tokentmp.get(i).equals(")")){
                tokentmp.remove(i);
            }
        }
        //seperate ''
        tokentmp=fil.sparateQuo(tokentmp);
        List<String> newList=tokentmp=tokentmp.subList(tokentmp.indexOf("WHERE")+1,tokentmp.size());
        //check token !
        //seperate many conditions
        checkCondition(newList);
        //remove ''
        pro.removeQuo(newList);
        for(int i=0;i<newList.size();i++){
            if(newList.get(i).equals("")){
                newList.remove(i);
            }
        }
        newList=pro.combineWithMulti(newList);
        return newList;
    }
    public List<String> generateNameList(List<String> tokentmp) throws Exception {
        //seperate ''
        Filter fil=new Filter(tokentmp);
        WhereProcess pro=new WhereProcess();
        tokentmp=fil.sparateQuo(tokentmp);

        //process upper lower!!!
        List<String> newList=tokentmp.subList(tokentmp.indexOf("SET")+1,tokentmp.indexOf("WHERE"));
        //check token !
        checkNameValueList(newList);
        //remove ''
        pro.removeQuo(newList);
        for(int i=0;i<newList.size();i++){
            if(newList.get(i).equals("")){
                newList.remove(i);
            }
        }
        return newList;
    }
    public  void checkNameValueList(List<String> list) throws Exception {
        Filter fil=new Filter(list);
        if(!fil.isNameValueList()){
            throw new Exception("wrong for nameValueList");
        }


    }
    public void checkCondition(List<String> list) throws Exception {

        Filter fil=new Filter(list);

        if(!fil.isGeneralCondition()){
            throw new Exception("wrong for condition");
        }
    }
    public void checkException() throws Exception {
        Filter fil=new Filter(this.tokens);
        tokens=fil.sparateQuo(tokens);
        if(!tokens.get(2).equalsIgnoreCase("SET")){
            throw new Exception("not an update");
        }

    }
    public void checkPath(File path) throws Exception {
        if(!path.exists()){
            throw new Exception("table not exist");
        }
    }
}
