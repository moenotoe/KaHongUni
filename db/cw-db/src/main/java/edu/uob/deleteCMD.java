package edu.uob;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class deleteCMD extends DBcmd {
    private List<String> tokens;
    private String storageFolderPath;


    public deleteCMD(List<String> tokens){
        this.tokens=tokens;
        this.storageFolderPath= Paths.get("databases").toAbsolutePath().toString();
    }

    @Override
    String query(DBServer s) throws Exception {
        String tabNamePath=selectTabName(s);
        File tabPath=new File(tabNamePath);
        String tableName=tokens.get(2);
        //get table
        Table tab=new Table(tabPath);
        checkException();
        List<String> whereCon=new ArrayList<>();

        int start=0;
        //get condition
        for(String tmp:tokens){
            if(tmp.equalsIgnoreCase("WHERE")){
                start= tokens.indexOf(tmp);
            }
        }
        int end=tokens.indexOf(";");
        whereCon=tokens.subList(start+1,end);

        WhereProcess pro=new WhereProcess();
        pro.removeQuo(whereCon);
        List<String> build=new ArrayList<>();
        if(tokens.contains(">")&&tokens.get(tokens.indexOf(">")+1).equals("=")){
            build=pro.combineGreaEqu(whereCon);
        } else if (tokens.contains("<")&&tokens.get(tokens.indexOf("<")+1).equals("=")) {
            build=pro.combineLessEqu(whereCon);
        }else {
            build=whereCon;
        }

        List<String> wildList=new ArrayList<>();
        wildList.add("*");

        for(int i=0;i<build.size();i++){
            if(build.get(i).equals("(")||build.get(i).equals(")")){
                build.remove(i);
            }
        }
        //combine
        build=pro.combineWithMulti(build);
        Condition con=new Condition(wildList,build, tab);
        con.getColCon();
        ExeCom exe=new ExeCom();
        String selectAns=exe.deleteCommand(tab,con);
        return "delete success";
    }

    public String selectTabName(DBServer s){
        return this.storageFolderPath.toString()+File.separator+s.getCurDb().toString()+File.separator+tokens.get(2)+".tab";
    }
    public void checkException() throws Exception {
        if(!tokens.get(1).equalsIgnoreCase("FROM")||!tokens.get(3).equalsIgnoreCase("WHERE")){
            throw new Exception("invalid inquiry");
        }
    }

    public void removeQuo(List<String> whereCon){
        for (int i = 0; i < whereCon.size(); i++) {
            String s = whereCon.get(i);
            whereCon.set(i, s.replace("'", ""));
        }
    }
}
