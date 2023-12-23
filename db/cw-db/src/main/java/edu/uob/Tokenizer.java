package edu.uob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tokenizer {
    String[] specialCharacters = {"(",")",",",";",">","<",">=","<=","!=","==",".","'","="};
    private String command;
    List<String> tokens;
    public Tokenizer(String command){
        this.command=command;
    }

    public List<String> tokenOutput(){
        this.tokens=new ArrayList<>();
        try {
            checkEmpty(command);
            cutHeadTail(command);
            String[] fragments = command.split("'");
            for (int i=0; i<fragments.length; i++) {
                // Every odd fragment is a string literal, so just append it without any alterations
                if (i % 2 != 0) {
                    tokens.add("'" + fragments[i] + "'");
                }else{
                    String[] nextBatchOfTokens = tokenise(fragments[i]);
                    // Then add these to the "result" array list (needs a bit of conversion)
                    tokens.addAll(Arrays.asList(nextBatchOfTokens));
                }
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return tokens;
    }

    public void cutHeadTail(String command){
        command.trim();
    }

    public void checkEmpty(String command) throws Exception {
        if(command.length()==0){
            throw new Exception("Query is empty");
        }
    }


    String[] tokenise(String input)
    {
        // Add in some extra padding spaces around the "special characters"
        // so we can be sure that they are separated by AT LEAST one space (possibly more)
        for(int i=0; i<specialCharacters.length ;i++) {
            input = input.replace(specialCharacters[i], " " + specialCharacters[i] + " ");
        }
        // Remove all double spaces (the previous replacements may had added some)
        // This is "blind" replacement - replacing if they exist, doing nothing if they don't
        while (input.contains("  ")) input = input.replaceAll("  ", " ");
        // Again, remove any whitespace from the beginning and end that might have been introduced
        input = input.trim();
        // Finally split on the space char (since there will now ALWAYS be a space between tokens)
        return input.split(" ");
    }


}
