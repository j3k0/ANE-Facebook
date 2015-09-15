package com.freshplanet.ane.AirFacebook.functions;

import android.content.Intent;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.facebook.share.model.GameRequestContent;
import com.freshplanet.ane.AirFacebook.AirFacebookExtension;
import com.freshplanet.ane.AirFacebook.GameRequestActivity;
import com.freshplanet.ane.AirFacebook.utils.FREConversionUtil;

import java.util.ArrayList;

public class GameRequestDialogFunction extends BaseFunction implements FREFunction
{
	public FREObject call(FREContext context, FREObject[] args)
	{
		super.call(context, args);

		// Retrieve callback
		String message = FREConversionUtil.toString(FREConversionUtil.getProperty("message", args[0]));
		ArrayList<String> to = getStringListProperty(args[0], "to");
		String data = FREConversionUtil.toString(FREConversionUtil.getProperty("data", args[0]));
		String title = FREConversionUtil.toString(FREConversionUtil.getProperty("title", args[0]));

		GameRequestContent.ActionType actionType = null;
		FREObject actionTypeObject = FREConversionUtil.getProperty("actionType", args[0]);
		if(actionTypeObject != null) {

			int actionTypeInt = getIntFromFREObject(FREConversionUtil.getProperty("value", actionTypeObject));
			switch (actionTypeInt){
				case 1: actionType = GameRequestContent.ActionType.SEND; break;
				case 2: actionType = GameRequestContent.ActionType.ASKFOR; break;
				case 3: actionType = GameRequestContent.ActionType.TURN; break;
				default: actionType = null;
			}
		}

		String objectId = FREConversionUtil.toString(FREConversionUtil.getProperty("objectId", args[0]));

		GameRequestContent.Filters filters = null;
		FREObject filtersObject = FREConversionUtil.getProperty("filters", args[0]);
		if(filtersObject != null) {

			int filtersObjectInt = getIntFromFREObject(FREConversionUtil.getProperty("value", filtersObject));
			switch (filtersObjectInt){
				case 1: filters = GameRequestContent.Filters.APP_USERS; break;
				case 2: filters = GameRequestContent.Filters.APP_NON_USERS; break;
				default: actionType = null;
			}
		}

		ArrayList<String> suggestions = getStringListProperty(args[0], "suggestions");
		String callback = getStringFromFREObject(args[1]);

		AirFacebookExtension.log("GameRequestDialogFunction"
			+ " message:" + message
			+ " to:" + to
			+ " data:" + data
			+ " title:" + title
			+ " actionType:" + actionType
			+ " objectId:" + objectId
			+ " filters:" + filters
			+ " suggestions:" + suggestions
		);

		GameRequestContent.Builder builder = new GameRequestContent.Builder();
		if(message != null) builder.setMessage(message);
		// TODO: send ticket to FB
		// Android game request to do not support multiple targets. Use first target.
		if(to != null && to.size() > 0) builder.setTo(to.get(0));
		if(data != null) builder.setData(data);
		if(title != null) builder.setTitle(title);
		if(actionType != null) builder.setActionType(actionType);
		if(objectId != null) builder.setObjectId(objectId);
		if(filters != null) builder.setFilters(filters);
		if(suggestions != null) builder.setSuggestions(suggestions);
		GameRequestContent content = builder.build();

		// Start dialog activity
		Intent i = new Intent(context.getActivity().getApplicationContext(), GameRequestActivity.class);
		i.putExtra(GameRequestActivity.extraPrefix + ".callback", callback);
		i.putExtra(GameRequestActivity.extraPrefix + ".content", content);
		context.getActivity().startActivity(i);

		return null;

	}
}