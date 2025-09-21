package sigmabot;

/**
 * Thrown to indicate that a line in the save file could not be decoded into a valid Task.
 * <p>
 * This exception is used by the decodeSaveFormat methods in Task classes to signal
 * malformed or invalid data when reading from the save file.
 * </p>
 */
public class SigmaBotReadSaveException extends Exception {
    /**
     * Constructs a new SigmaBotReadSaveException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public SigmaBotReadSaveException(String message) {
        super(message);
    }
}