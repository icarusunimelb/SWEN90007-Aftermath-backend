package exceptions;

public class NoSuchMapperTypeException extends Exception{
    public NoSuchMapperTypeException(String errorMessage) {
        super(errorMessage);
    }
}
