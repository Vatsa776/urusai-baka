package array;

import exceptions.ArrayUtilsExceptions.ZipFormatException;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.*;
import java.lang.reflect.Array;

/**
 * Created by vatsa on 06/01/16.
 */
public abstract class ArrayUtils {

    /**
     * Creates an ArrayList with all falsey values removed. The values false, null, 0, "", and NaN are falsey.
     *
     * @param arrayObject The array to compact.
     * @return An ArrayList of the compacted elements.
     */
    public static ArrayList<Object> compact(Object arrayObject) {
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
     */
    public static List<ArrayList<Object>> zip(Object... data) {
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
     */
    private static List<ArrayList<Object>> zip(boolean sanitize, Object... data) {
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

            try {

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
            } catch (ZipFormatException e) {
                System.out.println(e);
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
                try {
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

                } catch (Exception e) {

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
     */
    private static Object[] toObjectArray(Object arrayObject) {
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
     */
    private static List<Object> toObjectArrayList(Object arrayObject) {
        Class arrayClass = arrayObject.getClass().getComponentType();

        if (arrayClass.isPrimitive()) {
            List result = new ArrayList();
            int length = Array.getLength(arrayObject);

            for (int i = 0; i < length; i++) {
                result.add(Array.get(arrayObject, i));
            }
            return result;
        } else {
            return Arrays.asList((Object[]) arrayObject);
        }
    }

    /**
     * Checks for the values false, null, 0, "", and NaN.
     *
     * @param element The element to be checked for falseness.
     * @return true if element was falsey.
     */
    private static boolean isFalsey(Object element) {
        if (element == null) {
            return true;
        }

        if (element.equals(false)) {
            return true;
        }

        if (element instanceof String) {
            if (((String) element).isEmpty()) {
                return true;
            }
        }

        if (element instanceof Integer) {
            if ((Integer) element == 0) {
                return true;
            }
        }

        if (element instanceof Double || element instanceof Float) {

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
     */
    public static List<ArrayList<Object>> chunk(Object input, int chunkSize) {
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
     * @param input the array to drop
     * @param dropSize how many elements to drop
     * @param direction from which end to drop (left = true)
     * @return An arraylist of the remaining elements
     */
    private static List<Object> drop(Object input, int dropSize, boolean direction) {
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
     */
    public static List<Object> drop(Object input, int dropSize) {
        return drop(input, dropSize, true);
    }

    /**
     * Drops the last n elements of an Array.
     *
     * @param input    the array to drop
     * @param dropSize number of elements to drop from the right.
     * @return An arraylist of the edited array.
     */
    public static List<Object> dropRight(Object input, int dropSize) {
        return drop(input, dropSize, false);
    }
}
