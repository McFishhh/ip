import java.util.Scanner;

public class SigmaBot {
    private String[] todo = new String[100]; 
    private int numItems = 0;

    public SigmaBot() { 
    }

    public String[] addItem(String item) {
        todo[numItems] = item;
        numItems += 1;

        return todo;
    }

    public String[] getTodo() {
        return this.todo;
    } 

    public int getNumItems() {
        return this.numItems;
    } 

    public String echo(Scanner scanner) {
        String msg = scanner.nextLine();
        
        return msg;
    }

    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SigmaBot bot = new SigmaBot();

        String greeting = "\tHello! I'm SigmaBot\r\n" + "\tWhat can I do for you?\r\n";
        String goodbye = "\tBye. Hope to see you again soon!\r\n";
        String sep = "\t____________________________________________________________\r\n";
        
        System.out.println(sep + greeting + sep);
    
        String msg = bot.echo(scanner);
        while (!msg.toLowerCase().equals("bye")) {
            if (msg.toLowerCase().equals("list")) {
                System.out.print(sep);
                for (int i = 0; i < bot.getNumItems(); i += 1) {
                    System.out.println("\t" + String.valueOf(i + 1) + ". " + bot.todo[i]);
                }       
                System.out.println(sep);
            } else {
                bot.addItem(msg);
                System.out.println(sep + "\t added: " + msg + "\r\n" + sep);
            }

            msg = bot.echo(scanner);
        } 

        System.out.println(sep + goodbye + sep);
    }
}
