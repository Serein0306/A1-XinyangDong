/**
 * The outcome of an operation that the clinic asked the system to perform.
 *
 * <p>Adding or cancelling an appointment can fail for several different reasons
 * (a double booking, a time outside the timetable, a professional who is not
 * working that day, an appointment that does not exist). Instead of printing a
 * message from deep inside the manager, the manager returns one of these
 * objects. The caller then decides what to do with it, which keeps the manager
 * free of console output and makes the result easy to test and to print in a
 * consistent way.</p>
 *
 * <p>The object is immutable: the two factory methods are the only way to
 * create one.</p>
 *
 * @author  Xinyang Dong (25258567)
 * @version 1.0
 */
public class OperationResult {

    /** Whether the operation was carried out. */
    private final boolean successful;

    /** The message describing what happened. */
    private final String message;

    /**
     * Creates a result. Private, so that a result can only be made through
     * {@link #success(String)} or {@link #failure(String)}.
     *
     * @param successful whether the operation was carried out
     * @param message    the message describing what happened
     */
    private OperationResult(boolean successful, String message) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("An operation result needs a message.");
        }
        this.successful = successful;
        this.message = message;
    }

    /**
     * Creates a result for an operation that was carried out.
     *
     * @param message what was done
     * @return a successful result
     */
    public static OperationResult success(String message) {
        return new OperationResult(true, message);
    }

    /**
     * Creates a result for an operation that could not be carried out.
     *
     * @param message why the operation was refused
     * @return a failed result
     */
    public static OperationResult failure(String message) {
        return new OperationResult(false, message);
    }

    /**
     * Returns whether the operation was carried out.
     *
     * @return true if the operation succeeded
     */
    public boolean isSuccessful() {
        return successful;
    }

    /**
     * Returns the message describing what happened.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the message with a marker showing whether it succeeded, so that a
     * result can be printed directly and the outcome is obvious at a glance.
     *
     * @return the marked message
     */
    @Override
    public String toString() {
        return (successful ? "[ OK ]      " : "[ REFUSED ] ") + message;
    }
}
