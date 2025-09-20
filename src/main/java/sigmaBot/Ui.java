package sigmabot;
import java.util.Scanner;

public class Ui {
    private final Scanner scanner;
    private String input;
    private final Parser parser;

    public static final String GREETING = "Hello! I'm SigmaBot\r\n" + 
                                            "What can I do for you?\r\n";
    public static final String GOODBYE = "Bye. Hope to see you again soon!\r\n";

    /**
     * Constructs a Ui object and initializes the scanner and parser.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
        this.parser = new Parser();
    }

    /**
     * Reads the next line of user input, validates it, and returns it.
     *
     * @return the next valid user input string
     */
    public String nextInput() {
        while (true) {
            String rawInput = scanner.nextLine();
            this.input = rawInput.trim().toLowerCase();
            parser.setInput(this.input);

            if (parser.isValidTask() || parser.isValidAction()) {
                break;
            }
            
            System.out.println("Hey! that doesn't make any sense!\n");
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
