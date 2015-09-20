package com.freshplanet.ane.AirFacebook.functions;

import android.os.Bundle;
import com.adobe.fre.*;
import com.facebook.appevents.AppEventsLogger;
import com.freshplanet.ane.AirFacebook.utils.FREConversionUtil;

public class LogEventFunction implements FREFunction
{
	@Override
	public FREObject call(FREContext context, FREObject[] args)
	{
		String eventName = FREConversionUtil.toString(FREConversionUtil.getProperty("eventName", args[0]));
		Double valueToSum = FREConversionUtil.toDouble(FREConversionUtil.getProperty("valueToSum", args[0]));
		if(valueToSum == null){
			valueToSum = 0.0;
		}

		Bundle parameters = FREConversionUtil.toBundle(args[0]);

		AppEventsLogger logger = AppEventsLogger.newLogger(context.getActivity().getApplicationContext());
		logger.logEvent(eventName, valueToSum, parameters);

		return null;
	}

}
