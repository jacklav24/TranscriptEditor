import java.util.Arrays;
import java.util.Scanner;



public class Main {
    public static final int[] INDICATOR = {0,1};
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Hi! Welcome to my transcript editor!\n");
        boolean printAttributes = (InputValidation.queryIntFromOptions(INDICATOR, scan, 
                "Enter 1 if you want read from a file, and 0 if you want to paste text into the console. ", "Enter 0 or 1: ")== 1) ? true : false;
        // query filename from scanner.
        String current;
        if (printAttributes) {
            String fileName = InputValidation.queryString(scan, "Give the fileName in format ./filename.txt: ", "Must be a string: ");
            current = TranscriptEditor.readFile(fileName);
        }
        else {
            StringBuilder sb = new StringBuilder();
            System.out.println("Type in or past your text. Type -999 followed by a new line to stop!");
            while (true) {
                String input = scan.nextLine();
                if (input.equals("-999")) {
                    break;
                }
                // Do something with the input if needed
                sb.append(input);
                
            }
            current = sb.toString();
        }
        current  = TranscriptEditor.removeTimes(current);
        System.out.println(current + "\nhahaha");
        Object[] tokenizedInput = TranscriptEditor.tokenizeString(current);
        System.out.println("Printing!");
        System.out.println(TranscriptEditor.returnStuff(tokenizedInput));
        
        scan.close(); // close scanner
        
    }
}
