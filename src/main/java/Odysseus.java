import java.util.Scanner;

public class Odysseus {
    private static final String NAME = "Odysseus";

    private static final String MSG_FORMAT = """
        ____________________________________________________________
        %s
        ____________________________________________________________
        """;

    private static final String BYE_MSG = """
        ____________________________________________________________
        Bye. Hope to see you again soon!
        ____________________________________________________________
        """;


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean chatting = true;
        String[] tasks = new String[100];
        int count = 0;

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
        """, NAME);
        System.out.println(str);

        while (chatting) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("bye")) {
                chatting = false;
                System.out.println(BYE_MSG);
            } else if (input.equalsIgnoreCase("list")) {
                // list out tasks
                StringBuilder sb = new StringBuilder();
                if (count == 0) {
                    System.out.println(String.format(MSG_FORMAT, "No tasks available"));
                } else {
                    for (int i = 0; i < count; i++) {
                        sb.append((i + 1) + ". " + tasks[i] + "\n");
                    }
                    System.out.println(String.format(MSG_FORMAT, sb.toString()));
                }
            } else {
                // add task
                tasks[count] = input;
                count++;
                String addedMsg = String.format("added: %s", input);
                System.out.println(String.format(MSG_FORMAT, addedMsg));
            }
        }
    }
}
