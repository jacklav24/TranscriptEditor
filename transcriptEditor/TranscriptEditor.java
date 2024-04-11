import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranscriptEditor {
    /**
     * Accepts a string and returns the tokens (words, paragraph breaks, sentence-ending punctuation).
     * 
     * @param input a string that needs to be tokenized
     * @return an ordered List of tokens
     */
    public static Object[] tokenizeString(String input) {
        List<String> results = new ArrayList<String>();

        // fix issues if any input documents use Windows line endings
        input = input.replaceAll("\\r\\n?", "\n");

        // reduce any instance of two or more newlines to only two newlines
        input = input.replaceAll("(\\n\\n+)", "\n\n");

        // use regular expressions to tokenize string
        // this regular expression has 6 options:
        // [a-zA-Z]+'?[a-zA-z]+ : a word of length >=2 or a contraction of length >= 3
        // [a-zA-z] : single-character alphanumeric words
        // [.] : a period
        // [?] : a question mark
        // [!] : an exclamation point
        // [\\n]{2,} : two newlines
        //  \\d+(?:st|nd|rd|th)? matches numbers followed optionally by ordinal suffixes (st, nd, rd, th).
        // \\d+ matches one or more digits.
        // (?:st|nd|rd|th)? is a non-capturing group that matches the ordinal suffixes optionally.
        // %? matches an optional percent sign %.
        Pattern pattern = Pattern.compile("(?<![():])\\b\\d+(?:st|nd|rd|th)?%?\\b(?![:)])|[a-zA-Z]+'?[a-zA-Z]+|[a-zA-Z]|[.]|[?]|[!]|[\\n]{2,}");

        Matcher matcher = pattern.matcher(input);
        
        // collect results
        while (matcher.find()) {
                results.add(matcher.group());
        }
        
       Object[] result = results.toArray();
        
        return result;
       
        
    }
    
    /**
     * Reads and returns all contents of a file.
     * 
     * @param filepath the filepath (including the filename) of the file to read
     * @return a string containing all contents of the file
     */
    public static String readFile(String filepath) {
        File f = new File(filepath);        
        Scanner scan = null;
        try { // attempt to open the file
            scan = new Scanner(f);
            StringBuilder sb = new StringBuilder();

            while (scan.hasNext()) { // while the file has more words append to the string builder
                sb.append(scan.next());
                sb.append(' '); // adding a space to separate words
            }
            // get string array with a single word per index
            // List<String> temp = tokenizeString(sb.toString());
            // return string array made into k length sequences of words at each index
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        finally {
            scan.close(); // close scanner
        }

        return null;
    }
    
    /**
     * Accepts a string and returns an array of the words in that string. The words
     * have been lower-cased and stripped of all non-alphabetic characters.
     * 
     * @param input The string from which you want the array of words
     * @return An array of all the alphabetic words in the input string
     */
    public static String[] getWordsFromString(String input) {
        return input.trim().replaceAll("[^a-zA-Z\\s]", "").toLowerCase().split("\\s+");
    }
    /**
     * Takes the array and resturns as one big string.
     * @param tokens - array of objects (strings)
     * @return - String
     */
    public static String returnStuff(Object[] tokens) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tokens.length; i++) {
            sb.append(tokens[i]);
            sb.append(' ');
        }
        return sb.toString();
    }
    
    
    public static String containsTimeFormat(String input) {
        // Define the regular expression pattern for the time format (##:##:##)
        Pattern pattern = Pattern.compile("\\(\\d{2}:\\d{2}:\\d{2}\\)");
        Pattern pattern2 = Pattern.compile("\\(\\d{2}:\\d{2}\\)");
        Matcher matcher = pattern.matcher(input);
        Matcher matcher2 = pattern2.matcher(input);
        // Check if the input string contains the time format
        int index;
        if (matcher.find()) {
            index = matcher.end();
            return input.substring(index);
        }
        else if (matcher2.find()) {
            index = matcher2.end();
            return input.substring(index);
        }
        return input;
    }
    
    
    public static String removeTimes(String input) {
        List<String> results = new ArrayList<String>();
        
        // fix issues if any input documents use Windows line endings
        input = input.replaceAll("\\r\\n?", "\n");

        // reduce any instance of two or more newlines to only two newlines
        input = input.replaceAll("(\\n\\n+)", "\n\n");
        
        String[] lines = input.split("\n");
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            line = containsTimeFormat(line);
            sb.append(line);
        }
        return sb.toString();
    }
}
