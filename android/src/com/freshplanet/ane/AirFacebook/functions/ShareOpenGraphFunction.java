package com.freshplanet.ane.AirFacebook.functions;

import android.content.Intent;
import android.net.Uri;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphContent;
import com.freshplanet.ane.AirFacebook.AirFacebookExtension;
import com.freshplanet.ane.AirFacebook.ShareDialogActivity;
import com.freshplanet.ane.AirFacebook.utils.FacebookObjectsConversionUtil;

import java.util.List;

public class ShareOpenGraphFunction implements FREFunction
{
	public FREObject call(FREContext context, FREObject[] args)
	{
//		Boolean useShareApi = getBooleanFromFREObject(args[1]);
//		String callback = getStringFromFREObject(args[2]);

		AirFacebookExtension.log("ShareOpenGraphFunction");

		ShareOpenGraphContent.Builder builder = new ShareOpenGraphContent.Builder();
		FacebookObjectsConversionUtil.parseShareOpenGraphContent(args[0], builder);
		ShareOpenGraphContent content = builder.build();

		// Start dialog activity
//		Intent i = new Intent(context.getActivity().getApplicationContext(), ShareDialogActivity.class);
//		i.putExtra(ShareDialogActivity.extraPrefix + ".callback", callback);
//		i.putExtra(ShareDialogActivity.extraPrefix + ".content", content);
//		i.putExtra(ShareDialogActivity.extraPrefix + ".useShareApi", useShareApi);
//		context.getActivity().startActivity(i);

		return null;
		
	}
}