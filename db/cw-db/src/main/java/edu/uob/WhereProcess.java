package edu.uob;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WhereProcess {

    public List<String> combineWithMulti(List<String> condition){
        if (condition.contains(">") && condition.get(condition.indexOf(">") + 1).equals("=")) {
            return combineGreaEqu(condition);
        } else if (condition.contains("<") && condition.get(condition.indexOf("<") + 1).equals("=")) {
            return combineLessEqu(condition);
        }else if (condition.contains("=") && condition.get(condition.indexOf("=") + 1).equals("=")) {
            return combineEqu(condition);
        }else if (condition.contains("!") && condition.get(condition.indexOf("!") + 1).equals("=")) {
            return combineNotEqu(condition);
        } else {
            return condition;
        }
    }

    public void removeQuo(List<String> whereCon){
        for (int i = 0; i < whereCon.size(); i++) {
            String s = whereCon.get(i);
            whereCon.set(i, s.replace("'", ""));
        }
    }
    public List<String> combineEqu(List<String> whereCon){
        List<String> build=new ArrayList<>();
        for(int i=0;i<whereCon.size();i++){
            if(!(whereCon.get(i).equals("=")&&whereCon.get(i+1).equals("="))){
                build.add(whereCon.get(i));
            }else {
                build.add("==");
                i++;
            }
        }
        return build;
    }
    public List<String> combineNotEqu(List<String> whereCon){
        List<String> build=new ArrayList<>();
        for(int i=0;i<whereCon.size();i++){
            if(!(whereCon.get(i).equals("!")&&whereCon.get(i+1).equals("="))){
                build.add(whereCon.get(i));
            }else {
                build.add("!=");
                i++;
            }
        }
        return build;
    }

    public List<String> combineGreaEqu(List<String> whereCon){
        List<String> build=new ArrayList<>();
        for(int i=0;i<whereCon.size();i++){
            if(!(whereCon.get(i).equals(">")&&whereCon.get(i+1).equals("="))){
                build.add(whereCon.get(i));
            }else {
                build.add(">=");
                i++;
            }
        }
        return build;
    }

    public List<String> combineLessEqu(List<String> whereCon){
        List<String> build=new ArrayList<>();
        for(int i=0;i<whereCon.size();i++){
            if(!(whereCon.get(i).equals("<")&&whereCon.get(i+1).equals("="))){
                build.add(whereCon.get(i));
            }else {
                build.add("<=");
                i++;
            }
        }
        return build;
    }
    public boolean isDigitCombine(String tmp){
        String pattern =  "^[-+]?\\d+$";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(tmp);

        return m.matches();
    }

    public boolean isFloat(String tmp){
        String pattern =  "^[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?$";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(tmp);

        return m.matches();
    }

    public boolean isBool(String tmp){
        String pattern =  "^(?i)(true|false)$";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(tmp);

        return m.matches();
    }

    public boolean isString(String tmp){
        String pattern =  "^\\w+$";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(tmp);

        return m.matches();
    }
    public boolean isNull(String tmp){
        String pattern =  "^null+$";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(tmp);

        return m.matches();
    }
    public List<String> sparateQuo(List<String> whereList){
        //TEST
        ArrayList<String> tmpList=new ArrayList<>();
        String tmp="^'.*'$";
        Pattern pattern=Pattern.compile(tmp);

        for(String ans:whereList){
            Matcher mat=pattern.matcher(ans);
            if(mat.matches()==true){
                String tmp2[]= ans.split("'");
                tmpList.add("'");
                tmpList.add(tmp2[1].trim());
                tmpList.add("'");
            }else {
                tmpList.add(ans);
            }
        }
        return tmpList;

    }

    public ArrayList<String> combineFloat(List<String> arr){
        //if have float ,combine
        ArrayList<String> tmp=new ArrayList<>();
        for(int i=0;i<arr.size();i++){
            if(arr.get(i).equals(".")&&isDigitCombine(arr.get(i-1))&&isDigitCombine(arr.get(i+1))){
                tmp.add(arr.get(i-1)+arr.get(i)+arr.get(i+1));
                tmp.remove(arr.get(i-1));
                i++;
            }else {
                tmp.add(arr.get(i));
            }
        }

        return tmp;
    }
}
