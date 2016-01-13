package exceptions.ArrayUtilsExceptions;

/**
 * Created by vatsa on 13/01/16.
 */
public class JoinArgumentException extends Exception {

    String message;

    public JoinArgumentException()
    {
        message = "Invalid argument passed to join method.";
    }

    public JoinArgumentException(Object obj)
    {
        message = "Invalid argument " + obj.getClass() + " passed to join method.";
    }

    @Override
    public String toString() {
        return message;
    }
}
