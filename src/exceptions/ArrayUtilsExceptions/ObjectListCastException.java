package exceptions.ArrayUtilsExceptions;

/**
 * Created by vatsa on 13/01/16.
 */
public class ObjectListCastException extends Exception {

    String message;

    public ObjectListCastException()
    {
        message = "Failed to cast the object.";
    }

    public ObjectListCastException(Object obj)
    {
        message = "Failed to cast object " + obj.getClass() + " to an object array.";
    }

    @Override
    public String toString() {
        return message;
    }
}
