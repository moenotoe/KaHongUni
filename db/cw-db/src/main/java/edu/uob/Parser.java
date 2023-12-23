package edu.uob;

import java.util.List;

public class Parser {
    Tokenizer tok;

    public Parser(Tokenizer tok){
        this.tok=tok;
    }


    public DBcmd parse() throws Exception {
        List<String> tokens = tok.tokenOutput();
        String cmdType = tokens.get(0);
        command(tokens);


        if (cmdType.equalsIgnoreCase("USE")) {
            return new useCMD(tokens);
        } else if (cmdType.equalsIgnoreCase("CREATE")) {
            return new CreateCMD(tokens);
        } else if (cmdType.equalsIgnoreCase("SELECT")) {
            return new selectCMD(tokens);
        }else if(cmdType.equalsIgnoreCase("DROP")){
            return new dropCMD(tokens);
        }else if(cmdType.equalsIgnoreCase("INSERT")){
            checkInsertExcep(tokens);
            return new insertCMD(tokens);
        }else if(cmdType.equalsIgnoreCase("ALTER")) {
            return new AlterCMD(tokens);
        }else if (cmdType.equalsIgnoreCase("UPDATE")) {
            return new updateCMD(tokens);
        }else if (cmdType.equalsIgnoreCase("DELETE")) {
            return new deleteCMD(tokens);
        }else if(cmdType.equalsIgnoreCase("JOIN")){
            return new joinCmd(tokens);
        }else {
            throw new Exception("invalid inquiry");
        }
    }

    public void command(List<String> tokens) throws Exception {
        if(!tokens.get(tokens.size()-1).equals(";")){
            throw new Exception("missing ';'");
        }
    }

    public void checkInsertExcep(List<String> command) throws Exception {
        if(!command.get(3).equalsIgnoreCase("VALUES")&&command.get(1).equalsIgnoreCase("INTO")){
            throw new Exception("not a correct query");
        }
    }

}
