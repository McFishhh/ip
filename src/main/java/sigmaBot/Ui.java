package sigmaBot;
import java.util.Scanner;

public class Ui {
    private Scanner scanner;
    private String input;
    private Parser parser;

    public static final String GREETING = "Hello! I'm SigmaBot\r\n" + 
                                            "What can I do for you?\r\n";
    public static final String GOODBYE = "Bye. Hope to see you again soon!\r\n";
    public static final String SEP = "____________________________________________________________\r\n";


    /**
     * Constructs a Ui object and initializes the scanner and parser.
     */
    public Ui() {
        scanner = new Scanner(System.in);
        parser = new Parser();
    }

    /**
     * Reads the next line of user input, validates it, and returns it.
     *
     * @return the next valid user input string
     */
    public String nextInput() {
        this.input = scanner.nextLine().trim().toLowerCase();
        parser.setInput(this.input);
        
        while (!parser.isValidTask() && !parser.isValidAction()) {
            System.out.println(SEP + "Hey! that doesnt make any sense!\n" + SEP);
            this.input = scanner.nextLine().trim().toLowerCase();
            parser.setInput(this.input);
        }
            
        return input;
    }

    /**
     * Returns the most recent user input string.
     *
     * @return the most recent user input string
     */
    public String getInput() {
        return input;
    }
}
