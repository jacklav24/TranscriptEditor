packa
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




/**
 * Automatically generates passage based on provided inspiration. Originally an in class assignment.
 * 
 * @author Stephanie Valentine and Jack LaVergne
 */
public class NovelGenerator {
    
    private static Map<String, List<String>> model;

    
    /**
     * Reads and returns all contents of a file.
     * 
     * @param filepath the filepath (including the filename) of the file to read
     * @return a string containing all contents of the file
     */
    private static String readFile(String filepath) {
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
     * Accepts a string and returns the tokens (words, paragraph breaks, sentence-ending punctuation).
     * 
     * @param input a string that needs to be tokenized
     * @return an ordered List of tokens
     */
    public static List<String> tokenizeString(String input) {
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
        Pattern pattern = Pattern.compile("(?<![():])\\b\\d+\\b(?![:])|[a-zA-Z]+'?[a-zA-Z]+|"
                + "[a-zA-Z]|[.]|[?]|[!]|[\\n]{2,}");
        Matcher matcher = pattern.matcher(input);

        // collect results
        while (matcher.find()) {
            results.add(matcher.group());
        }
        
       
        
        return results;
       
    }
    
    /**
     * Adds length * 2 extra elements as a workaround for the fenceposts. Ensures if a key starts with the 2nd-to-last
     * word, a full value will still be present after it.
     * @param results - list of words.
     * @param length - length of list elements.
     */
    public static void addDuplicateElements(List<String> results, int length) {
        int count = 0;
        for (int i = 0; i <= length * 2; i++) {
            results.add(results.get(i));
            count++;
        }
        
    }

    /**
     * Learns an author's style. Returns a map with each unique word in the input as a key. The map values are Lists of
     * the tokens that follow the key in the text.
     * 
     * @param input an ordered list of tokens
     */
    private static void trainModel(List<String> input, int length) {      
        List<String> tempList = new ArrayList<>(); // creates a list to hold values
        
        // instantiates model map
        if (model == null) {
            model = new HashMap<String, List<String>>();
        }
        
        
        String key;
        String value;
        // for each element in the list up to the original end
        for (int i = 0; i < input.size() - length; i++) {
            // get key
            key = input.get(i);
            // get value
            value = input.get(i + length);
            
            if (model.containsKey(key)) {
                // get list
                tempList = model.get(key);  
            }
            else {
                // create list
                tempList = new ArrayList<String>();  
            }
            // add value to list
            tempList.add(value);
            // add key value pair to map
            model.put(key, tempList);
        }      
    }
    
    

    /**
     * Reads a file, tokenizes it, and uses the tokens to train the NovelGenerator model.
     * 
     * @param filepath the filepath (including the filename) of the file from which to learn
     */
    public static void learnFromFile(String filepath, int length) {
        String words = readFile(filepath);
        
        List<String> tokens = tokenizeString(words);
        if (tokens.size() < length) {
            return;
        }
        // add elements to account for fencposting and lengths. Must be length * 2 to account for if
        addDuplicateElements(tokens, length);
        // return results in lengths k.
        tokens = getSequencesStringList(tokens, length);
        
       
        trainModel(tokens, length);
    }
    
    /**
     * puts together a list of string sequences of length k.
     * 
     * @param big list of individual words in a file.
     * @param length k words to be stored in each index.
     * @return string array of sequences of length k.
     */
    public static List<String> getSequencesStringList(List<String> bigString, int k) {
        StringBuilder sB = new StringBuilder();
        char space = ' ';
        // create result list
        List<String> result = null;
        // if the entered string is longer than k
        if (bigString.size() >= k) {
            // result is an arraylist
            result = new ArrayList<String>();
            // for each element in the initial list - k
            for (int i = 0; i < bigString.size() - k; i++) {

                for (int j = 0; j < k; j++) { // loop k times for each i to add k words to each index of result
                    // append each word and a space to string builder
                    sB.append(bigString.get(i + j));
                    sB.append(space);
                }
                result.add(sB.toString()); // add built string to current index
                sB.setLength(0); // clear string builder for next loop.
            }
        } 
        return result;
    }

    /**
     * Gets the training model (convenience method for use by unit testing functions).
     * 
     * @return the model
     */
    public static Map<String, List<String>> getModel() {
        return model;
    }
    
    /**
     * Sets the model as null for testing purposes.
     */
    public static void setModelNull() {
        model = null;
    }

    /**
     * Generates a novel with approximately as many tokens as the requested length. The novel may be longer than the
     * requested length (it will finish its last sentence), but it will never be shorter.
     * 
     * @param length a requested minimum number of tokens of the auto-generated novel
     * @return an auto-generated novel
     */
    public static String generateNovel(int size, int length) {
        
        
        
        

        // instantiate stringBuilder
        StringBuilder sb = new StringBuilder();
        
        // set first value to startKey
        String current = getStartingPoint();
        if (current == null) {
            return null;
        }
        
        sb.append(current);
        
        int wordCount = 1;
        
        while (wordCount < size) { // while word count is below the minimum add more words
            current = getRandomResult(current);
            sb.append(current);
            wordCount += length; // increase by length, since current will be a string of length amount of words
        }
        
        // if not ended with a punctuation, continue until ending with punctuation.
        while ((!current.endsWith("! ") && !current.endsWith("? ") && !current.endsWith(". "))) {
            current = getRandomResult(current);
            sb.append(current);
   
            }
        return sb.toString();
    }
    
    /**
     * Randomly gets a starting spot where the first letter of the token is capital.
     * @param start - initial key
     * @return - result , starting key for novel.
     */
    public static String getStartingPoint() {
        if (model == null) {
            return null;
        }
        // get all keys
        Set<String> keySet = model.keySet();
        // get a key
        String temp = keySet.iterator().next();
        
        // loop through random result 3 times to make key more random.
        for (int i = 0; i <= 2; i++) {
            temp = getRandomResult(temp);  
        }
        
        char firstChar = temp.charAt(0);
        while (!Character.isUpperCase(firstChar)) { // keep looping until the first char of temp is uppercase.
            temp = getRandomResult(temp);
            firstChar = temp.charAt(0);
        }
        String result = temp;
        return result; // return new starting key.
    }
    
    /**
     * Gets a random value from the key value pair with s as the key.
     * @param s - string key s.
     * @return - string value.
     */
    public static String getRandomResult(String s) {
        List<String> valueOptions = model.get(s); // gets potential key options
        
        int option = (int) (Math.random() * valueOptions.size()); // chooses an integer representing the option.
        
        return valueOptions.get(option);
    }

    /**
     * Main function that reads files, generates models, etc.
     * 
     * @param args does not accept any args.
     */
    public static void main(String[] args) {
        
        Scanner scan = new Scanner(System.in);
        
       
        
        
        int size = InputValidation.queryInt(scan, "How many words per key? : ", "Enter a valid int.");
        
        while (!(size > 0)) { // ensure size is not negative
            size = InputValidation.queryInt(scan, "Enter a positive integer: ", "Enter a valid int.");
        }
        int length = InputValidation.queryInt(scan, "How many words minimum? (more than words per key): ", 
        "Enter a valid int");
        
        while (length <= size) { // ensure length is > size (k)
            length = InputValidation.queryInt(scan, "Needs to be more than words per key please. Enter again:  ", 
                    "Enter a valid int");
        }
        
        learnFromFile("./Tell-Tale.txt", size);
        learnFromFile("./Pooh.txt", size);
        learnFromFile("./Trump.txt", size);
        learnFromFile("./Biden.txt", size);
        learnFromFile("./French.txt", size);

        // get novel in style of writer.
        String novel = generateNovel(length, size);
        if (novel == null) {
            System.out.println("Oops, your novel couldn't be generated!!");
        }
        else {
            System.out.println(novel);
        }
        
        
        scan.close();
    }
}
