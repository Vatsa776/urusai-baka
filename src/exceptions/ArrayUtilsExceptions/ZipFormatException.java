package exceptions.ArrayUtilsExceptions;

/**
 * Created by vatsa on 06/01/16.
 */
public class ZipFormatException extends Exception {

    private Object incompatible;
    private int index;
    private String error;

    public ZipFormatException()
    {
        error = "GENERIC: Whoops. Something went wrong.";
    }

    public ZipFormatException(Object incompatible, int index)
    {
        setIncompatible(incompatible);
        setIndex(index);

        error = "The Object " + this.incompatible.getClass() + " at location "+ this.index +" is not an array/ArrayList.";
    }

    public void setIncompatible(Object incompatible)
    {
        this.incompatible = incompatible;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    @Override
    public String toString()
    {
        return this.error;
    }
}
