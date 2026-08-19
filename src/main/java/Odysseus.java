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

    private String[] tasks = new String[100];
    private Integer count = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean chatting = true;

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
            } else {
                System.out.println(String.format(MSG_FORMAT, input));
            }
        }
    }
}
