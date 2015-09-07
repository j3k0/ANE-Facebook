package com.freshplanet.ane.AirFacebook.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREObject;
import com.facebook.share.widget.GameRequestDialog;

public class CanPresentGameRequestDialogFunction extends BaseFunction
{
	public FREObject call(FREContext context, FREObject[] args)
	{
		super.call(context, args);

		try {
			return FREObject.newObject(GameRequestDialog.canShow());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}