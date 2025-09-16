package sigmaBot;

/**
 * Custom exception class for SigmaBot application errors.
 * Extends RuntimeException to handle application-specific error conditions.
 */
public class SigmaBotException extends RuntimeException {
    
    /**
     * Constructs a new SigmaBotException with the specified error message.
     *
     * @param msg the detail message explaining the cause of the exception
     */
    public SigmaBotException(String msg) {
        super(msg);
    }
}
