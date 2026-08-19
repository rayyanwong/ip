public class Odysseus {
    private static final String NAME = "Odysseus";
    public static void main(String[] args) {
        String str = String.format("""
        ____________________________________________________________
        Hello! I'm %s
        What can I do for you?
        ____________________________________________________________
        Bye. Hope to see you again soon!
        ____________________________________________________________
        """, NAME);
        System.out.println(str);
    }
}
