package exceptions;

public class RecordNotExistException extends  Exception{
    public RecordNotExistException(String errorMessage) {
        super(errorMessage);
    }
}
