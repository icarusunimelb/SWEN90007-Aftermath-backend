package exceptions;

public class CanNotAcquireLockException extends Exception{
    public CanNotAcquireLockException(String errorMessage) {
        super(errorMessage);
    }
}
