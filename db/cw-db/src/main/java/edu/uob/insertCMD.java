package edu.uob;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class insertCMD extends DBcmd{
    private List<String> tokens;
    private String storageFolderPath;
    private String extension=".tab";


    public insertCMD(List<String> tokens){
        this.tokens=tokens;
        this.storageFolderPath= Paths.get("databases").toAbsolutePath().toString();
    }
    @Override
    String query(DBServer s) throws Exception {
        String tabName= tokens.get(2).toLowerCase();
        File Path=new File(s.getTabPath(tabName));
        checkTab(Path);
        insertVal(tokens,s,s.getTabPath(tabName),Path);
        return "insert success";
    }

    public void checkTab(File tabPath) throws Exception {
        if(!tabPath.exists()){
            throw new Exception("table not exist");
        }
        if(!tokens.get(3).equalsIgnoreCase("values")){
            throw new Exception("invalid inquiry");
        }
    }
    public void insertVal(List<String> tokens,DBServer s,String Path,File tab) throws Exception {
        //substring (insert element)
        int startIndex=tokens.indexOf("(");
        int endIndex=tokens.indexOf(")");
        //get the att list
        List<String> attList = tokens.subList(startIndex+1, endIndex);
        WhereProcess checkQuo=new WhereProcess();
        List<String> sepaQuo=checkQuo.sparateQuo(attList);
        Filter fil=new Filter(sepaQuo);
        //parse
        if(!fil.isValueList()){
            throw new Exception("invalid inquiry");
        }
        sepaQuo.removeIf(coma->coma.equals(","));
        sepaQuo.removeIf(sinQuo->sinQuo.equals("'"));
        ArrayList<String> combineList=new ArrayList<>();
        combineList=checkQuo.combineFloat(sepaQuo);
        ArrayList<String> rowVal=new ArrayList<>(combineList);
        ExeCom exe=new ExeCom();
        exe.insertCommand(combineList,tab);
        return;
    }



}
