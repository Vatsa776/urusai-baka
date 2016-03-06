package exceptions.ArrayUtilsExceptions;

/**
 * Created by vatsa on 06/03/16.
 */
public class ReverseArgumentException extends Exception {

    String message;

    public ReverseArgumentException()
    {
        message = "Invalid argument passed to reverse method.";
    }

    public ReverseArgumentException(Object obj)
    {
        message = "Invalid argument " + obj.getClass() + " passed to reverse method.";
    }

    @Override
    public String toString() {
        return message;
    }
}
