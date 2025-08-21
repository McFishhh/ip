import java.util.Scanner;

public class SigmaBot {

    public SigmaBot() { 
    }

    public static String echo(Scanner scanner) {
        String msg = scanner.nextLine();
        
        return msg;
    }

    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String greeting = "\tHello! I'm SigmaBot\r\n" + "\tWhat can I do for you?\r\n";
        String goodbye = "\tBye. Hope to see you again soon!\r\n";
        String sep = "\t____________________________________________________________\r\n";
        
        System.out.println(sep + greeting + sep);
    
        String msg = echo(scanner);
        while (!msg.toLowerCase().equals("bye")) {
            System.out.println(sep + "\t" + msg + "\r\n" + sep);
            msg = echo(scanner);
        } 

        System.out.println(sep + goodbye + sep);
    }
}
