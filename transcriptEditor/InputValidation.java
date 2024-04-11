import java.util.Scanner;

/**
 * Library class of functions that perform text-based input validation. Input is read from Scanner objects, which are
 * not stored and must be passed into each function.
 */
public class InputValidation {
    /**
     * The string to add after the prompt, so the cursor moves forward some number of spaces.
     */
    private static final String AFTER_PROMPT_SPACE = " ";

    /**
     * Determines whether a given String is made up entirely of digits with an optional '-' in the first position.
     * 
     * @param s the string whose parse-ability is in question
     * @return true if s is made up entirely of digits, with an optional '-' in the first position, false otherwise
     */
    public static boolean canParseInt(String s) {
        // handle empty strings or strings with a single '-' and no digit
        boolean emptyString = s.length() == 0;
        boolean dashOnly = "-".equals(s);
        if (emptyString || dashOnly) {
            return false;
        }

        // check every char of s
        for (int i = 0; i < s.length(); i++) {
            // is this character a digit or a negative sign? if not, we cannot parse. return false.
            boolean isDigit = Character.isDigit(s.charAt(i));
            boolean isNegative = i == 0 && s.charAt(i) == '-';
            if (!isDigit && !isNegative) {
                return false;
            }
        }

        // every digit must either be a - or a digit, so it can be considered an int.
        return true;
    }

    /**
     * Determines whether a given String is made up entirely of digits with an optional '-' in the first position and up
     * to 1 .
     * 
     * @param s the string whose parse-ability is in question
     * @return true if s made up of digits, with optional '-' in the first position and up to 1 ., false otherwise.
     */
    public static boolean canParseDouble(String s) {
        boolean emptyString = s.length() == 0;
        boolean dashOnly = "-".equals(s);
        int countPts = 0;
        if (emptyString || dashOnly) {
            return false;
        }

        // check every char of s
        for (int i = 0; i < s.length(); i++) {
            // is this character a digit or . or a negative sign? if not, we cannot parse. return false.
            boolean isDigit = Character.isDigit(s.charAt(i));
            boolean isNegative = i == 0 && s.charAt(i) == '-';
            boolean isDecimalPoint = s.charAt(i) == '.' && countPts < 1;
            if (!isDigit && !isNegative && !isDecimalPoint) {
                return false;
            } else if (isDecimalPoint) {
                countPts++;
            }
        }
        return true;
    }

    /**
     * Determines whether a given String is length 1.
     * 
     * @param s the string whose parse-ability is in question
     * @return true if s is length 1, false otherwise.
     */
    public static boolean canParseChar(String s) {
        if (s.length() == 1) {
            return true;
        }

        return false;
    }

    /**
     * Prompts a user for an integer value and ensures that the user provides viable input. Caller must provide the
     * initial user prompt message (e.g., "Please enter an integer.") and the user re-prompt message (e.g., "That wasn't
     * an integer. Please try again.").
     * 
     * @param scnr The buffer from which to pull the input.
     * @param promptMessage The initial message the function should use to request the desired input from the user.
     * @param repromptMessage The message to be used to request input when their previous input is not parse-able.
     * @return a user-provided int
     */
    public static int queryInt(Scanner scnr, String promptMessage, String repromptMessage) {

        System.out.print(promptMessage + AFTER_PROMPT_SPACE);
        String response = scnr.nextLine();

        while (!canParseInt(response)) {
            System.out.print(repromptMessage + AFTER_PROMPT_SPACE);
            response = scnr.nextLine();
        }

        int queriedInt = Integer.parseInt(response);

        return queriedInt;
    }
    
    /**
     * Prompts a user for a positive integer value and ensures that the user provides viable input. Caller must provide the
     * initial user prompt message (e.g., "Please enter an integer.") and the user re-prompt message (e.g., "That wasn't
     * an integer. Please try again.").
     * 
     * @param scnr The buffer from which to pull the input.
     * @param promptMessage The initial message the function should use to request the desired input from the user.
     * @param repromptMessage The message to be used to request input when their previous input is not parse-able.
     * @return a user-provided int
     */
    public static int queryPositiveInt(Scanner scnr, String promptMessage, String repromptMessage) {

        
        // get an int from the user
        int queriedInt = queryInt(scnr, promptMessage, repromptMessage);

        while (queriedInt <= 0) {
            System.out.print(repromptMessage + AFTER_PROMPT_SPACE);
            queriedInt = queryInt(scnr, repromptMessage, repromptMessage);
        }

        return queriedInt;
    }

