package exceptions.ArrayUtilsExceptions;

/**
 * Created by vatsa on 06/03/16.
 */
public class UnionArgumentException extends Exception {

    String message;

    public UnionArgumentException()
    {
        message = "Invalid argument passed to union method.";
    }

    public UnionArgumentException(Object obj)
    {
        message = "Invalid argument " + obj.getClass() + " passed to union method.";
    }

    @Override
    public String toString() {
        return message;
    }
}
