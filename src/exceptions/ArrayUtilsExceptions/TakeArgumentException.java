package exceptions.ArrayUtilsExceptions;

/**
 * Created by Vatsa on 3/6/2016.
 */
public class TakeArgumentException extends Exception {

    String message;

    public TakeArgumentException()
    {
        message = "Invalid argument passed to take method.";
    }

    public TakeArgumentException(Object obj)
    {
        message = "Invalid argument " + obj.getClass() + " passed to take method.";
    }

    @Override
    public String toString() {
        return message;
    }
}
