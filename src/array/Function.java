package array;

/**
 * Created by Vatsa on 1/14/2016.
 */

/**
 * Defines a Function interface for lambda expressions.
 */
public interface Function {

    /**
     * The body of the function to invoke.
     * @param data Whatever data the function takes.
     * @return Whatever object the function returns.
     */
    Object body(Object... data);

    /**
     * TODO:
     * Find a way to accept variable multiple objects without typecasting.
     */
}
