package com.freshplanet.ane.AirFacebook.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREObject;
import com.facebook.share.widget.AppInviteDialog;

public class CanPresentAppInviteDialogFunction extends BaseFunction
{
	public FREObject call(FREContext context, FREObject[] args)
	{
		try {
			return FREObject.newObject(AppInviteDialog.canShow());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}