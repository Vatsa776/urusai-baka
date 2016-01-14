package exceptions.ArrayUtilsExceptions;

/**
 * Created by Vatsa on 1/14/2016.
 */
public class FilterArgumentException extends Exception {

    String message;

    public FilterArgumentException()
    {
        message = "Invalid argument passed to filter method.";
    }

    public FilterArgumentException(Object obj)
    {
        message = "Invalid argument " + obj.getClass() + " passed to filter method.";
    }

    @Override
    public String toString() {
        return message;
    }
}