    /**
     * Requests an integer from the console user. Prompts a user for an integer value and ensures that the user provides
     * viable input. Caller must provide the initial user prompt message (e.g., "Please enter an integer.") and the user
     * re-prompt message (e.g., "That wasn't an integer. Please try again."). Additionally, this function ensures that
     * the user's int is included in an array of acceptable values. If their input is not in the list, the user will be
     * reprompted.
     * 
     * 
     * @param options The list of acceptable int values (any user inputted int not in this list will be rejected)
     * @param scnr The buffer from which to pull the input.
     * @param promptMessage The initial message the function should use to request the desired input from the user.
     * @param repromptMessage The message to be used to request input when their previous input is not parse-able.
     * @return a user-provided int
     */
    public static int queryIntFromOptions(int[] options, Scanner scnr, String promptMessage, String repromptMessage) {
        // get an int from the user
        int queriedInt = queryInt(scnr, promptMessage, repromptMessage);

        // now we have an int, but is it one of the ints in our options array?
        while (!valueIsInArray(options, queriedInt)) {
            // we want both messages to be the re-prompt message, since every prompt from this point is a reprompt
            queriedInt = queryInt(scnr, repromptMessage, repromptMessage);
        }

        return queriedInt;
    }

    /**
     * Prompts a user for an double value and ensures that the user provides viable input. Caller must provide the
     * initial user prompt message (e.g., "Please enter a double.") and the user re-prompt message (e.g., "That wasn't a
     * double. Please try again.").
     * 
     * @param scnr The buffer from which to pull the input.
     * @param promptMessage The initial message the function should use to request the desired input from the user.
     * @param repromptMessage The message to be used to request input when their previous input is not parse-able.
     * @return a user-provided double
     */
    public static double queryDouble(Scanner scnr, String promptMessage, String repromptMessage) {
        System.out.print(promptMessage + AFTER_PROMPT_SPACE);
        String response = scnr.nextLine();

        while (!canParseDouble(response)) {
            System.out.print(repromptMessage + AFTER_PROMPT_SPACE);
            response = scnr.nextLine();
        }

        double queriedDouble = Double.parseDouble(response);

        return queriedDouble;
    }

    /**
     * Requests an double from the console user. Prompts a user for an double value and ensures that the user provides
     * viable input. Caller must provide the initial user prompt message (e.g., "Please enter a double.") and the user
     * re-prompt message (e.g., "That wasn't a double. Please try again."). Additionally, this function ensures that the
     * user's double is included in an array of acceptable values. If their input is not in the list, the user will be
     * reprompted.
     * 
     * 
     * @param options The list of acceptable double values (any user inputted double not in this list will be rejected)
     * @param scnr The buffer from which to pull the input.
     * @param promptMessage The initial message the function should use to request the desired input from the user.
     * @param repromptMessage The message to be used to request input when their previous input is not parse-able.
     * @return a user-provided double
     */

    public static double queryDoubleFromOptions(double[] options, Scanner scnr, String promptMessage,
            String repromptMessage) {
        double queriedDouble = queryDouble(scnr, promptMessage, repromptMessage);

        // now we have an int, but is it one of the ints in our options array?
        while (!valueIsInArray(options, queriedDouble)) {
            // we want both messages to be the re-prompt message, since every prompt from this point is a reprompt
            queriedDouble = queryDouble(scnr, repromptMessage, repromptMessage);
        }

        return queriedDouble;
    }

    /**
     * Prompts a user for an char value and ensures that the user provides viable input. Caller must provide the initial
     * user prompt message (e.g., "Please enter a char.") and the user re-prompt message (e.g., "That wasn't a char.
     * Please try again.").
     * 
     * @param scnr The buffer from which to pull the input.
     * @param promptMessage The initial message the function should use to request the desired input from the user.
     * @param repromptMessage The message to be used to request input when their previous input is not parse-able.
     * @return a user-provided char
     */

