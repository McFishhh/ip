import java.util.Scanner;

public class Ui {
    private Scanner scanner;
    private String input;
    private String inputFirstWord;

    public static final String GREETING = "Hello! I'm SigmaBot\r\n" + 
                                            "What can I do for you?\r\n";
    public static final String GOODBYE = "Bye. Hope to see you again soon!\r\n";
    public static final String SEP = "____________________________________________________________\r\n";


    public Ui() {
        scanner = new Scanner(System.in);
    }

    public String nextInput() {
        input = scanner.nextLine().trim().toLowerCase();
        inputFirstWord = input.split(" ")[0];
        
        while (!isValidTask() && !isValidAction()) {
            System.out.println(SEP + "Hey! that doesnt make any sense!\n" + SEP);
            input = scanner.nextLine().trim().toLowerCase();
            inputFirstWord = input.split(" ")[0];
        }
            
        return input;
    }

    public String getInput() {
        return input;
    }

    public boolean isTodoTask() {
        return inputFirstWord.equals("todo");
    }

    public boolean isDeadlineTask() {
        return inputFirstWord.equals("deadline");
    }

    public boolean isEventTask() {
        return inputFirstWord.equals("event");
    }

    public boolean isList() {
        return input.equals("list");
    }    
    
    public boolean isBye() {
        return input.equals("bye");
    }

    public boolean isMark() {
        return inputFirstWord.equals("mark");
    }

    public boolean isUnmark() {
        return inputFirstWord.equals("unmark");
    }

    public boolean isDelete() {
        return inputFirstWord.equals("delete");
    }

    public boolean isValidTask() {
        return isTodoTask() || isDeadlineTask() || isEventTask();
    }

    public boolean isValidAction() {
        return isList() || isBye() || isMark() || isUnmark() || isDelete();
    }

}
