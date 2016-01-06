package array;

import exceptions.ArrayUtilsExceptions.ZipFormatException;

import java.util.ArrayList;

/**
 * Created by vatsa on 06/01/16.
 */
public class ArrayUtils {

    /**
     * A traditional zip function. It provides a simple way to group several lists together.
     * Ex: zip([1,2,3],["a","b","c"],[true,false]) = [[1,"a",true],[2,"b",false],[3,"c",null]]
     * You can also zip lists of uneven sizes; the result will contain null fillers.
     * <p>
     * Remember to sanitize the resultant list, or use {@link #zip(boolean, Object...)} to sanitize the output.
     * <p>
     * SUPPORTS: int[],char[],float[],boolean[],double[],String[],ArrayLists, and <?>[].
     * <p>
     * @param data A variable number of arrays/ArrayLists of any type.
     * @return An ArrayList of the zipped ArrayLists.
     */
    public static ArrayList<ArrayList<Object>> zip(Object... data)
    {
        return zip(false,data);
    }

    /**
     * A sanitized zip function. It provides a simple way to group several lists together.
     * Ex: zip([1,2,3],["a","b","c"],[true,false]) = [[1,"a",true],[2,"b",false],[3,"c"]]
     * You can also zip lists of uneven sizes; the result will contain uneven lists.
     * <p>
     * The result is a sanitized list without any null fillers.
     * <p>
     * SUPPORTS: int[],char[],float[],boolean[],double[],String[],ArrayLists, and <?>[].
     * <p>
     * @param data A variable number of arrays/ArrayLists of any type.
     * @return An ArrayList of the zipped ArrayLists.
     */
    public static ArrayList<ArrayList<Object>> zip(boolean sanitize, Object... data)
    {
        ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
        ArrayList<ArrayList<Object>> sources = new ArrayList<ArrayList<Object>>();

        int maxSize = -1;
        int count = 0;


        for(Object datum : data)
        {
            count++;

            try {

                if (datum instanceof ArrayList) {
                    sources.add((ArrayList<Object>) datum);
                }

                //Arrays.asList((<T[]>)datum) says incompatible bounds, have to resort to this fuckery
                //grumble grumble

                else if (datum instanceof int[]) {
                    ArrayList<Object> tempData = new ArrayList<Object>();

                    for (int i = 0; i < ((int[]) datum).length; i++) {
                        tempData.add(((int[]) datum)[i]);
                    }

                    sources.add(tempData);
                } else if (datum instanceof char[]) {
                    ArrayList<Object> tempData = new ArrayList<Object>();

                    for (int i = 0; i < ((char[]) datum).length; i++) {
                        tempData.add(((char[]) datum)[i]);
                    }

                    sources.add(tempData);
                } else if (datum instanceof float[]) {
                    ArrayList<Object> tempData = new ArrayList<Object>();

                    for (int i = 0; i < ((float[]) datum).length; i++) {
                        tempData.add(((float[]) datum)[i]);
                    }

                    sources.add(tempData);
                } else if (datum instanceof double[]) {
                    ArrayList<Object> tempData = new ArrayList<Object>();

                    for (int i = 0; i < ((double[]) datum).length; i++) {
                        tempData.add(((double[]) datum)[i]);
                    }

                    sources.add(tempData);
                } else if(datum instanceof boolean[]) {
                    ArrayList<Object> tempData = new ArrayList<Object>();

                    for(int i=0; i<((boolean[])datum).length; i++) {
                        tempData.add(((boolean[])datum)[i]);
                    }

                    sources.add(tempData);
                } else if (datum.getClass().isArray()) {
                    ArrayList<Object> tempData = new ArrayList<Object>();

                    for (int i = 0; i < ((Object[]) datum).length; i++) {
                        tempData.add(((Object[]) datum)[i]);
                    }

                    sources.add(tempData);
                } else {
                    throw new ZipFormatException(datum, count);
                }
            }

            catch (ZipFormatException e)
            {
                System.out.println(e);
            }
        }

        for(ArrayList<Object> source : sources)
        {
            if(source.size() > maxSize)
            {
                maxSize = source.size();
            }
        }

        for(int i=0; i<maxSize; i++)
        {
            result.add(new ArrayList<Object>());
        }

        for(ArrayList<Object> source : sources)
        {
            int nullRemoval = (sanitize ? maxSize : source.size());

            for(int i=0; i < maxSize; i++)
            {
                try
                {
                    ArrayList<Object> temp = result.get(i);
                    Object toAdd = null;

                    if( i < nullRemoval)
                        toAdd = source.get(i);

                    temp.add(toAdd);
                }

                catch(Exception e)
                {

                }
            }
        }

        return result;
    }
}
