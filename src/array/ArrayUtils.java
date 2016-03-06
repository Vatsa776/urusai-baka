package array;

import exceptions.ArrayUtilsExceptions.ArgumentException;
import exceptions.ArrayUtilsExceptions.ArgumentExceptionTypes;
import exceptions.ArrayUtilsExceptions.ObjectListCastException;
import exceptions.ArrayUtilsExceptions.ZipFormatException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by vatsa on 06/01/16.
 */
public abstract class ArrayUtils {

    /**
     * Creates an ArrayList with all falsey values removed. The values false, null, 0, "", and NaN are falsey.
     *
     * @param arrayObject The array to compact.
     * @return An ArrayList of the compacted elements.
     * @throws ObjectListCastException
     */
    public static ArrayList<Object> compact(Object arrayObject) throws ObjectListCastException {
        List<Object> result;

        if (arrayObject instanceof List) {
            result = ((List<Object>) arrayObject);
        } else {
            result = toObjectArrayList(arrayObject);
        }

        Iterator<Object> it = result.iterator();

        while (it.hasNext()) {
            Object element = it.next();

            if (isFalsey(element)) {
                it.remove();
            }
        }

        return (ArrayList<Object>) result;
    }

    /**
     * A traditional zip function. It provides a simple way to group several lists together.
     * Ex: zip([1,2,3],["a","b","c"],[true,false]) = [[1,"a",true],[2,"b",false],[3,"c",null]]
     * You can also zip lists of uneven sizes; the result will contain null fillers, which
     * can be sanitized by specifying the first parameter as a boolean true.
     * <p>
     * Remember to sanitize the resultant list, or use {@link #zip(boolean, Object...)} with
     * true as the first value to sanitize the output. This can also be used to unzip a zipped object.
     * <p>
     * <b>SUPPORTS</b>: int[],char[],float[],boolean[],double[],String[],ArrayLists, and <?>[].
     *
     * @param data A variable number of arrays/ArrayLists of any type. First value may be boolean.
     * @return An ArrayList of the zipped ArrayLists.
     * @throws ZipFormatException
     */
    public static List<ArrayList<Object>> zip(Object... data) throws ZipFormatException {
        if (data[0] instanceof Boolean) {
            return zip((Boolean) data[0], data);
        }

        return zip(false, data);
    }

    /**
     * Internal implementation of the {@link #zip(Object...)} method.
     * The result is a sanitized list without any null fillers, if sanitize is true.
     *
     * @param sanitize Whether to return null fillers or not.
     * @param data     A variable number of arrays/ArrayLists of any type.
     * @return An ArrayList of the zipped ArrayLists.
     * @throws ZipFormatException
     */
    private static List<ArrayList<Object>> zip(boolean sanitize, Object... data) throws ZipFormatException {
        List<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
        List<ArrayList<Object>> sources = new ArrayList<ArrayList<Object>>();

        int maxSize = -1;
        int count = 0;


        for (Object datum : data) {
            if (count == 0) {
                if (datum instanceof Boolean) {
                    continue;
                }
            }

            count++;

            ArrayList<Object> tempData = new ArrayList<Object>();

            if (datum instanceof List) {
                sources.add((ArrayList<Object>) datum);
            }

            //Cleaned this up. I've forgotten my java lol

            else {

                if (datum.getClass().getComponentType().isPrimitive()) {
                    for (int i = 0; i < Array.getLength(datum); i++) {
                        tempData.add(Array.get(datum, i));
                    }

                } else if (datum.getClass().isArray()) {
                    for (int i = 0; i < ((Object[]) datum).length; i++) { //Collection copy messes up the data for some reason
                        tempData.add(((Object[]) datum)[i]);
                    }

                } else {
                    throw new ZipFormatException(datum, count);
                }

                sources.add(tempData);
            }
        }

        for (ArrayList<Object> source : sources) {
            if (source.size() > maxSize) {
                maxSize = source.size();
            }
        }

        for (int i = 0; i < maxSize; i++) {
            result.add(new ArrayList<Object>());
        }

        for (ArrayList<Object> source : sources) {
            int nullRemoval = (sanitize ? maxSize : source.size());

            for (int i = 0; i < maxSize; i++) {

                ArrayList<Object> temp = result.get(i);
                Object toAdd = null;

                if (i < nullRemoval)
                    toAdd = source.get(i);

                if (sanitize) {
                    if (toAdd != null) {
                        temp.add(toAdd);
                    }
                } else {
                    temp.add(toAdd);
                }
            }
        }

        return result;
    }

    /**
     * Internal method to convert primitive/non primitive arrays to Object arrays.
     *
     * @param arrayObject The array to be converted to Object[].
     * @return Object[] of the converted elements.
     * @throws ObjectListCastException
     */
    private static Object[] toObjectArray(Object arrayObject) throws ObjectListCastException {
        Class arrayClass = arrayObject.getClass().getComponentType();

        if (arrayClass.isPrimitive()) {
            List result = new ArrayList();
            int length = Array.getLength(arrayObject);

            for (int i = 0; i < length; i++) {
                result.add(Array.get(arrayObject, i));
            }
            return result.toArray();
        } else {
            return (Object[]) arrayObject;
        }
    }

