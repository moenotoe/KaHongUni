package edu.uob;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class InterpreterCommand {

    String command;
    ParseEntity parseEntity;
    ParseAction parseAction;

    public InterpreterCommand(String command, ParseEntity parseEntity, ParseAction parseAction){
        this.command=command;
        this.parseEntity=parseEntity;
        this.parseAction=parseAction;
    }

    public String interCommand(String command) throws Exception {
        String result="";
        String[] tokens= new String[]{""};
        String playerName="";
        Players currentPlayer=null;

        if(command.contains(":")) {
            String[] nameAndCommand = command.trim().split(":");
            playerName=nameAndCommand[0].trim();
            tokens = nameAndCommand[1].toLowerCase().trim().split("[!?\\s]+");
        }

        if (checkNameExist(playerName)==false){
            parseEntity.getPlayers().add(new Players(playerName,parseEntity));
            currentPlayer=parseEntity.getPlayerByName(playerName);
        }else {
            currentPlayer=parseEntity.getPlayerByName(playerName);
        }

        for(String token:tokens) {
            if (token.equals("look")) {
                result = interLook(currentPlayer);
            } else if (token.equals("goto")) {
                checkMultiLocations(tokens);

                String targetLocation=target(tokens,locationName());
                result = interGoto(targetLocation,currentPlayer);
            } else if (token.equals("get")) {
                checkMultiEntity(tokens);
                String targetArtefact=target(tokens,ArtefactsName(currentPlayer));
                result = interGet(targetArtefact,currentPlayer);
            } else if (token.equals("inv")||token.equals("inventory")) {
                result = interInv(currentPlayer);
            } else if (token.equals("drop")) {
                checkMultiEntity(tokens);
                String targetArtefact=target(tokens,ArtefactsName(currentPlayer));
                result = interDrop(targetArtefact,currentPlayer);
            } else if (token.equals("health")) {
                result=interHealth(currentPlayer);
            } else if (isTrigger(token)) {
                if(containSubject(tokens)==false||checkRelateEntity(tokens,token)==false){
                    throw new Exception("Please include correct subject!");
                }
                result=interAction(token,currentPlayer);
            }
        }

        return result;

    }

    public void checkMultiLocations(String[] tokens) throws Exception {
        ArrayList<String> allLocations=new ArrayList<>();
        int count=0;
        allLocations=parseEntity.getLocationList();

        for(String token:tokens){
            if(allLocations.contains(token)){
                count++;
            }
        }
        if(count>1){
            throw new Exception("You can only go one way!");
        }
    }

    public void checkMultiEntity(String[] tokens) throws Exception {
        ArrayList<String> entityList=new ArrayList<>();
        entityList=allEntityList();
        int count=0;

        for(String token:tokens){
            if(entityList.contains(token)){
                count++;
            }
        }
        if(count>1){
            throw new Exception("No Ambiguous Commands");
        }


    }

    public boolean checkRelateEntity(String[] tokens, String trigger){
        ArrayList<String> validEntityList=new ArrayList<>();
        ArrayList<String> tokensEntity=new ArrayList<>();
        ArrayList<String> allEntity=allEntityList();

        for(int i=0;i<tokens.length;i++){
            if(allEntity.contains(tokens[i])){
                tokensEntity.add(tokens[i]);
            }
        }

        for (Map.Entry<String, HashSet<GameAction>> entry:parseAction.getActionsMap().entrySet()) {
            if(entry.getKey().equalsIgnoreCase(trigger)) {
                for (GameAction action:entry.getValue()) {
                    validEntityList.addAll(action.getSubjects());
                }
            }
        }

       for(String token:tokensEntity){
           if(!validEntityList.contains(token)){
               return false;
           }
       }
        return true;
    }

    public ArrayList<String> allEntityList(){
        ArrayList<String> allEntityList=new ArrayList<>();
        for (Map.Entry<String, HashSet<GameAction>> entry:parseAction.getActionsMap().entrySet()) {
            for(GameAction action:entry.getValue()){
                allEntityList.addAll(action.getSubjects());
                allEntityList.addAll(action.getConsumed());
                allEntityList.addAll(action.getProducts());
            }
        }
        return allEntityList;
    }

    public boolean checkNameExist(String playerName){
        ArrayList<String> playersList=getAllPlayersName();
        for(String player:playersList){
            if(player.toLowerCase().equals(playerName.toLowerCase())){
                return true;
            }
        }
        return false;
    }
    public boolean containSubject(String[] tokens){
        boolean isContainSub=false;
        for(String token:tokens){
            if(parseAction.subjectList().contains(token)){
                isContainSub=true;
                break;
            }
        }
        return isContainSub;
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

    public String interHealth(Players currentPlayer){
        String interHealth="";
        interHealth=String.valueOf(currentPlayer.getHealth());
        return interHealth;
    }
    public String interAction(String token,Players currentPlayer) throws Exception {
        String interAction = "";
        for (Map.Entry<String, HashSet<GameAction>> entry:parseAction.getActionsMap().entrySet()) {
            if(entry.getKey().equals(token)){  //ex open
                if(entry.getKey().equalsIgnoreCase("fight")||entry.getKey().equalsIgnoreCase("attack")||entry.getKey().equalsIgnoreCase("hit")){
                    //just consume health && check the elf in curr location!
                    processFight(entry.getValue(),currentPlayer);
                    currentPlayer.lostHealth();
                    if(isLive(currentPlayer)==false){
                        processDead(currentPlayer);
                        return interAction="You dead";
                    }
                }
                processSubAndConsum(entry.getKey(),entry.getValue(),currentPlayer);
                processProduct(entry.getValue(),currentPlayer);
                for (GameAction action:entry.getValue()) {
                    interAction=action.narr;
                }
            }
        }

        if(token.equals("open")||token.equals("unlock")){
            parseEntity.turnOnTriggerDoor();
        }
        if(token.equals("bridge")){
            parseEntity.turnOnTriggerBridge();
        }
        return interAction;
    }
    public void processFight(HashSet<GameAction> entryValue,Players currentPlayer) throws Exception {
        for(GameAction acton:entryValue){
            for(String set:acton.getSubjects()){
                String currLocation=currentPlayer.getCurrentLocation();
                Location location=parseEntity.getLocationByName(currLocation);
                if(!location.getLocationEntity().containsKey(set)){
                    throw new Exception("the location not this enemy!");
                }
            }
        }

    }
    public void processDead(Players currentPlayer){
        //put all stuff to current location, let player back to init location
        ArrayList<GameEntity> stuffs=new ArrayList<>();
        for(int i=0;i<currentPlayer.getStuff().size();i++){
            stuffs.add(currentPlayer.getStuff().get(i));
        }
        //add stuff to cur location
        String currLocation=currentPlayer.getCurrentLocation();
        for(GameEntity entity:stuffs){
            parseEntity.getLocationByName(currLocation).addLocationEntity(entity.getName(),entity);
        }
        currentPlayer.resetPlayer();
        currentPlayer.setCurrentLocation(parseEntity.initLocation());
    }
    public boolean isLive(Players currentPlayer){
       if(currentPlayer.getHealth()==0){
           return false;
       }
       return true;
    }
    public void processProduct(HashSet<GameAction> actionMapValue,Players currentPlayer) {
        ArrayList<String> product = new ArrayList<>();
        for (GameAction action:actionMapValue) {
            for (String loopSet:action.getProducts()) {
                product.add(loopSet);
            }
        }
        for (int i = 0; i < product.size(); i++) {
            productTypeAndProcess(product.get(i),currentPlayer);
        }
    }
    public void productTypeAndProcess(String productName,Players currentPlayer){
        if(productType(productName).equals("Location")){
            checkUnlocked(currentPlayer.getCurrentLocation());
        } else if (productType(productName).equals("Artefacts")) {
            stuffToPlayerFromRoom(productName,currentPlayer);
        } else if (productType(productName).equals("Characters")) {
            processCallNPC(productName, currentPlayer);
        } else if (productType(productName).equals("Furniture")) {
            roomToLocation(productName,currentPlayer.getCurrentLocation());
            removeFromRoom(productName);
        } else if (productType(productName).equals("health")) {
            if(currentPlayer.getHealth()<3) {
                currentPlayer.increaseHealth();
            }
        }
    }

    public void processCallNPC(String productName,Players currentPlayer){

        if(parseEntity.getLocationByName("storeroom").getLocationEntity().containsKey(productName)) {

            roomToLocation(productName, currentPlayer.getCurrentLocation());
            removeFromRoom(productName);
        }else if(npcInCurrLocation(productName,currentPlayer)) {
            return;
        }else {
            for(int i=0;i<parseEntity.getMap().size();i++){
                String currLocation=currentPlayer.getCurrentLocation();
                if(parseEntity.getMap().get(i).getLocationEntity().containsKey(productName)&&!parseEntity.getLocationByName(currLocation).getLocationEntity().containsKey(productName)) {
                    GameEntity entity = parseEntity.getMap().get(i).getLocationEntity().get(productName);
                    parseEntity.getLocationByName(currLocation).addLocationEntity(productName, entity);
                    parseEntity.getMap().get(i).removeLocationEntity(productName);
                }
            }
        }
    }

    public boolean npcInCurrLocation(String productName,Players currentPlayer){
        String currLocation=currentPlayer.getCurrentLocation();
        if(parseEntity.getLocationByName(currLocation).getLocationEntity().containsKey(productName)){
            return true;
        }
        return false;
    }

    public void stuffToPlayerFromRoom(String productName,Players currentPlayer){
        //copy to player, remove from room
        GameEntity entity=parseEntity.getLocationByName("storeroom").getLocationEntity().get(productName);
        currentPlayer.addStuff(entity);
        parseEntity.getLocationByName("storeroom").removeLocationEntity(productName);
    }

    public void removeFromRoom(String stuffName){
        parseEntity.getLocationByName("storeroom").getLocationEntity().remove(stuffName);
    }

    public void roomToLocation(String productName,String currLocation){
        GameEntity entity=parseEntity.getLocationByName("storeroom").getLocationEntity().get(productName);
        parseEntity.getLocationByName(currLocation).addLocationEntity(productName,entity);
    }
    public String productType(String productEntityName){
        String typeofProduct="";

        for (Location location : parseEntity.getMap()) {
            GameEntity entity = location.getLocationEntity().get(productEntityName);
            if (entity != null && entity instanceof Furniture) {
                typeofProduct="Furniture";
            } else if (entity != null && entity instanceof Artefacts) {
                typeofProduct="Artefacts";
            } else if (entity != null && entity instanceof Characters) {
                typeofProduct = "Characters";
            } else if (parseEntity.getLocationList().contains(productEntityName)) {
                typeofProduct = "Location";
            } else if (productEntityName.equals("health")) {
                typeofProduct="health";
            }
        }
        return typeofProduct;
    }
    public void processSubAndConsum(String actionHashmapKey,HashSet<GameAction> actionMapValue,Players currentPlayer) throws Exception {
        ArrayList<String> tmp=new ArrayList<>();
        for (GameAction action:actionMapValue) {
            for(String loopSet:action.getSubjects()){
                tmp.add(loopSet);
            }
        }

        for(String str:tmp){
            if(isConsumed(str,actionMapValue)==true) {
                checkBagOrLocation(str,currentPlayer);
            }else {
                //is subject-->in current location?/ in inv?(ex shovel-->is artefact, but is subject(not consumed after use!))
                String currLocation=currentPlayer.getCurrentLocation();//<--subject in here?
                Location currLocatObj=parseEntity.getLocationByName(currLocation);
                if(!currLocatObj.getLocationEntity().containsKey(str)&&!currentPlayer.getAllStuffName().contains(str)){
                   throw new Exception("there are no this subject/You don not have the tools!");
               }
            }
        }
    }

    public void checkBagOrLocation(String str,Players currentPlayer) throws Exception {
        if(isInCurrLocation(str,currentPlayer.getCurrentLocation())||currentPlayer.getAllStuffName().contains(str)){
            //put in to storeroom(check in curr location or in inv first)
            if(isInCurrLocation(str,currentPlayer.getCurrentLocation())==true){
                if(checkBagAndLocation(currentPlayer,str)==true) {
                    putLocStuffToRoom(currentPlayer.getCurrentLocation(), str);
                }
            } else if (currentPlayer.getAllStuffName().contains(str)==true) {
                //put inv's artefact to storeroom
                putPlayerStuffToStoreroom(currentPlayer,str);
            }
        }else{
            throw new Exception("You can not do that!");
        }
    }
    public boolean checkBagAndLocation(Players currentPlayer, String realConsumed){
        String realSub="";
        Boolean locationSub=false;
        Boolean bagSub=false;
        String currLocation=currentPlayer.getCurrentLocation();

        for (Map.Entry<String, HashSet<GameAction>> entry:parseAction.getActionsMap().entrySet()) {
            for (GameAction action:entry.getValue()) {
                if(action.getConsumed().contains(realConsumed)){
                    for(String sub:action.getSubjects()){
                        if(!sub.equals(realConsumed)){
                            realSub=sub;
                        }
                    }
                }
            }
        }
        bagSub=judgeBagSub(currentPlayer,realSub);
        locationSub=judgeLocationSub(currLocation,realSub);

        if(bagSub==true||locationSub==true){
            return true;
        }else {
            return false;
        }
    }

    public boolean judgeBagSub(Players currentPlayer,String realSub){
        Boolean bagSub=false;
        for(int i=0;i<currentPlayer.getAllStuffName().size();i++){
            if(currentPlayer.getAllStuffName().contains(realSub)){
                bagSub=true;
            }
        }
        return bagSub;
    }

    public boolean judgeLocationSub(String currLocation, String realSub){
        Boolean locationSub=false;
        for (int i=0;i<parseEntity.getLocationByName(currLocation).getLocationEntity().size();i++){
            if(parseEntity.getLocationByName(currLocation).getLocationEntity().containsKey(realSub)){
                locationSub=true;
            }
        }
        return locationSub;
    }

    public void putLocStuffToRoom(String targetLocation,String targetStuff){
        //copy the stuff in location to room->remove the stuff in location(the entity!)
        GameEntity entity=parseEntity.getLocationByName(targetLocation).getLocationEntity().get(targetStuff);
        Location storeroom=parseEntity.getLocationByName("storeroom");
        storeroom.getLocationEntity().put(targetStuff,entity);
        parseEntity.getLocationByName(targetLocation).removeLocationEntity(targetStuff);
    }
    public void putPlayerStuffToStoreroom(Players targetPlayer, String targetStuff){
        //get from inv(generate stuff)->put in to storeroom->remove from player's inv
        GameEntity stuffEntity=targetPlayer.getStuffObj(targetStuff);
        Location storeroom=parseEntity.getLocationByName("storeroom");
        storeroom.getLocationEntity().put(targetStuff,stuffEntity);
        targetPlayer.removeStuff(targetStuff);
    }



    public boolean isInCurrLocation(String targetArtefact, String userCurrPlace){
        //loop the location, have the artefact's name?
        for(int i=0;i<parseEntity.getMap().size();i++){
            if(parseEntity.getMap().get(i).getName().equals(userCurrPlace)){
                if(parseEntity.getMap().get(i).getLocationEntity().containsKey(targetArtefact)){
                    return true;
                }
            }
        }
        return false;
    }
    public boolean isConsumed(String entityName,HashSet<GameAction> actionMapValue){
        boolean isConsumed=false;
        for (GameAction action:actionMapValue) {
            if(action.getConsumed().contains(entityName)&&action.getSubjects().contains(entityName)||entityName.equals("health")){
                isConsumed=true;
            }
        }
        return isConsumed;
    }

    //build in command
    public String interDrop(String targetStuff,Players currentPlayer) throws Exception {
        GameEntity entityObj=currentPlayer.getStuffObj(targetStuff);
        if(entityObj==null){
            throw new Exception("You don't have this stuff, what can you drop?");
        }
        for(int i=0;i<parseEntity.getMap().size();i++){
            //the entity location=player cur location
            if(parseEntity.getMap().get(i).getName().equals(currentPlayer.getCurrentLocation())){
                parseEntity.getMap().get(i).addLocationEntity(targetStuff,entityObj);
            }
        }
        currentPlayer.removeStuff(targetStuff);
        return "You drop "+targetStuff+", it is in the floor.";

    }
    public String interInv(Players currentPlayer){
        StringBuilder interInv=new StringBuilder();
        interInv.append("Your inventory have: \n");

        interInv.append(currentPlayer.getAllStuffDes());
        return interInv.toString();
    }
    public String interGet(String getStuff,Players currentPlayer) throws Exception {

        if(parseEntity.isStuffInLocationAndCanGet(currentPlayer.getCurrentLocation(),getStuff)){
            parseEntity.putIntoInvFromLocation(getStuff,currentPlayer);
            //remove from cur location to room
            putLocStuffToRoom(currentPlayer.getCurrentLocation(),getStuff);
            return "You picked up a "+ getStuff;
        }else {
            throw new Exception("You can not get this stuff");
        }
    }
    public String interGoto(String targetLocation,Players currentPlayer) throws Exception {
        String interGoto="";

        //1 player
        if(targetLocation.equals("cellar")&&parseEntity.isTriggerDoor()==false) {
            throw new Exception("the location is locked");
        } else if (targetLocation.equals("clearing")&&parseEntity.isTriggerBridge()==false) {
            throw new Exception("the location is locked");
        }else if(targetLocation.equals("cellar")&&currentPlayer.getCurrentLocation().equals("cabin")&&parseEntity.triggerDoor==true){
            currentPlayer.setCurrentLocation(targetLocation);
            interGoto = interLook(currentPlayer);
        } else if (targetLocation.equals("cabin")&&(currentPlayer.getCurrentLocation().equals("cellar")||currentPlayer.getCurrentLocation().equals("forest"))) {
            currentPlayer.setCurrentLocation(targetLocation);
            interGoto = interLook(currentPlayer);
        }else if (targetLocation.equals("forest")&&(currentPlayer.getCurrentLocation().equals("cabin")||currentPlayer.getCurrentLocation().equals("riverbank"))) {
            currentPlayer.setCurrentLocation(targetLocation);
            interGoto = interLook(currentPlayer);
        }else if (targetLocation.equals("riverbank")&&(currentPlayer.getCurrentLocation().equals("forest")||currentPlayer.getCurrentLocation().equals("clearing"))) {
            currentPlayer.setCurrentLocation(targetLocation);
            interGoto = interLook(currentPlayer);
        }else if(targetLocation.equals("clearing")&&currentPlayer.getCurrentLocation().equals("riverbank")&&parseEntity.triggerBridge==true){
            currentPlayer.setCurrentLocation(targetLocation);
            interGoto = interLook(currentPlayer);
        }else{
            throw new Exception("You can not go there!");
        }
        return interGoto;
    }
    public String interLook(Players currentPlayer) throws Exception {
        String interLook="";
        String currLocation=currentPlayer.getCurrentLocation();

        switch (currLocation){
            //cabin
            case "cabin":
                interLook=cabinDetail();
                break;
            case "forest":
                interLook=forestDetail();
                break;
            case "cellar":
                interLook=cellarDetail();
                break;
            case "riverbank":
                interLook=riverbankDetail();
                break;
            case "clearing":
                interLook=clearingDetail();
                break;
        }
        return interLook;
    }

    public String cabinDetail(){
        StringBuilder cabinStuff=new StringBuilder();
        ArrayList<String> playerList=new ArrayList<>();
        for(int i=0;i<parseEntity.getPlayers().size();i++){
            if(parseEntity.getPlayers().get(i).getCurrentLocation().equals("cabin"))
            playerList.add(parseEntity.getPlayers().get(i).getName());
        }
        cabinStuff.append("You are in ");
        cabinStuff.append(locationDescription("cabin"));
        cabinStuff.append(" You can see:\n");
        cabinStuff.append(playerCanSeeStuff("cabin"));
        for(String player:playerList){
            cabinStuff.append(player+"\n");
        }
        cabinStuff.append("You can access from here:\n");

        cabinStuff.append(avaliPath("cabin")+"\n");

        return cabinStuff.toString();
    }

    public String forestDetail(){
        StringBuilder forestStuff=new StringBuilder();
        ArrayList<String> playerList=new ArrayList<>();
        for(int i=0;i<parseEntity.getPlayers().size();i++){
            if(parseEntity.getPlayers().get(i).getCurrentLocation().equals("forest"))
                playerList.add(parseEntity.getPlayers().get(i).getName());
        }
        forestStuff.append("You are in ");
        forestStuff.append(locationDescription("forest"));
        forestStuff.append("You can see:\n");
        forestStuff.append(playerCanSeeStuff("forest"));
        for(String player:playerList){
            forestStuff.append(player+"\n");
        }
        forestStuff.append("You can access from here:\n");
        forestStuff.append(avaliPath("forest")+"\n");
        return forestStuff.toString();


    }

    public String cellarDetail(){
        StringBuilder cellarStuff=new StringBuilder();
        ArrayList<String> playerList=new ArrayList<>();
        for(int i=0;i<parseEntity.getPlayers().size();i++){
            if(parseEntity.getPlayers().get(i).getCurrentLocation().equals("cellar"))
                playerList.add(parseEntity.getPlayers().get(i).getName());
        }
        cellarStuff.append("You are in ");
        cellarStuff.append(locationDescription("cellar"));
        cellarStuff.append("You can see:\n");
        cellarStuff.append(playerCanSeeStuff("cellar"));
        for(String player:playerList){
            cellarStuff.append(player+"\n");
        }
        cellarStuff.append("You can access from here:\n");
        cellarStuff.append(avaliPath("cellar"));
        return cellarStuff.toString();
    }

    public String riverbankDetail(){
        StringBuilder riverbankStuff=new StringBuilder();
        ArrayList<String> playerList=new ArrayList<>();
        for(int i=0;i<parseEntity.getPlayers().size();i++){
            if(parseEntity.getPlayers().get(i).getCurrentLocation().equals("riverbank"))
                playerList.add(parseEntity.getPlayers().get(i).getName());
        }
        riverbankStuff.append("You are in ");
        riverbankStuff.append(locationDescription("riverbank"));
        riverbankStuff.append("You can see:\n");
        riverbankStuff.append(playerCanSeeStuff("riverbank"));
        for(String player:playerList){
            riverbankStuff.append(player+"\n");
        }
        riverbankStuff.append("You can access from here:\n");
        riverbankStuff.append(avaliPath("riverbank"));
        return riverbankStuff.toString();
    }

    public String clearingDetail(){
        StringBuilder clearingStuff=new StringBuilder();
        ArrayList<String> playerList=new ArrayList<>();
        for(int i=0;i<parseEntity.getPlayers().size();i++){
            if(parseEntity.getPlayers().get(i).getCurrentLocation().equals("clearing"))
                playerList.add(parseEntity.getPlayers().get(i).getName());
        }
        clearingStuff.append("You are in ");
        clearingStuff.append(locationDescription("clearing"));
        clearingStuff.append("You can see:\n");
        clearingStuff.append(playerCanSeeStuff("clearing"));
        for(String player:playerList){
            clearingStuff.append(player+"\n");
        }
        clearingStuff.append("You can access from here:\n");
        clearingStuff.append(avaliPath("clearing"));
        return clearingStuff.toString();
    }

    public String[] locationName(){
        ArrayList<String> str=new ArrayList<>();
        for(int i=0;i<parseEntity.getMap().size();i++){
            str.add(parseEntity.getMap().get(i).getName());
        }
        String[] strArray=str.toArray(new String[0]);
        return strArray;
    }

    public String[] ArtefactsName(Players currentPlayer){

        ArrayList<String> str=new ArrayList<>();

        for(Location loc:parseEntity.getMap()){
            for (Map.Entry<String, GameEntity> entry:loc.getLocationEntity().entrySet()) {
                String key=entry.getKey();
                str.add(key);
            }
        }
        for(String stuff:currentPlayer.getAllStuffName()){
            str.add(stuff);
        }

        String[] strArray = str.toArray(new String[0]);
        return strArray;
    }

    public String playerCanSeeStuff(String targetLocation){
        StringBuilder str=new StringBuilder();
        for(int i=0;i<parseEntity.getMap().size();i++){
            if(parseEntity.getMap().get(i).getName().equals(targetLocation)){
                for (Map.Entry<String, GameEntity> entry:parseEntity.getMap().get(i).getLocationEntity().entrySet()) {
                    str.append(entry.getValue().getDescription()+"\n");
                }
            }
        }
        return str.toString();
    }

    public String locationDescription(String location){
        StringBuilder str=new StringBuilder();
        for(int i=0;i<parseEntity.getMap().size();i++){
            if(parseEntity.getMap().get(i).getName().equals(location)){
                str.append(parseEntity.getMap().get(i).getDescription()+".");
            }
        }
        return str.toString();
    }

    public String target(String[] tokens,String[] target) throws Exception {

        String str="";
        boolean haveMatchTarget=false;
        for (String word1:tokens) {
            for (String word2:target) {
                if (word1.equals(word2)) {
                    str=word1;
                    haveMatchTarget=true;
                }
            }
        }
        if(haveMatchTarget==false){
            throw new Exception("not have this location");
        }
        return str;
    }


    public String avaliPath(String currLocation){
        StringBuilder avaPath=new StringBuilder();
        if(currLocation.equals("cabin")){
            //check the storeroom whether have key
            avaPath.append("forest"+"\n");
            String checkUnlocked=checkUnlocked(currLocation);
            avaPath.append(checkUnlocked+"\n");

        } else if (currLocation.equals("forest")) {
            avaPath.append("cabin"+"\n");
            avaPath.append("riverbank"+"\n");
        } else if (currLocation.equals("riverbank")) {
            avaPath.append("forest"+"\n");
            String checkUnlocked=checkUnlocked(currLocation);
            avaPath.append(checkUnlocked+"\n");
        } else if (currLocation.equals("clearing")) {
            avaPath.append("riverbank"+"\n");
        } else if (currLocation.equals("cellar")) {
            avaPath.append("cabin"+"\n");
        }


        return avaPath.toString();
    }

    public String checkUnlocked(String currLocation){

        //**use action to check!
        String tmp="";
        for(int i=0;i<parseEntity.getMap().size();i++){
            if(parseEntity.getMap().get(i).getName().equals("storeroom")){
                //check have the key?
                tmp=haveKey(tmp,i,currLocation);
            }
        }
        return tmp;
    }

    public String haveKey(String tmp,int i,String currLocation){
        if(parseEntity.getMap().get(i).getLocationEntity().containsKey("key")||parseEntity.getMap().get(i).getLocationEntity().containsKey("log")){
            if(parseEntity.getMap().get(i).getLocationEntity().containsKey("key")&& parseEntity.triggerDoor==true&&currLocation.equals("cabin")) {
                tmp = "cellar";
            } else if (parseEntity.getMap().get(i).getLocationEntity().containsKey("log") && parseEntity.triggerBridge==true&&currLocation.equals("riverbank")) {
                tmp="clearing";
            }
        }
        return tmp;
    }

    public ArrayList<String> getAllPlayersName(){
        ArrayList<String> nameList=new ArrayList<>();
        for(int i=0;i<parseEntity.getPlayers().size();i++){
            nameList.add(parseEntity.getPlayers().get(i).getName());
        }
        return nameList;
    }
}
