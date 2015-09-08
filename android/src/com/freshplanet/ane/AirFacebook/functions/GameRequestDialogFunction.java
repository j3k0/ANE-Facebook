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
		String actionType = FREConversionUtil.toString(FREConversionUtil.getProperty("actionType", args[0]));
		String objectId = FREConversionUtil.toString(FREConversionUtil.getProperty("objectId", args[0]));
		String filters = FREConversionUtil.toString(FREConversionUtil.getProperty("filters", args[0]));
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
		// Android game request to do not support multiple targets. Use first target.
		if(to != null && to.size() > 0) builder.setTo(to.get(0));
		if(data != null) builder.setData(data);
		if(title != null) builder.setTitle(title);
		if(actionType != null) builder.setActionType(GameRequestContent.ActionType.valueOf(actionType));
		if(objectId != null) builder.setObjectId(objectId);
		if(filters != null) builder.setFilters(GameRequestContent.Filters.valueOf(filters));
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