package odysseus.ui;

import java.util.Scanner;

/**
 * Handles all user interaction: reading commands from standard input
 * and printing formatted output to the CLI.
 */
public class Ui {

    protected final String name;
    protected final Scanner scanner = new Scanner(System.in);

    private static final String MSG_FORMAT = """
            ____________________________________________________________
            %s
            ____________________________________________________________
            """;

    /**
     * Constructs the Ui instance.
     */
    public Ui() {
        this.name = "Odysseus";
    }

    /**
     * Reads the next line of user input from standard input.
     *
     * @return the raw command line entered by the user
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Prints welcome banner and introduction.
     */
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

    /**
     * Prints the message to CLI.
     *
     * @param msg message to show
     */
    public void show(String msg) {
        System.out.println(String.format(MSG_FORMAT, msg));
    }

    /**
     * Prints the error message to CLI.
     *
     * @param e exception which message is to be shown
     */
    public void showError(Exception e) {
        System.out.println(String.format(MSG_FORMAT, e.getMessage()));
    }
}
