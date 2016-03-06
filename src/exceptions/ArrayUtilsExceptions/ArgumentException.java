package exceptions.ArrayUtilsExceptions;

/**
 * Created by vatsa on 06/03/16.
 */
public class ArgumentException extends Exception {

    String message;

    public ArgumentException()
    {
        message = "Invalid argument passed to method.";
    }

    public ArgumentException(Object obj,ArgumentExceptionTypes type)
    {
        message = "Invalid argument " + obj.getClass() + " passed to "+type+" method.";
    }

    @Override
    public String toString() {
        return message;
    }
}
