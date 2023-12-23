package edu.uob;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

public class dropCMD extends DBcmd{
    private List<String> tokens;
    private String storageFolderPath;
    private String extension=".tab";
    public dropCMD(List<String> tokens){
        this.tokens=tokens;
        this.storageFolderPath=Paths.get("databases").toAbsolutePath().toString();
    }



    @Override
    String query(DBServer s) throws Exception {
        String comType1=tokens.get(1);
        String name=tokens.get(2).toLowerCase();
        if(comType1.equalsIgnoreCase("DATABASE")){
            dropDb(name);
            return "drop database success";
        }else if(comType1.equalsIgnoreCase("TABLE")){
            dropTable(s,name);
            return "drop table success";
        }

        return null;
    }

    public void dropTable(DBServer s,String name) throws Exception {
        String tabPath=Paths.get("databases").toAbsolutePath().toString()+File.separator+s.getCurDb().toString()+File.separator+name+extension;
        File tabFile=new File(tabPath);
        String nameWithSuffix=name+extension;
        checkTab(nameWithSuffix,tabFile);

        //delete table
        ExeCom delTab=new ExeCom();
        delTab.deleteTab(tabFile);
    }

    public void checkTab(String name, File tabFile) throws Exception {
        if(!tabFile.exists()){
            throw new Exception("table"+name+"not exist");
        }
    }
    public void dropDb(String name) throws Exception {

        String dbPath=storageFolderPath+ File.separator+name;
        File dbFile=new File(dbPath);
        checkDb(name,dbFile);

        //delete db
        ExeCom delDb=new ExeCom();
        delDb.deleteDb(dbFile);
    }
    public void checkDb(String name,File dbFile) throws Exception {
        if(!dbFile.exists()){
            throw new Exception("database"+name+"not exist");
        }
    }
}
