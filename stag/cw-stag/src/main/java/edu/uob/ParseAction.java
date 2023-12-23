package edu.uob;

import com.alexmerz.graphviz.ParseException;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ParseAction {

    String path;
    HashMap<String, HashSet<GameAction>> actionsMap;
    public ParseAction(String xmlPath) throws IOException, ParseException, ParserConfigurationException, SAXException {
        this.actionsMap=new HashMap<>();
        this.path=xmlPath;
        parseActions();
    }

    public HashMap<String, HashSet<GameAction>> parseActions() throws IOException, SAXException, ParserConfigurationException {
        HashMap<String, HashSet<GameAction>> actionsMap=new HashMap<>();
        File file=new File(this.path);

        DocumentBuilderFactory dbFactory=DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder=dbFactory.newDocumentBuilder();
        Document document=dBuilder.parse(file);
        document.getDocumentElement().normalize();

        NodeList actionNodes=document.getElementsByTagName("action");


        for(int i=0;i<actionNodes.getLength();i++){  //every action
            Node actionSubNode=actionNodes.item(i);
            Element element=((Element)actionSubNode);

            GameAction engine=new GameAction();

            NodeList triggers=element.getElementsByTagName("triggers").item(0).getChildNodes();
            for(int j=0;j<triggers.getLength();j++){
                if(triggers.item(j).getNodeType()==Node.ELEMENT_NODE){
                    engine.trigger.add(triggers.item(j).getTextContent());
                }
            }
            NodeList subjects = element.getElementsByTagName("subjects").item(0).getChildNodes();
            for (int k=0; k<subjects.getLength();k++) {
                if (subjects.item(k).getNodeType()==Node.ELEMENT_NODE) {
                    engine.subjects.add(subjects.item(k).getTextContent());
                }
            }

            NodeList consumed=element.getElementsByTagName("consumed").item(0).getChildNodes();
            for (int l=0; l<consumed.getLength(); l++) {
                if (consumed.item(l).getNodeType() == Node.ELEMENT_NODE) {
                    engine.consumed.add(consumed.item(l).getTextContent());

                }
            }

            NodeList produced = element.getElementsByTagName("produced").item(0).getChildNodes();
            for (int m=0;m<produced.getLength();m++) {
                if (produced.item(m).getNodeType()==Node.ELEMENT_NODE) {
                    engine.products.add(produced.item(m).getTextContent());
                }
            }

            NodeList narr=((Element) actionSubNode).getElementsByTagName("narration");
            for(int n=0;n<narr.getLength();n++){
                engine.narr=narr.item(n).getTextContent();
            }
            for (String trigger:engine.trigger) {
                if (!actionsMap.containsKey(trigger)) {
                    actionsMap.put(trigger, new HashSet<>());
                }
                actionsMap.get(trigger).add(engine);
            }
        }
        return this.actionsMap=actionsMap;
    }


    public HashMap<String, HashSet<GameAction>> getActionsMap(){
        return this.actionsMap;
    }

    public ArrayList<String> getActionMapKeys(){
        ArrayList<String> keyList=new ArrayList<>();
        for(Map.Entry<String, HashSet<GameAction>> entry:actionsMap.entrySet()){
            keyList.add(entry.getKey());
        }
        return keyList;
    }

    public ArrayList<String> subjectList(){
        ArrayList<String> subElements=new ArrayList<>();
        for (Map.Entry<String, HashSet<GameAction>> entry:actionsMap.entrySet()) {
            for (GameAction action:entry.getValue()) {
                for(String subject:action.getSubjects()){
                    subElements.add(subject);
                }
            }
        }
        return subElements;
    }

}
