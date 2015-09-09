package com.freshplanet.ane.AirFacebook.utils;

import com.adobe.fre.*;
import com.freshplanet.ane.AirFacebook.AirFacebookExtension;

import java.util.ArrayList;
import java.util.List;

public class ValueContainer {

    List<String> keys;
    List<ConversionType> types;
    List<Object> values;

    public ValueContainer() {
        keys = new ArrayList<>();
        types = new ArrayList<>();
        values = new ArrayList<>();
    }

    private void addValue(String key, ConversionType type, FREObject valueObject) throws FREInvalidObjectException, FRETypeMismatchException, FREWrongThreadException {
        AirFacebookExtension.log("addValue " + key + " " + type);

        Object value;
        switch (type){
            case STRING:
                value = valueObject.getAsString();
                break;
            case INT:
                value = valueObject.getAsInt();
                break;
            case BOOL:
                value = valueObject.getAsBool();
                break;
            case DOUBLE:
                value = valueObject.getAsDouble();
                break;
            case LONG:
                value = (long) valueObject.getAsDouble();
                break;
            case STRING_ARRAY:
                value = FREConversionUtil.toStringArray((FREArray)valueObject);
                break;
            case INT_ARRAY:
                value = FREConversionUtil.toIntegerArray((FREArray) valueObject);
                break;
            case BOOL_ARRAY:
                value = FREConversionUtil.toBoolArray((FREArray) valueObject);
                break;
            case DOUBLE_ARRAY:
                value = FREConversionUtil.toDoubleArray((FREArray) valueObject);
                break;
            case LONG_ARRAY:
                value = FREConversionUtil.toLongArray((FREArray) valueObject);
                break;
            case OBJECT:
                value = getValueContainer(valueObject);
                break;
            case OBJECT_ARRAY:
                value = toValueObjectArray((FREArray) valueObject);
                break;
            default:
                return;
        }

        keys.add(key);
        types.add(type);
        values.add(value);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[ValueContainer ");
        for(int i = 0; i<keys.size(); i++){

            String key = keys.get(i);
            ConversionType type = types.get(i);

            builder.append(key);
            builder.append("(").append(type).append(")=");
            builder.append(values.get(i));
            builder.append(" ");
        }
        builder.append("]");
        return builder.toString();
    }

    public static List<ValueContainer> toValueObjectArray(FREArray array){
        List<ValueContainer> result = new ArrayList<>();

        try
        {
            for (Integer i = 0; i < array.getLength(); i++)
            {
                try
                {
                    FREObject object = array.getObjectAt(i);
                    result.add(getValueContainer(object));
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

    public static ValueContainer getValueContainer(FREObject object)
    {
        try
        {
            ValueContainer result = new ValueContainer();

            FREArray keys = (FREArray)FREConversionUtil.getProperty("keys", object);
            FREArray types = (FREArray)FREConversionUtil.getProperty("types", object);
            FREArray values = (FREArray)FREConversionUtil.getProperty("values", object);

            AirFacebookExtension.log("getValueContainer " + keys + " " + types + " " + values);

            long length = keys.getLength();

            if(length != types.getLength() || length != values.getLength()){
                throw new Error("Wrong input arrays length!");
            }

            for (long i = 0; i < length; i++) {
                try {
                    String key = FREConversionUtil.toString(keys.getObjectAt(i));
                    int valueType = FREConversionUtil.toInt(types.getObjectAt(i));
                    ConversionType conversionType = ConversionType.fromValue(valueType);

                    FREObject valueObject = values.getObjectAt(i);
                    result.addValue(key, conversionType, valueObject);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