    public static char queryChar(Scanner scnr, String promptMessage, String repromptMessage) {
        System.out.print(promptMessage + AFTER_PROMPT_SPACE);
        String response = scnr.nextLine();

        while (!canParseChar(response)) {
            System.out.print(repromptMessage + AFTER_PROMPT_SPACE);
            response = scnr.nextLine();
        }

        char queriedChar = response.charAt(0);

        return queriedChar;
    }

    /**
     * Requests a char from the console user. Prompts a user for a char value and ensures that the user provides viable
     * input. Caller must provide the initial user prompt message (e.g., "Please enter a char.") and the user re-prompt
     * message (e.g., "That wasn't a char. Please try again."). Additionally, this function ensures that the user's char
     * is included in an array of acceptable values. If their input is not in the list, the user will be reprompted.
     * 
     * 
     * @param options The list of acceptable char values (any user inputted char not in this list will be rejected)
     * @param scnr The buffer from which to pull the input.
     * @param promptMessage The initial message the function should use to request the desired input from the user.
     * @param repromptMessage The message to be used to request input when their previous input is not parse-able.
     * @return a user-provided char
     */

    public static char queryCharFromOptions(char[] options, Scanner scnr, String promptMessage,
            String repromptMessage) {
        // get an char from the user
        char queriedChar = queryChar(scnr, promptMessage, repromptMessage);

        // now we have an char, but is it one of the chars in our options array?
        while (!valueIsInArray(options, queriedChar)) {
            // we want both messages to be the re-prompt message, since every prompt from this point is a reprompt
            queriedChar = queryChar(scnr, repromptMessage, repromptMessage);
        }

        return queriedChar;
    }

    /**
     * Prompts a user for a string value and ensures that the user provides viable input. Caller must provide the
     * initial user prompt message (e.g., "Please enter a string.") and the user re-prompt message (e.g., "That wasn't a
     * string. Please try again.").
     * 
     * @param scnr The buffer from which to pull the input.
     * @param promptMessage The initial message the function should use to request the desired input from the user.
     * @param repromptMessage The message to be used to request input when their previous input is not parse-able.
     * @return a user-provided string
     */

    public static String queryString(Scanner scnr, String promptMessage, String repromptMessage) {

        System.out.print(promptMessage + AFTER_PROMPT_SPACE);
        String response = scnr.nextLine();

        return response;
    }

    /**
     * Requests a string from the console user. Prompts a user for a string value and ensures that the user provides
     * viable input. Caller must provide the initial user prompt message (e.g., "Please enter a string.") and the user
     * re-prompt message (e.g., "That wasn't a string. Please try again."). Additionally, this function ensures that the
     * user's string is included in an array of acceptable values. If their input is not in the list, the user will be
     * reprompted.
     * 
     * 
     * @param options The list of acceptable string values (any user inputted string not in this list will be rejected)
     * @param scnr The buffer from which to pull the input.
     * @param promptMessage The initial message the function should use to request the desired input from the user.
     * @param repromptMessage The message to be used to request input when their previous input is not parse-able.
     * @return a user-provided string
     */

    public static String queryStringFromOptions(String[] options, Scanner scnr, String promptMessage,
            String repromptMessage) {
        System.out.print(promptMessage + AFTER_PROMPT_SPACE);
        String response = scnr.nextLine();
        while (!valueIsInArray(options, response)) {
            // we want both messages to be the re-prompt message, since every prompt from this point is a reprompt
            response = queryString(scnr, repromptMessage, repromptMessage);
        }
        return response;
    }

    /**
     * Searches an array to determine if a given value is included. Private because function is helpful internally, but
     * does not relate to the library's public area of responsibility.
     * 
     * @param haystack The array to search
     * @param needle The value to search for
     * @return true if the value is in the array, false otherwise
     */
    private static boolean valueIsInArray(int[] haystack, int needle) {
        // check needle for equivalence to every value in the haystack
        for (int i = 0; i < haystack.length; i++) {
            if (haystack[i] == needle) {
                return true;
            }
        }
        return false;
    }

