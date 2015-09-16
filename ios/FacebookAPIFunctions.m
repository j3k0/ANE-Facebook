//
//  FacebookAPIFunctions.m
//  AirFacebook
//
//  Created by Ján Horváth on 16/09/15.
//
//

#import <Foundation/Foundation.h>
#import "FacebookAPIFunctions.h"

#import "FPANEUtils.h"
#import "FREConversionUtil.h"
#import "AirFacebook.h"

#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import <FBSDKLoginKit/FBSDKLoginKit.h>
#import <FBSDKShareKit/FBSDKShareKit.h>

#import "FBShareDelegate.h"
#import "FBAppInviteDialogDelegate.h"
#import "FBGameRequestDialogDelegate.h"

#import "FBSDKGameRequestContent+Parser.h"
#import "FBSDKAccessToken+Serializer.h"
#import "FBSDKProfile+Serializer.h"

DEFINE_ANE_FUNCTION(logInWithPermissions)
{
    NSArray *permissions = FPANE_FREObjectToNSArrayOfNSString(argv[0]);
    NSString *type = FPANE_FREObjectToNSString(argv[1]);
    
    [AirFacebook log:[NSString stringWithFormat:@"Trying to open session with %@ permissions: %@", type, [permissions componentsJoinedByString:@", "]]];
    
    FBSDKLoginManager *loginManager = [[FBSDKLoginManager alloc] init];
    loginManager.loginBehavior = [[AirFacebook sharedInstance] loginBehavior];
    loginManager.defaultAudience = [[AirFacebook sharedInstance] defaultAudience];
    if([type isEqualToString:@"read"]){
        [loginManager logInWithReadPermissions:permissions fromViewController:nil handler: [AirFacebook openSessionCompletionHandler]];
    }else{
        [loginManager logInWithPublishPermissions:permissions fromViewController:nil handler: [AirFacebook openSessionCompletionHandler]];
    }
    
    return nil;
}

DEFINE_ANE_FUNCTION(nativeLog)
{
    NSString *message = FPANE_FREObjectToNSString(argv[0]);
    
    // NOTE: logs from as3 should go only to native log
    [AirFacebook nativeLog:message withPrefix:@"AS3"];
    
    return nil;
}


DEFINE_ANE_FUNCTION(setNativeLogEnabled)
{
    BOOL nativeLogEnabled = FPANE_FREObjectToBOOL(argv[0]);
    
    [[AirFacebook sharedInstance] setNativeLogEnabled:nativeLogEnabled];
    
    return nil;
}

DEFINE_ANE_FUNCTION(initFacebook)
{
    [AirFacebook log:@"initFacebook"];
    
    NSString *callback = FPANE_FREObjectToNSString(argv[1]);
    
    // maybe we dont need this sharedInstance
    [AirFacebook sharedInstance];
    
    [[FBSDKApplicationDelegate sharedInstance] application:[UIApplication sharedApplication] didFinishLaunchingWithOptions:[NSMutableDictionary dictionary]];
    
    [[AirFacebook sharedInstance] dispatchEvent:[NSString stringWithFormat:@"SDKINIT_%@", callback] withMessage:nil];
    
    return nil;
}

DEFINE_ANE_FUNCTION(handleOpenURL)
{
    [AirFacebook log:@"handleOpenURL"];
    
    NSURL *url = [NSURL URLWithString:FPANE_FREObjectToNSString(argv[0])];
    NSString *sourceApplication = FPANE_FREObjectToNSString(argv[1]);
    NSString *annotation = FPANE_FREObjectToNSString(argv[2]);
    
    BOOL result = [[FBSDKApplicationDelegate sharedInstance] application:[UIApplication sharedApplication]
                                                                 openURL:url
                                                       sourceApplication:sourceApplication
                                                              annotation:annotation];
    return FPANE_BOOLToFREObject(result);
}

DEFINE_ANE_FUNCTION(getAccessToken)
{
    FBSDKAccessToken *token = [FBSDKAccessToken currentAccessToken];
 
    [AirFacebook log:@"getAccessToken token:%@", [token toString]];
    
    return [token toFREObject];
}

DEFINE_ANE_FUNCTION(getProfile)
{
    FBSDKProfile *profile = [FBSDKProfile currentProfile];
 
    [AirFacebook log:@"getProfile profile:%@", [profile toString]];
    
    return [profile toFREObject];
}

