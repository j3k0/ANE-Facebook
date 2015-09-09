package com.freshplanet.ane.AirFacebook.utils;

import com.adobe.fre.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by nodrock on 30/06/15.
 */
public class FREConversionUtil {

    public static FREObject fromString(String value){
        try
        {
            return FREObject.newObject(value);
        }
        catch (FREWrongThreadException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static FREObject fromNumber(Number value){
        try
        {
            return FREObject.newObject(value.doubleValue());
        }
        catch (FREWrongThreadException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static FREObject fromInt(Integer value){
        try
        {
            return FREObject.newObject(value);
        }
        catch (FREWrongThreadException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static FREObject fromBoolean(Boolean value){
        try
        {
            return FREObject.newObject(value);
        }
        catch (FREWrongThreadException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static FREArray fromStringArray(Collection<String> value)
    {
        try
        {
            long i = 0;
            FREArray array = FREArray.newArray(value.size());
            for (String item : value) {
                array.setObjectAt(i, FREObject.newObject(item));
                i++;
            }
            return array;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static String toString(FREObject object){
        try
        {
            return object.getAsString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static Number toNumber(FREObject object){
        try
        {
            return object.getAsDouble();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static Integer toInt(FREObject object){
        try
        {
            return object.getAsInt();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static Boolean toBoolean(FREObject object){
        try
        {
            return object.getAsBool();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> toStringArray(FREArray array){
        List<String> result = new ArrayList<>();

        try
        {
            for (Integer i = 0; i < array.getLength(); i++)
            {
                try
                {
                    result.add(FREConversionUtil.toString(array.getObjectAt(i)));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    public static <T> List<T> toList(Class<T> type, FREArray array){
        List<T> result = new ArrayList<>();

        try
        {
            for (Integer i = 0; i < array.getLength(); i++)
            {
                FREObject object = array.getObjectAt(i);

                if(String.class.isAssignableFrom(type)){
                    result.add((T)object.getAsString());
                }else if(Integer.class.isAssignableFrom(type)){
                    result.add((T)(Integer)object.getAsInt());
                }else if(Boolean.class.isAssignableFrom(type)){
                    result.add((T)(Boolean)object.getAsBool());
                }else if(Double.class.isAssignableFrom(type)){
                    result.add((T)(Double)object.getAsDouble());
                }else if(Long.class.isAssignableFrom(type)){
                    result.add((T)(Long)((Double)object.getAsDouble()).longValue());
                }else{
                    return null;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    public static List<Integer> toIntegerArray(FREArray array){
        List<Integer> result = new ArrayList<>();

        try
        {
            for (Integer i = 0; i < array.getLength(); i++)
            {
                try
                {
                    result.add(FREConversionUtil.toInt(array.getObjectAt(i)));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    public static List<Boolean> toBoolArray(FREArray array){
        List<Boolean> result = new ArrayList<>();

        try
        {
            for (Integer i = 0; i < array.getLength(); i++)
            {
                try
                {
                    result.add(FREConversionUtil.toBoolean(array.getObjectAt(i)));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    public static List<Double> toDoubleArray(FREArray array){
        List<Double> result = new ArrayList<>();

        try
        {
            for (Integer i = 0; i < array.getLength(); i++)
            {
                try
                {
                    result.add(FREConversionUtil.toNumber(array.getObjectAt(i)).doubleValue());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    public static List<Long> toLongArray(FREArray array){
        List<Long> result = new ArrayList<>();

        try
        {
            for (Integer i = 0; i < array.getLength(); i++)
            {
                try
                {
                    result.add(FREConversionUtil.toNumber(array.getObjectAt(i)).longValue());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    public static FREObject getProperty(String name, FREObject object){
        try
        {
            return object.getProperty(name);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static Long getArrayLength(FREArray array){
        try
        {
            return array.getLength();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static FREObject getArrayItemAt(Long index, FREArray array){
        try
        {
            return array.getObjectAt(index);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
