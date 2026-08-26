package odysseus;

/** An error caused by invalid user input to odysseus.Odysseus */
public class OdysseusException extends Exception {
    public OdysseusException(String msg) {
        super(msg);
    }
}
