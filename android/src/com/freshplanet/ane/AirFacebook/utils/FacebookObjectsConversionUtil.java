package com.freshplanet.ane.AirFacebook.utils;

import android.net.Uri;
import android.os.Bundle;
import com.adobe.fre.FREArray;
import com.adobe.fre.FREObject;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphValueContainer;
import com.freshplanet.ane.AirFacebook.AirFacebookExtension;

import java.util.ArrayList;
import java.util.List;

public class FacebookObjectsConversionUtil {

    public static void parseShareContent(FREObject object, ShareContent.Builder builder)
    {
        String contentUrl = getStringProperty(object, "contentUrl");
        List<String> peopleIds = getStringListProperty(object, "peopleIds");
        String placeId = getStringProperty(object, "placeId");
        String ref = getStringProperty(object, "ref");

        if(contentUrl != null) builder.setContentUrl(Uri.parse(contentUrl));
        if(peopleIds != null) builder.setPeopleIds(peopleIds);
        if(placeId != null) builder.setPlaceId(placeId);
        if(ref != null) builder.setRef(ref);
    }

    public static void parseShareOpenGraphContent(FREObject object, ShareOpenGraphContent.Builder builder)
    {
        FacebookObjectsConversionUtil.parseShareContent(object, builder);

        String previewPropertyName = getStringProperty(object, "previewPropertyName");
        ValueContainer valueContainer = ValueContainer.getValueContainer(FREConversionUtil.getProperty("action", object));

        AirFacebookExtension.log("VALUECONTAINER " + valueContainer.toString());

        if(previewPropertyName != null) builder.setPreviewPropertyName(previewPropertyName);
        if(valueContainer != null) builder.setAction(valueContainer.toOpenGraphAction());
    }

    public static void parseShareLinkContent(FREObject object, ShareLinkContent.Builder builder)
    {
        FacebookObjectsConversionUtil.parseShareContent(object, builder);

        String contentTitle = getStringProperty(object, "contentTitle");
        String contentDescription = getStringProperty(object, "contentDescription");
        String imageUrl = getStringProperty(object, "imageUrl");

        if(contentTitle != null) builder.setContentTitle(contentTitle);
        if(contentDescription != null) builder.setContentDescription(contentDescription);
        if(imageUrl != null) builder.setImageUrl(Uri.parse(imageUrl));
    }

    protected static String getStringProperty(FREObject object, String property)
    {
        FREObject propertyObject = FREConversionUtil.getProperty(property, object);
        if(propertyObject == null){
            return null;
        }
        return FREConversionUtil.toString(propertyObject);
    }

    protected static List<String> getStringListProperty(FREObject object, String property)
    {
        try
        {
            FREArray propertyArray = (FREArray)object.getProperty(property);
            if(propertyArray == null){
                return null;
            }
            return getListOfStringFromFREArray(propertyArray);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    protected static List<String> getListOfStringFromFREArray(FREArray array)
    {
        List<String> result = new ArrayList<String>();

        try
        {
            for (int i = 0; i < array.getLength(); i++)
            {
                result.add(FREConversionUtil.toString(array.getObjectAt((long) i)));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return result;
    }
}
