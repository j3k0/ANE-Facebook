package com.freshplanet.ane.AirFacebook.utils;

import android.text.TextUtils;
import com.adobe.fre.*;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.model.ShareOpenGraphValueContainer;
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

    public ShareOpenGraphAction toOpenGraphAction()
    {
        ShareOpenGraphAction.Builder builder = new ShareOpenGraphAction.Builder();
        addValuesToBuilder(builder);
        return builder.build();
    }

    private ShareOpenGraphObject toOpenGraphObject()
    {
        ShareOpenGraphObject.Builder builder = new ShareOpenGraphObject.Builder();
        addValuesToBuilder(builder);
        return builder.build();
    }

    private <T extends ShareOpenGraphValueContainer.Builder> void addValuesToBuilder(T builder)
    {
        for(int i = 0; i<keys.size(); i++){

            String key = keys.get(i);
            ConversionType type = types.get(i);
            switch (type){
                case STRING:
                    builder.putString(key, (String)values.get(i));
                    break;
                case INT:
                    builder.putInt(key, (Integer) values.get(i));
                    break;
                case BOOL:
                    builder.putBoolean(key, (Boolean) values.get(i));
                    break;
                case DOUBLE:
                    builder.putDouble(key, (Double) values.get(i));
                    break;
                case LONG:
                    builder.putLong(key, (Long) values.get(i));
                    break;
                case STRING_ARRAY:
                    builder.putStringArrayList(key, (ArrayList<String>) values.get(i));
                    break;
                case INT_ARRAY:
                {
                    List<Integer> list = (List<Integer>)values.get(i);
                    int[] primitiveList = new int[list.size()];
                    for(int j = 0; j<list.size(); j++){
                        primitiveList[j] = list.get(j);
                    }
                    builder.putIntArray(key, primitiveList);
                }
                break;
                case BOOL_ARRAY:
                {
                    List<Boolean> list = (List<Boolean>) values.get(i);
                    boolean[] primitiveList = new boolean[list.size()];
                    for (int j = 0; j < list.size(); j++) {
                        primitiveList[j] = list.get(j);
                    }
                    builder.putBooleanArray(key, primitiveList);
                }
                break;
                case DOUBLE_ARRAY:
                {
                    List<Double> list = (List<Double>) values.get(i);
                    double[] primitiveList = new double[list.size()];
                    for (int j = 0; j < list.size(); j++) {
                        primitiveList[j] = list.get(j);
                    }
                    builder.putDoubleArray(key, primitiveList);
                }
                break;
                case LONG_ARRAY:
                {
                    List<Long> list = (List<Long>) values.get(i);
                    long[] primitiveList = new long[list.size()];
                    for (int j = 0; j < list.size(); j++) {
                        primitiveList[j] = list.get(j);
                    }
                    builder.putLongArray(key, primitiveList);
                }
                break;
                case OBJECT:
                {
                    ValueContainer valueContainer = (ValueContainer) values.get(i);
                    builder.putObject(key, valueContainer.toOpenGraphObject());
                }
                break;
                case OBJECT_ARRAY:
                {
                    ArrayList<ShareOpenGraphObject> newList = new ArrayList<>();
                    List<ValueContainer> list = (List<ValueContainer>) values.get(i);
                    for(int j = 0; j<list.size(); j++){
                        ValueContainer valueContainer = list.get(j);
                        newList.add(valueContainer.toOpenGraphObject());
                    }
                    builder.putObjectArrayList(key, newList);
                }
                break;
                default:
                    continue;
            }
        }
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
                    int type = FREConversionUtil.toInt(types.getObjectAt(i));
                    ConversionType conversionType = ConversionType.fromValue(type);

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
