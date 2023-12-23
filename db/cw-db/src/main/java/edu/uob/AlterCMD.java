package edu.uob;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AlterCMD extends DBcmd{

    private List<String> tokens;
    final private String storageFolderPath;



    public AlterCMD(List<String> tokens){
        this.tokens=tokens;
        this.storageFolderPath= Paths.get("databases").toAbsolutePath().toString();
    }


    @Override
    String query(DBServer s) throws Exception {
        String alterType=this.tokens.get(3);
        String tabName=this.tokens.get(2).toLowerCase();
        String attriVal=getAttributeName();
        String tabNamePath=this.storageFolderPath+File.separator+s.getCurDb().toString()+File.separator+tabName+".tab";
        File tabPath=new File(tabNamePath);
        checkInquiry(tabPath);

        //check edge condition
        //1.is attriName
        if(this.tokens.size()>6){
            throw new Exception("invalid alter");
        }
        List<String> tmp=new ArrayList<>();
        tmp.add(attriVal);
        Filter attName=new Filter(tmp);
        if(!attName.isPlainTest()){
            throw new Exception("invalid alter");
        }

        ExeCom exe=new ExeCom();
        exe.AlterCommand(attriVal,tabPath,alterType);
        return "Alter success";
    }

    public String getAttributeName(){
        String AttriName=tokens.get(4);
        return AttriName;
    }

    public void checkInquiry(File tabPath) throws Exception {
        String tok1=this.tokens.get(1);
        String tok3=this.tokens.get(3);
        if(!tok1.equalsIgnoreCase("table")){
            throw new Exception("invalid inquiry");
        }
        if(!(tok3.equalsIgnoreCase("ADD")||tok3.equalsIgnoreCase("DROP"))){
            throw new Exception("invalid inquiry, only add or drop");
        }

        if(tok3.equals("DROP")){
            //input table and check table
            Table dropCheck=new Table(tabPath);
            dropCheck.inputToObj();
            List<String> attrInFile=dropCheck.getAttributes();

            for(String tmp:attrInFile){
                if(tmp.equals(getAttributeName())){
                    throw new Exception("not this attribute");
                }
            }
        }
        if(!tabPath.exists()){
            throw new Exception("table not exist");
        }
    }
}
