package edu.uob;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class selectCMD extends DBcmd {
    private List<String> tokens;
    final private String storageFolderPath;

    public selectCMD(List<String> tokens){
        this.tokens=tokens;
        this.storageFolderPath= Paths.get("databases").toAbsolutePath().toString();
    }
    @Override
    String query(DBServer s) throws Exception {
        //get file path
        List<String> wildList=new ArrayList<>();
        WhereProcess pro = new WhereProcess();
        String tabNamePath=selectFileName(s);
        File tabPath=new File(tabNamePath);
        if(!tabPath.exists()){
            throw new Exception("table not exist");
        }
        //get table
        Table tab=new Table(tabPath);
        //judge is illegal query
        int startWild=0;
        for(String tmp:tokens) {
            if(tmp.equalsIgnoreCase("SELECT"))
               startWild=tokens.indexOf(tmp);
        }
        int endWild=0;
        for(String tmp:tokens) {
            if(tmp.equalsIgnoreCase("FROM"))
                endWild=tokens.indexOf(tmp);
        }
        judgeInquiry(endWild);
        for(int i=0;i<tokens.size();i++) {
            if(tokens.get(i).equals("(")||tokens.get(i).equals(")")) {
                tokens.remove(i);
            }
        }
        wildList=tokens.subList(startWild+1,endWild);
        checkWildList(wildList);
        if(tokens.get(endWild+2).equals(";")) {
            String ans=noWhereCon(endWild, wildList, tab);
            return ans;
        }
        //the singal condition
;
        int startCon=startConSet();
        int endCon=tokens.indexOf(";");
        List<String> whereCon= new ArrayList<>();
        whereCon=tokens.subList(startCon+1,endCon);
        checkWhereCon(whereCon,pro);
        //process single where condition
        for(int i=0;i<tokens.size();i++){
            if(tokens.get(i).equals("and")||tokens.get(i).equals("or")){
                tokens.set(tokens.indexOf(tokens.get(i)),tokens.get(i).toUpperCase());
            }
        }

        if(!tokens.contains("OR")&&!tokens.contains("AND")) {
            List<String> build = new ArrayList<>();
            ExeCom exe = new ExeCom();
            pro.removeQuo(whereCon);
            build=pro.combineWithMulti(whereCon);
            //combine flo
            build=pro.combineFloat(build);
            Condition con = new Condition(wildList, build, tab);
            String selectAns = exe.selectCommand(tab, con);
            return selectAns;
        }else {

            //multi condition
            List<List<String>> andCon = new ArrayList<List<String>>();
            //pick up 'and' condition
            List<String> whereMultiCon = new ArrayList<>();
            whereMultiCon=tokens.subList(startCon + 1, endCon);
            andCon=generateAndConList(whereMultiCon,whereCon);
            List<LinkedHashMap<String,String>> ans=new ArrayList<>();
            Table tableMulti=new Table(tabPath);
            tableMulti.inputToObj();
            for(int i=0;i<tableMulti.getRow().size();i++){

                Condition connew=new Condition();
                Boolean result=connew.topCheck(andCon,tableMulti.getRow().get(i));

                if(result==true){
                    ans.add(tableMulti.getRow().get(i));

                }
            }
            Table ansTab=new Table(tabPath);
            MultiAns mulAns=new MultiAns(ans,wildList,ansTab);
            String finalAns=mulAns.result();
            return finalAns;
        }
    }



    public void  checkWildList(List<String> wildList)throws Exception{
        Filter fil=new Filter(wildList);
        if(!fil.isWildAttribList()){
            throw new Exception("not a wildList");
        }
    }
    public void  checkWhereCon(List<String> whereCon,WhereProcess pro) throws Exception {
        List<String> newCheck=pro.sparateQuo(whereCon);

        Filter fil=new Filter(newCheck);
        if(!fil.isGeneralCondition()){
            throw new Exception("not a where condition");
        }
        if(!tokens.contains("from")&&!tokens.contains("FROM")){
            throw new Exception("invalid inquiry for select");
        }
    }


    public String noWhereCon(int endWild, List<String> wildList,Table tab) throws IOException {
        List<String> empty=new ArrayList<>();
        empty.add("flag");
        if(tokens.get(endWild+2).equals(";")){
            Condition conNoWhere=new Condition(wildList,empty,tab);
            ExeCom exeNoWhere=new ExeCom();

            String noWhereAns=exeNoWhere.selectCommand(tab,conNoWhere);
            return noWhereAns;
        }else {
            return null;
        }

    }
    public void judgeInquiry(int endWild) throws Exception {

        if (!(tokens.contains("where")||tokens.contains("WHERE"))&&!tokens.get(endWild+2).equals(";")) {
            throw new Exception("invalid inquiry");
        }
    }

    public String selectFileName(DBServer s){
        //select tableName
        int index=0;
        while(!tokens.get(index).equalsIgnoreCase("FROM")){
            index++;
        }
        index++;
        String tabName=tokens.get(index).toLowerCase();
        return this.storageFolderPath.toString()+File.separator+s.getCurDb().toString()+File.separator+tabName+".tab";
    }



    public int startConSet(){
        int startCon=0;
        for(String tmp:tokens){
            if(tmp.equalsIgnoreCase("WHERE")){
                startCon=tokens.indexOf(tmp);
            }
        }
        return startCon;
    }

    public List<List<String>> generateAndConList(List<String> whereMultiCon, List<String>whereCon){
        List<List<String>> tmp=new ArrayList<>();
        WhereProcess pro=new WhereProcess();
        int start = 0;
        for (int i = 0; i < whereMultiCon.size(); i++) {
            //separate many condition
            if (whereMultiCon.get(i).equalsIgnoreCase("and")||whereMultiCon.get(i).equalsIgnoreCase("or")) {
                int substart = start;
                int endsub = i;
                List<String> conSubList = whereMultiCon.subList(substart, endsub);
                List<String> build = new ArrayList<>();
                build=pro.combineWithMulti(conSubList);
                List<String> combine=new ArrayList<>();
                combine=pro.combineWithMulti(build);
                pro.removeQuo(combine);
                tmp.add(combine);
                List<String> tmp1=new ArrayList<>();
                tmp1.add(whereMultiCon.get(i));
                tmp.add(tmp1);
                start=endsub + 1;
            } else {
                continue;
            }
        }
        List<String> tmpsub=whereCon.subList(start,whereMultiCon.size());
        List<String> combine=new ArrayList<>();
        combine=pro.combineWithMulti(tmpsub);
        pro.removeQuo(combine);
        tmp.add(combine);
        return tmp;
    }


}
