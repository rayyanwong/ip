import java.util.List;
import java.util.Scanner;

public class Ui {

    protected final String name;
    protected final Scanner scanner = new Scanner(System.in);

    private static final String BYE_MSG = "Bye. Hope to see you again soon!";
    private static final String MSG_FORMAT = """
        ____________________________________________________________
        %s
        ____________________________________________________________
        """;

    public Ui() {
        this.name = "Odysseus";
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showWelcome() {
       String str = String.format("""
           ___      _                             \s
          / _ \\  __| |_   _ ___ ___  ___ _   _ ___\s
         | | | |/ _` | | | / __/ __|/ _ \\ | | / __|
         | |_| | (_| | |_| \\__ \\__ \\  __/ |_| \\__ \\
          \\___/ \\__,_|\\__, |___/___/\\___|\\__,_|___/
                      |___/                       \s
        ____________________________________________________________
        Hello! I'm %s
        What can I do for you?
        ____________________________________________________________
        """, this.name);
       System.out.println(str);
   }

   public void show(String msg) {
       System.out.println(String.format(MSG_FORMAT, msg));
   }

   public void showGoodbye() {
       this.show(BYE_MSG);
   }

   public void showError(Exception e) {
       System.out.println(String.format(MSG_FORMAT, e.getMessage()));
   }

   public void showTasks(List<Task> list) {
       if (list.isEmpty())  {
           show("No tasks available");
       } else {
           StringBuilder sb = new StringBuilder();
           for (int i = 0; i < list.size(); i++) {
               sb.append((i + 1) + ". " + list.get(i) + "\n");
           }
           show(sb.toString());
       }
   }

}
