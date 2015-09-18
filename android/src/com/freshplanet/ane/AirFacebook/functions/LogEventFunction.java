package com.freshplanet.ane.AirFacebook.functions;

import android.os.Bundle;
import com.adobe.fre.*;
import com.facebook.appevents.AppEventsLogger;

public class LogEventFunction extends BaseFunction
{
	@Override
	public FREObject call(FREContext context, FREObject[] args)
	{
		super.call(context, args);

		try {
			String eventName = args[0].getProperty("eventName").getAsString();
			Double valueToSum = args[0].getProperty("valueToSum").getAsDouble();
			FREArray paramsKeysArray = (FREArray)args[0].getProperty("keys");
			FREArray paramsTypesArray = (FREArray)args[0].getProperty("types");
			FREArray paramsValuesArray = (FREArray)args[0].getProperty("values");

			Bundle parameters = getBundleFromFREArrays(paramsKeysArray, paramsTypesArray, paramsValuesArray);

			AppEventsLogger logger = AppEventsLogger.newLogger(context.getActivity().getApplicationContext());
			logger.logEvent(eventName, valueToSum, parameters);

		} catch (FRETypeMismatchException | FREInvalidObjectException | FREASErrorException | FRENoSuchNameException | FREWrongThreadException e) {
			e.printStackTrace();
		}

		return null;
	}

}
