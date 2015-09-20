package com.freshplanet.ane.AirFacebook.utils;

import android.os.Bundle;
import com.adobe.fre.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * Created by nodrock on 30/06/15.
 */
public class FREConversionUtil {

    public static final int TYPE_STRING = 0;
    public static final int TYPE_INT = 1;
    public static final int TYPE_BOOL = 2;

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

    public static FREArray fromStringSet(Set<String> value) {

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

    public static Double toDouble(FREObject object){
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

    public static ArrayList<String> toStringArray(FREObject object){
        ArrayList<String> result = new ArrayList<>();

        try
        {
            FREArray array = (FREArray)object;

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

    public static Bundle toStringBundle(FREObject object)
    {
        try
        {
            FREArray keys = (FREArray) object.getProperty("keys");
            FREArray values = (FREArray) object.getProperty("values");
            return toStringBundle(keys, values);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static Bundle toStringBundle(FREArray keys, FREArray values)
    {
        Bundle result = new Bundle();

        try
        {
            long keysLength = keys.getLength();
            long valuesLength = values.getLength();
            if(keysLength != valuesLength)
            {
                throw new Error("Wrong input arrays length!");
            }
            for (long i = 0; i < keysLength; i++)
            {
                String key = FREConversionUtil.toString(keys.getObjectAt(i));
                String value = FREConversionUtil.toString(values.getObjectAt(i));
                result.putString(key, value);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    public static Bundle toBundle(FREObject object)
    {
        try
        {
            FREArray keys = (FREArray) object.getProperty("keys");
            FREArray types = (FREArray) object.getProperty("types");
            FREArray values = (FREArray) object.getProperty("values");
            return toBundle(keys, types, values);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static Bundle toBundle(FREArray keys, FREArray types, FREArray values)
    {
        Bundle result = new Bundle();

        try
        {
            long length = keys.getLength();

            if(length != types.getLength() || length != values.getLength())
            {
                throw new Error("Wrong input arrays length!");
            }

            for (long i = 0; i < length; i++) {

                String key = keys.getObjectAt(i).getAsString();
                int type = types.getObjectAt(i).getAsInt();
                FREObject valueObject = values.getObjectAt(i);
                switch (type){
                    case TYPE_STRING:
                        result.putString(key, valueObject.getAsString());
                        break;
                    case TYPE_INT:
                        result.putInt(key, valueObject.getAsInt());
                        break;
                    case TYPE_BOOL:
                        result.putBoolean(key, valueObject.getAsBool());
                        break;
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