    /**
     * Internal method to convert primitive/non primitive arrays to Object ArrayList.
     *
     * @param arrayObject The array to be converted to ArrayList.
     * @return ArrayList of the converted elements.
     * @throws ObjectListCastException
     */
    private static List<Object> toObjectArrayList(Object arrayObject) throws ObjectListCastException {
        Class arrayClass = arrayObject.getClass().getComponentType();

        if (arrayClass != null) {

                List result = new ArrayList();
                int length = Array.getLength(arrayObject);

                for (int i = 0; i < length; i++) {
                    result.add(Array.get(arrayObject, i));
                }
                return result;
        } else {
            throw new ObjectListCastException(arrayClass);
        }
    }

    /**
     * Checks for the values false, null, 0, "", and NaN.
     * INTERNAL ONLY.
     *
     * @param element The element to be checked for falseness.
     * @return true if element was falsey.
     */
    private static boolean isFalsey(Object element) {
        if (element == null) {
            return true;
        } else if (element.equals(false)) {
            return true;
        } else if (element instanceof String) {
            if (((String) element).isEmpty()) {
                return true;
            }
        } else if (element instanceof Integer) {
            if ((Integer) element == 0) {
                return true;
            }
        } else if (element instanceof Double || element instanceof Float) {
            try {
                if (((Double) element).isNaN()) {
                    return true;
                }
            } catch (ClassCastException e) {
                if (((Float) element).isNaN()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Method to break up an array/arraylist into smaller chunks, as specified by chunksize.
     *
     * @param input     An array/arraylist to be chunked.
     * @param chunkSize size to chunk it in.
     * @return A list of arraylists of the chunked array.
     * @throws ObjectListCastException
     */
    public static List<ArrayList<Object>> chunk(Object input, int chunkSize) throws ObjectListCastException {
        List<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
        int index = -1;

        List<Object> data = toObjectArrayList(input);

        for (int i = 0; i < data.size(); i++) {

            if (i % chunkSize == 0) {
                result.add(new ArrayList<Object>());
                index++;
            }

            result.get(index).add(data.get(i));
        }

        return result;
    }

    /**
     * Internal implementation for both {@link #drop(Object, int)} and {@link #dropRight(Object, int)}.
     *
     * @param input     the array to drop
     * @param dropSize  how many elements to drop
     * @param direction from which end to drop (left = true)
     * @return An arraylist of the remaining elements
     * @throws ObjectListCastException
     */
    private static List<Object> drop(Object input, int dropSize, boolean direction) throws ObjectListCastException {
        List<Object> result = new ArrayList<Object>();
        int index = -1;

        List<Object> data = toObjectArrayList(input);

        Iterator<Object> it = data.iterator();

        while (it.hasNext()) {
            it.next();

            index++;
            if (direction) {
                if (index < dropSize) {
                    it.remove();
                }
            } else {
                if (index > data.size() - dropSize) {
                    it.remove();
                }
            }
        }

        return data;
    }

    /**
     * Drops the first n elements of an Array.
     *
     * @param input    the array to drop
     * @param dropSize number of elements to drop from the left.
     * @return An arraylist of the edited array.
     * @throws ObjectListCastException
     */
    public static List<Object> drop(Object input, int dropSize) throws ObjectListCastException {
        return drop(input, dropSize, true);
    }

    /**
     * Drops the last n elements of an Array.
     *
     * @param input    the array to drop
     * @param dropSize number of elements to drop from the right.
     * @return An arraylist of the edited array.
     * @throws ObjectListCastException
     */
    public static List<Object> dropRight(Object input, int dropSize) throws ObjectListCastException {
        return drop(input, dropSize, false);
    }

    /**
     * Joins all the objects specified by arrayObject into a string, separated by the separator argument.
     *
     * @param arrayObject The array/list of objects to join
     * @param separator   The intended separator
     * @return separated string of objects
     * @throws ArgumentException
     */
    public static String join(Object arrayObject, String separator) throws ArgumentException {

        Class arrayClass = arrayObject.getClass().getComponentType();
        StringBuilder result = new StringBuilder();

        if (arrayClass != null) {
            if (arrayClass.isPrimitive() || arrayObject.getClass().isArray()) {
                int i = 0;

                for (; i < Array.getLength(arrayObject) - 1; i++) {

                    result.append(Array.get(arrayObject, i).toString());
                    result.append(separator);
                }

                result.append(Array.get(arrayObject, i));
            }
        } else if (arrayObject instanceof List) {

            Iterator it = ((List) arrayObject).iterator();
            Object obj = null;

            while (it.hasNext()) {
                obj = (Object) it.next();

                if (!it.hasNext()) {
                    break;
                }

                result.append(obj.toString());
                result.append(separator);
            }

            result.append(obj.toString());
        } else {
            throw new ArgumentException(arrayObject,ArgumentExceptionTypes.JOIN);
        }

        return result.toString();
    }

    /**
     * Filters the array/list based on the lambda passed to it. This method requires a <b>boolean</b> lambda.
     *
     * @param arrayObject
     * @param criteria
     * @return
     * @throws ObjectListCastException
     * @throws ArgumentException
     */
    public static List filter(Object arrayObject, Function criteria) throws ObjectListCastException, ArgumentException {

        Class arrayClass = arrayObject.getClass().getComponentType();
        List result = null;

        if (arrayClass != null) {
            if (arrayClass.isPrimitive() || arrayObject.getClass().isArray()) {
                result = toObjectArrayList(arrayObject);
            }
        } else if (arrayObject instanceof List) {
            result = (List) arrayObject;
        } else {
            throw new ArgumentException(arrayObject,ArgumentExceptionTypes.FILTER);
        }

        Iterator it = result.iterator();

        while (it.hasNext()) {

            Object obj = it.next();

            if ((boolean) criteria.body(obj)) {
                it.remove();
            }
        }

        return result;
    }

    /**
     * Internal implementation of the {@link #take(Object, int)}/{@link #takeRight(Object, int)} methods. Slices the arrayObject and returns n elements
     * from the left/right.
     *
     * @param arrayObject The array/list to slice
     * @param n           Number of elements to slice from the left or right
     * @param direction   Whether to slice from left or right
     * @return New ArrayList with <b>n</b> number of elements from the left/right.
     * @throws ObjectListCastException
     * @throws ArgumentException
     */
    private static List take(Object arrayObject, int n, boolean direction) throws ObjectListCastException, ArgumentException {

        Class arrayClass = arrayObject.getClass().getComponentType();
        List result = null;
        int counter = 0;

        if (arrayClass != null) {
            if (arrayClass.isPrimitive() || arrayObject.getClass().isArray()) {
                result = toObjectArrayList(arrayObject);
            }
        } else if (arrayObject instanceof List) {
            result = (List) arrayObject;
        } else {
            throw new ArgumentException(arrayObject,ArgumentExceptionTypes.TAKE);
        }

        Iterator it = result.iterator();

        if (direction) {
            while (it.hasNext() && counter != n) {
                counter++;
                it.next();
            }
            while (it.hasNext()) {
                it.next();
                it.remove();
            }
        } else {

            int length = result.size();
            while (it.hasNext() && counter != (length - n)) {
                counter++;
                it.next();
                it.remove();
            }
        }

        return result;
    }

    /**
     * Returns a slice of the array with n elements from the left.
     *
     * @param arrayObject The array/list to slice
     * @param n           Number of elements to slice from the left
     * @return New ArrayList with <b>n</b> number of elements from the left.
     * @throws ObjectListCastException
     * @throws ArgumentException
     */
    public static List take(Object arrayObject, int n) throws ObjectListCastException, ArgumentException {

        return take(arrayObject, n, true);
    }

    /**
     * Returns a slice of the array with n elements from the right.
     *
     * @param arrayObject The array/list to slice
     * @param n           Number of elements to slice from the right
     * @return New ArrayList with <b>n</b> number of elements from the left.
     * @throws ObjectListCastException
     * @throws ArgumentException
     */
    public static List takeRight(Object arrayObject, int n) throws ObjectListCastException, ArgumentException {

        return take(arrayObject, n, false);
    }

    /**
     * Reverses an array/list in place.
     *
     * @param arrayObject The array/list object to reverse
     * @return An ArrayList with the reversed objects
     * @throws ObjectListCastException
     * @throws ArgumentException
     */

    public static List reverse(Object arrayObject) throws ObjectListCastException, ArgumentException {

        Class arrayClass = arrayObject.getClass().getComponentType();
        List result = null;

        if (arrayClass != null) {
            if (arrayClass.isPrimitive() || arrayObject.getClass().isArray()) {
                result = toObjectArrayList(arrayObject);
            }
        } else if (arrayObject instanceof List) {
            result = (List) arrayObject;
        } else {
            throw new ArgumentException(arrayObject,ArgumentExceptionTypes.REVERSE);
        }

        for (int i = 0, j = result.size() - 1; i < j; i++) {
            result.add(i, result.remove(j));
        }

        return result;
    }

    /**
     * Takes any number of arrays/arrayLists and returns an arrayList with all unique objects.
     * <b>WILL NOT PRESERVE ELEMENT ORDER.</b>
     * @param arrayObjects any number of arrays/lists to unionize.
     * @return a list of all elements with no duplicates.
     * @throws ObjectListCastException
     * @throws ArgumentException
     */
    public static List union(Object... arrayObjects) throws ObjectListCastException, ArgumentException {

        Set elements = new HashSet();

        for(Object arrayObject : arrayObjects) {

            Class arrayClass = arrayObject.getClass().getComponentType();
            List result = null;

            if (arrayClass != null) {
                if (arrayClass.isPrimitive() || arrayObject.getClass().isArray()) {
                    result = toObjectArrayList(arrayObject);
                }
            } else if (arrayObject instanceof List) {
                result = (List) arrayObject;
            } else {
                throw new ArgumentException(arrayObject,ArgumentExceptionTypes.UNION);
            }

            Iterator it = result.iterator();

            while(it.hasNext()) {
                elements.add(it.next());
            }
        }

        return new ArrayList(elements);
    }
}


