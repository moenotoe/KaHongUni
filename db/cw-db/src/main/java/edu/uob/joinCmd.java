package edu.uob;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class joinCmd extends DBcmd{
    private List<String> tokens;
    final private String storageFolderPath;

    public joinCmd(List<String> tokens){
        this.tokens=tokens;
        this.storageFolderPath= Paths.get("databases").toAbsolutePath().toString();
    }
    @Override
    String query(DBServer s) throws Exception {
        //every exception//////////////////
        String tableName1=tokens.get(1);
        String tableName2=tokens.get(3);
        //create table
        String tableName1Path=storageFolderPath+ File.separator+s.getCurDb()+ File.separator+tableName1+".tab";
        String tableName2Path=storageFolderPath+ File.separator+s.getCurDb()+ File.separator+tableName2+".tab";
        File file1=new File(tableName1Path);
        File file2=new File(tableName2Path);

        Table table1=new Table(file1);
        Table table2=new Table(file2);
        //sperate attribute Name
        List<String> tmp=new ArrayList<>();
        tmp=tokens;

        for(int i=0;i< tmp.size();i++){
            if(tmp.get(i).equalsIgnoreCase("JOIN")||tmp.get(i).equalsIgnoreCase("AND")||tmp.get(i).equalsIgnoreCase("ON")){
                tmp.set(tmp.indexOf(tmp.get(i)),tmp.get(i).toLowerCase());
            }
        }

        //remove first 'and'
        tmp.remove("and");

        //get attri1 and 2
        String att1="";
        String att2="";
        for(int i=0;i<tmp.size();i++){
            if(tmp.get(i).equals("on")){
                att1=tmp.get(i+1);
            } else if (tmp.get(i).equals("and")) {
                att2=tmp.get(i+1);

            }
        }
        JoinProcess pro=new JoinProcess(table1,table2,att1,att2,tableName1,tableName2);
        String ans=pro.outputAns();
        return ans;
    }

}
