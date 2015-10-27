/**
 * Created by nodrock on 27/10/15.
 */
package com.freshplanet.ane.AirFacebook {
public class FBLoginResult {

    private var _isSuccess:Boolean;
    private var _isCancelled:Boolean;
    private var _isError:Boolean;
    private var _object:Object;
    private var _json:String;

    public function FBLoginResult()
    {
    }

    public static function createError():FBLoginResult
    {
        var result:FBLoginResult = new FBLoginResult();
        result._object = {error: "Unexpected error!"};
        result._json = JSON.stringify(result._object);
        result._isSuccess = false;
        result._isCancelled = false;
        result._isError = true;
        return result;
    }

    public static function parserFromJSON(json:String):FBLoginResult
    {
        var object:Object = JSON.parse(json);
        if(object != null){
            var result:FBLoginResult = new FBLoginResult();
            result._json = json;
            result._object = object;
            result._isSuccess = object["result"] == "success";
            result._isCancelled = object["result"] == "cancel";
            result._isError = object["result"] == "error";
            return result;
        } else {
            return createError();
        }
    }

    public function isSuccess():Boolean
    {
        return _isSuccess;
    }

    public function isCancelled():Boolean
    {
        return _isCancelled;
    }

    public function isError():Boolean
    {
        return _isError;
    }

    public function getErrorMessage():String
    {
        if(_isError){

            if(_object.hasOwnProperty("error")){
                return _object["error"];
            } else if (_object.hasOwnProperty("userInfo")){
                return _object["userInfo"]["NSLocalizedDescription"];
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public function get json():String
    {
        return _json;
    }
}
}