    /**
     * Searches an array to determine if a given value is included. Private because function is helpful internally, but
     * does not relate to the library's public area of responsibility.
     * 
     * @param haystack The array to search
     * @param needle The value to search for
     * @return true if the value is in the array, false otherwise
     */
    private static boolean valueIsInArray(double[] haystack, double needle) {
        for (int i = 0; i < haystack.length; i++) {
            if (haystack[i] == needle) {
                return true;
            }
        }
        return false;
    }

    /**
     * Searches an array to determine if a given value is included. Private because function is helpful internally, but
     * does not relate to the library's public area of responsibility.
     * 
     * @param haystack The array to search
     * @param needle The value to search for
     * @return true if the value is in the array, false otherwise
     */

    private static boolean valueIsInArray(char[] haystack, char needle) {
        for (int i = 0; i < haystack.length; i++) {
            if (haystack[i] == needle) {
                return true;
            }
        }
        return false;
    }

    /**
     * Searches an array to determine if a given value is included. Private because function is helpful internally, but
     * does not relate to the library's public area of responsibility.
     * 
     * @param haystack The array to search
     * @param needle The value to search for
     * @return true if the value is in the array, false otherwise
     */

    private static boolean valueIsInArray(String[] haystack, String needle) {
        for (int i = 0; i < haystack.length; i++) {
            if (haystack[i].equals(needle)) {
                return true;
            }
        }
        return false;
    }

    /**
     * main method.
     * 
     * @param args - takes no arguments.
     */
    public static void main(String[] args) {
        // test these static methods
        Scanner scnr = new Scanner(System.in);

        // get an int from the user
        int queriedInt = queryInt(scnr, "Please enter an integer:",
                "I'm sorry, I couldn't parse that. Please enter an integer:");
        System.out.println("The int queried from options is: " + queriedInt);
        System.out.println("\n\n");

        // get an int from the user, where that int is included in a set of options
        int[] options = { 3, 4, 6 };
        int queriedIntFromOptions = queryIntFromOptions(options, scnr, "int please (3, 4, or 6):",
                "that wasn't 3, 4, or 6. try again. int please:");
        System.out.println("The int queried from options is: " + queriedIntFromOptions);

        double queriedDouble = queryDouble(scnr, "Please enter a double:",
                "I'm sorry, I couldn't parse that. Please enter a double:");
        System.out.println("The double queried from options is: " + queriedDouble);
        System.out.println("\n\n");

        // get an int from the user, where that int is included in a set of options
        double[] optionsD = { 3.0, 4.0, 6.0 };
        double queriedDoubleFromOptions = queryDoubleFromOptions(optionsD, scnr, "double please (3.0, 4.0, or 6.0):",
                "that wasn't 3.0, 4.0, or 6.0. try again. double please:");
        System.out.println("The double queried from options is: " + queriedDoubleFromOptions);

        char queriedChar = queryChar(scnr, "Please enter a char:",
                "I'm sorry, I couldn't parse that. Please enter a char:");
        System.out.println("The char queried from options is: " + queriedChar);
        System.out.println("\n\n");

        // get an int from the user, where that int is included in a set of options
        char[] optionsC = { 'a', 'b', 'c' };
        char queriedCharFromOptions = queryCharFromOptions(optionsC, scnr, "char please (a, b, or c):",
                "that wasn't 3, 4, or 6. try again. char please:");
        System.out.println("The char queried from options is: " + queriedCharFromOptions);

        String queriedString = queryString(scnr, "Please enter a string:",
                "I'm sorry, I couldn't parse that. Please enter an String:");
        System.out.println("The string queried from options is: " + queriedString);
        System.out.println("\n\n");

        // get an int from the user, where that int is included in a set of options
        String[] optionsS = { "Jack", "Daniel", "Val" };

        String queriedStringFromOptions = queryStringFromOptions(optionsS, scnr,
                "string please (Jack, Daniel, or Val):", "that wasn't Jack, Daniel, or Val. try again. String please:");
        System.out.println("The string queried from options is: " + queriedStringFromOptions);
    }

}

