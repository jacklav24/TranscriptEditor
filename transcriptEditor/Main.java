import java.util.Scanner;



public class Main {
    public static final int[] INDICATOR = {0,1};
    
    public static void main(String[] args) {
        
        
        
        Scanner scan = new Scanner(System.in);
        
        
        System.out.println("Hi! Welcome to my transcript editor!\n");
        
        boolean files = (InputValidation.queryIntFromOptions(INDICATOR, scan, 
                "Enter 1 if you want read from a file, and 0 if you want to paste text into the console: ", "Enter 0 or 1: ")== 1) ? true : false;
        // query filename from scanner.
        
        boolean generateContent = (InputValidation.queryIntFromOptions(INDICATOR, scan, 
                "Enter 1 if you want to generate a passage, and 0 if you want to simple return the edited text: ", "Enter 0 or 1: ")== 1) ? true : false;
        
        
        if (generateContent) {
            
            System.out.println("Ok, we'll generate some content!");
        
            int size = InputValidation.queryInt(scan, "How many words per key? That is, how many words in a row"
                    + " should the model use to train? : ", "Enter a valid int.");
            
            while (!(size > 0)) { // ensure size is not negative
                size = InputValidation.queryInt(scan, "Enter a positive integer: ", "Enter a valid int.");
            }
            int length = InputValidation.queryInt(scan, "How many words minimum? (more than words per key): ", 
            "Enter a valid int");
            
            while (length <= size) { // ensure length is > size (k)
                length = InputValidation.queryInt(scan, "Needs to be more than words per key please. Enter again:  ", 
                        "Enter a valid int");
            }
            
            if (files) {
                PassageGenerator.getFiles(scan, size);
            }
            else {
                PassageGenerator.getStrings(scan, size);
            }
            // get novel in style of writer.
            String passage = PassageGenerator.generatePassage(length, size);
            if (passage == null) {
                System.out.println("Oops, your passage couldn't be generated!!");
            }
            else {
                System.out.println(passage);
            }
        
        }
        
        else {
            System.out.println("Cleaning input...");
            
            String current = TranscriptEditor.readStringInput(scan);
            
            
            current  = TranscriptEditor.removeTimes(current);
            System.out.println();
            System.out.println("Tokenizing!");
            System.out.println();
            Object[] tokenizedInput = TranscriptEditor.tokenizeString(current);
            
            System.out.println("Here's your cleaned Transcript!");
            System.out.println(TranscriptEditor.returnStuff(tokenizedInput));
        }
        
        
        scan.close();
        
        
    }
}
