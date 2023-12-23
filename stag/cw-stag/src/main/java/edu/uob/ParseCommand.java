package edu.uob;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class ParseCommand {

    String command;
    ParseAction parseAction;

    public ParseCommand(String command,ParseAction parseAction) throws Exception {
        this.command=command;
        this.parseAction=parseAction;
        correctCommand(command);
    }

    public void correctCommand(String command) throws Exception {
        String[] dividedCommand= new String[]{""};
        boolean isTriggerAndUni=false;
        boolean isBuildInAndUni=false;

        if(command.contains(":")) {
            String[] nameAndCommand = command.toLowerCase().trim().split(":");
            haveNameOrComm(nameAndCommand);
            dividedCommand = nameAndCommand[1].toLowerCase().trim().split("[!?\\s]+");
        }else {
            throw new Exception("Please type the player");
        }

        //containbuildin and unique buildin
        isTriggerAndUni=isTriggerAndUni(dividedCommand);
        isBuildInAndUni=isBuildInAndUni(dividedCommand);

        if(isBuildInAndUni==false&&isTriggerAndUni==false){
            throw new Exception("Error command!!");
        }
        if(isSameBuildAndTrigger(dividedCommand)){
            throw new Exception("You can not do two action at the same time!");
        }
    }

    public boolean isTriggerAndUni(String[] dividedCommand){
        boolean ans=false;
        for(String token:dividedCommand){
            if(isTrigger(token)==true){
                if(isUni(dividedCommand,token)==true){
                    ans=true;
                    break;
                }
            }
        }
        return ans;
    }

    public boolean isBuildInAndUni(String[] dividedCommand){
        boolean ans=false;
        for(String token:dividedCommand){
            if(isBuildIn(token)==true) {
                if(isUni(dividedCommand,token)==true)
                    ans=true;
                break;
            }
        }
        return ans;
    }

    public void haveNameOrComm(String[] nameAndCommand) throws Exception {
        if(nameAndCommand.length==1||nameAndCommand[0]==""){
            throw new Exception("Wrong Command");
        }
    }

    public boolean isSameBuildAndTrigger(String[] tokens){
        int count=0;
        for(int i=0;i<tokens.length;i++){
            if(sameBuildAndTriggerList().contains(tokens[i])){
                count++;
            }
        }
        if(count>1){
            return true;
        }
        return false;
    }

    public ArrayList<String> sameBuildAndTriggerList(){
        ArrayList<String> list=new ArrayList<>();
        for (Map.Entry<String, HashSet<GameAction>> entry:parseAction.getActionsMap().entrySet()) {
            list.add(entry.getKey());
        }
        list.add("health");
        list.add("look");
        list.add("drop");
        list.add("get");
        list.add("goto");
        list.add("inventory");
        list.add("inv");

        return list;
    }

    public boolean isTrigger(String token){
        ArrayList<String> triggerList=new ArrayList<>();
        for(Map.Entry<String, HashSet<GameAction>> entry:parseAction.actionsMap.entrySet()){
            triggerList.add(entry.getKey());
        }
        if(triggerList.contains(token)){
            return true;
        }
        return false;
    }
    public boolean isUni(String[] tokens, String target){
        int count=0;
        for (String token:tokens) {
            if (token.equals(target)) {
                count++;
            }
        }
        if(count>1){
            return false;
        }
        return true;
    }

    public boolean isBuildIn(String tokenZero){
        switch (tokenZero){
            //build in
            case "look":
            case "drop":
            case "get":
            case "goto":
            case "inventory":
            case "inv":
            case "health":
                return true;
            default:
                return false;
        }
    }


}