DEFINE_ANE_FUNCTION(logOut)
{
    FBSDKLoginManager *loginManager = [[FBSDKLoginManager alloc] init];
    [loginManager logOut];
    [FBSDKAccessToken setCurrentAccessToken:nil];
    [FBSDKProfile setCurrentProfile:nil];
    
    return nil;
}

DEFINE_ANE_FUNCTION(requestWithGraphPath)
{
    NSString *graphPath = FPANE_FREObjectToNSString(argv[0]);
    NSDictionary *parameters = FPANE_FREObjectsToNSDictionaryOfNSString(argv[1], argv[2]);
    NSString *httpMethod = FPANE_FREObjectToNSString(argv[3]);
    NSString *callback = FPANE_FREObjectToNSString(argv[4]);
    
    if ([FBSDKAccessToken currentAccessToken]) {
        [[[FBSDKGraphRequest alloc] initWithGraphPath:graphPath parameters:parameters HTTPMethod:httpMethod]
         startWithCompletionHandler:^(FBSDKGraphRequestConnection *connection, id result, NSError *error) {
             if (error){
                 
                 if (callback){
                     
                     NSDictionary* parsedResponseKey = [error.userInfo objectForKey:FBSDKGraphRequestErrorParsedJSONResponseKey];
                     if (parsedResponseKey && [parsedResponseKey objectForKey:@"body"])
                     {
                         NSDictionary* body = [parsedResponseKey objectForKey:@"body"];
                         NSError *jsonError = nil;
                         NSData *resultData = [NSJSONSerialization dataWithJSONObject:body options:0 error:&jsonError];
                         if (jsonError)
                         {
                             [AirFacebook log:[NSString stringWithFormat:@"Request error -> JSON error: %@", [jsonError description]]];
                         } else
                         {
                             NSString *resultString = [[NSString alloc] initWithData:resultData encoding:NSUTF8StringEncoding];
                             [[AirFacebook sharedInstance] dispatchEvent:callback withMessage:resultString];
                         }
                     }
                     return;
                 }
                 
                 [AirFacebook log:[NSString stringWithFormat:@"Request error: %@", [error description]]];
                 
             }
             else{
                 
                 NSError *jsonError = nil;
                 NSData *resultData = [NSJSONSerialization dataWithJSONObject:result options:0 error:&jsonError];
                 if (jsonError)
                 {
                     [AirFacebook log:[NSString stringWithFormat:@"Request JSON error: %@", [jsonError description]]];
                 }
                 else
                 {
                     NSString *resultString = [[NSString alloc] initWithData:resultData encoding:NSUTF8StringEncoding];
                     [[AirFacebook sharedInstance] dispatchEvent:callback withMessage:resultString];
                 }
                 
             }
         }];
    }
    
    return nil;
}

DEFINE_ANE_FUNCTION(setDefaultAudience)
{
    NSUInteger defaultAudience = FPANE_FREObjectToNSUInteger(argv[0]);
    
    [AirFacebook log:@"defaultAudience value:%d", defaultAudience];
    [[AirFacebook sharedInstance] setDefaultAudience:defaultAudience];
    
    return nil;
}

DEFINE_ANE_FUNCTION(setLoginBehavior)
{
    NSUInteger loginBehavior = FPANE_FREObjectToNSUInteger(argv[0]);
    
    [AirFacebook log:@"setLoginBehavior value:%d", loginBehavior];
    [[AirFacebook sharedInstance] setLoginBehavior:loginBehavior];
    
    return nil;
}

DEFINE_ANE_FUNCTION(setDefaultShareDialogMode)
{
    NSUInteger defaultShareDialogMode = FPANE_FREObjectToNSUInteger(argv[0]);
    
    [AirFacebook log:@"defaultShareDialogMode value:%d", defaultShareDialogMode];
    [[AirFacebook sharedInstance] setDefaultShareDialogMode:defaultShareDialogMode];
    
    return nil;
}

DEFINE_ANE_FUNCTION(canPresentShareDialog)
{
    UIViewController *rootViewController = [[[[UIApplication sharedApplication] delegate] window] rootViewController];
    
    FBSDKShareDialog *dialog = [[FBSDKShareDialog alloc] init];
    dialog.fromViewController = rootViewController;
    dialog.mode = [[AirFacebook sharedInstance] defaultShareDialogMode];
    BOOL canShow = [dialog canShow];
    
    return FPANE_BOOLToFREObject(canShow);
}

