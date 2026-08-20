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

    private static final String MARK_MSG = "Nice! I've marked this task as done:%n  %s";
    private static final String UNMARK_MSG = "OK, I've marked this task as not done yet:%n  %s";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean chatting = true;
        Task[] tasks = new Task[100];
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
            String command = input.split(" ")[0];
            switch (command.toLowerCase()) {

                case "bye" -> {
                    chatting = false;
                    System.out.println(BYE_MSG);
                }

                case "list" -> {
                    // list out tasks
                    StringBuilder sb = new StringBuilder();
                    if (count == 0) {
                        System.out.println(String.format(MSG_FORMAT, "No tasks available"));
                    } else {
                        for (int i = 0; i < count; i++) {
                            sb.append((i + 1) + ". " + tasks[i].toString() + "\n");
                        }
                        System.out.println(String.format(MSG_FORMAT, sb.toString()));
                    }
                }

                case "mark" -> {
                    // TODO: error handling for invalid index
                    int idx = Integer.parseInt(input.split(" ")[1]) - 1;
                    tasks[idx].markAsDone();
                    String msg = String.format(MARK_MSG, tasks[idx]);
                    System.out.println(String.format(MSG_FORMAT, msg));
                }

                case "unmark" -> {
                    // TODO: error handling for invalid index
                    int idx = Integer.parseInt(input.split(" ")[1]) - 1;
                    tasks[idx].markAsUndone();
                    String msg = String.format(UNMARK_MSG, tasks[idx]);
                    System.out.println(String.format(MSG_FORMAT, msg));
                }

                default -> {
                    Task newTask = new Task(input);
                    tasks[count] = newTask;
                    count++;
                    String addedMsg = String.format("added: %s", newTask.toString());
                    System.out.println(String.format(MSG_FORMAT, addedMsg));
                }
            }
        }
    }
}
