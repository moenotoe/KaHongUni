package edu.uob;

import java.io.File;

import java.nio.file.Paths;
import java.util.List;

public class useCMD extends DBcmd{

    private List<String> tokens;
    
    public useCMD(List<String> tokens){
        this.tokens=tokens;
    }

    @Override
    String query(DBServer s) throws Exception {
        //Exception:db not exist
        String dbName=this.tokens.get(1).toLowerCase();
        String storageFolderPath = Paths.get("databases").toAbsolutePath().toString();
        File Dbpath=new File(storageFolderPath+File.separator+dbName);

        if (!(Dbpath.exists() && Dbpath.isDirectory())) {
            throw new Exception("database not exist");
        }

        //turn to target folder
        s.setCurDb(new File(dbName));
        return "change to" + " "+this.tokens.get(1);
    }
}