DEFINE_ANE_FUNCTION(shareLinkDialog)
{
    NSString *contentUrl = [FREConversionUtil toString:[FREConversionUtil getProperty:@"contentUrl" fromObject:argv[0]]];
    NSArray *peopleIds = [FREConversionUtil toStringArray:[FREConversionUtil getProperty:@"peopleIds" fromObject:argv[0]]];
    NSString *placeId = [FREConversionUtil toString:[FREConversionUtil getProperty:@"placeId" fromObject:argv[0]]];
    NSString *ref = [FREConversionUtil toString:[FREConversionUtil getProperty:@"ref" fromObject:argv[0]]];
    NSString *contentTitle = [FREConversionUtil toString:[FREConversionUtil getProperty:@"contentTitle" fromObject:argv[0]]];
    NSString *contentDescription = [FREConversionUtil toString:[FREConversionUtil getProperty:@"contentDescription" fromObject:argv[0]]];
    NSString *imageUrl = [FREConversionUtil toString:[FREConversionUtil getProperty:@"imageUrl" fromObject:argv[0]]];
    
    BOOL useShareApi = FPANE_FREObjectToBOOL(argv[1]);
    NSString *callback = FPANE_FREObjectToNSString(argv[2]);
    
    FBSDKShareLinkContent *content = [[FBSDKShareLinkContent alloc] init];
    if(contentUrl != nil) content.contentURL = [NSURL URLWithString:contentUrl];
    if(peopleIds != nil) content.peopleIDs = peopleIds;
    if(placeId != nil) content.placeID = placeId;
    if(ref != nil) content.ref = ref;
    if(contentTitle != nil) content.contentTitle = contentTitle;
    if(contentDescription != nil) content.contentDescription = contentDescription;
    if(imageUrl != nil) content.imageURL = [NSURL URLWithString:imageUrl];
    
    [[AirFacebook sharedInstance] shareContent:content usingShareApi:useShareApi andCallback:callback];
    
    return nil;
}

DEFINE_ANE_FUNCTION(appInviteDialog)
{
    NSString *appLinkUrl = [FREConversionUtil toString:[FREConversionUtil getProperty:@"appLinkUrl" fromObject:argv[0]]];
    NSString *previewImageUrl = [FREConversionUtil toString:[FREConversionUtil getProperty:@"previewImageUrl" fromObject:argv[0]]];
    
    NSString *callback = FPANE_FREObjectToNSString(argv[1]);
    
    FBSDKAppInviteContent *content = [[FBSDKAppInviteContent alloc] init];
    if(appLinkUrl != nil) content.appLinkURL = [NSURL URLWithString:appLinkUrl];
    if(previewImageUrl != nil) content.appInvitePreviewImageURL = [NSURL URLWithString:previewImageUrl];
    
    [[AirFacebook sharedInstance] showAppInviteDialogWithContent:content andCallback:callback];
    
    return nil;
}

DEFINE_ANE_FUNCTION(gameRequestDialog)
{
    FBSDKGameRequestContent *content = [FBSDKGameRequestContent parseFromFREObject:argv[0]];
    [AirFacebook log:[content toString]];
    NSString *callback = FPANE_FREObjectToNSString(argv[1]);
    
    [[AirFacebook sharedInstance] showGameRequestDialogWithContent:content andCallback:callback];
    
    return nil;
}

DEFINE_ANE_FUNCTION(activateApp)
{
    [FBSDKAppEvents activateApp];
    return nil;
}

DEFINE_ANE_FUNCTION(logEvent)
{
    NSString *eventName = [FREConversionUtil toString:[FREConversionUtil getProperty:@"eventName" fromObject:argv[0]]];
    NSNumber *valueToSum = [FREConversionUtil toNumber:[FREConversionUtil getProperty:@"valueToSum" fromObject:argv[0]]];
    NSDictionary *parameters = FPANE_FREObjectsToNSDictionary([FREConversionUtil getProperty:@"paramsKeys" fromObject:argv[0]],
                                                              [FREConversionUtil getProperty:@"paramsTypes" fromObject:argv[0]],
                                                              [FREConversionUtil getProperty:@"paramsValues" fromObject:argv[0]]);
    
    [AirFacebook log:@"logEvent name:%@", eventName];
    
    [FBSDKAppEvents logEvent:eventName valueToSum:[valueToSum doubleValue] parameters:parameters];
    return nil;
}
