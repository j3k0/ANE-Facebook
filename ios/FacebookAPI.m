//
//  FacebookAPI.m
//  AirFacebook
//
//  Created by Ján Horváth on 16/09/15.
//
//

#import <Foundation/Foundation.h>
#import "FacebookAPI.h"
#import "FacebookAPIFunctions.h"
#import "AirFacebook.h"

void AirFacebookInitializer(void** extDataToSet, FREContextInitializer* ctxInitializerToSet, FREContextFinalizer* ctxFinalizerToSet)
{
    *extDataToSet = NULL;
    *ctxInitializerToSet = &AirFacebookContextInitializer;
    *ctxFinalizerToSet = &AirFacebookContextFinalizer;
}

void AirFacebookFinalizer(void *extData)
{
}

void AirFacebookContextInitializer(void* extData, const uint8_t* ctxType, FREContext ctx,
                                   uint32_t* numFunctionsToTest, const FRENamedFunction** functionsToSet)
{
    NSDictionary *functions = @{
                                @"initFacebook":                    [NSValue valueWithPointer:&ANEFACEBOOK(initFacebook)],
                                @"handleOpenURL":                   [NSValue valueWithPointer:&ANEFACEBOOK(handleOpenURL)],
                                @"getAccessToken":                  [NSValue valueWithPointer:&ANEFACEBOOK(getAccessToken)],
                                @"getProfile":                      [NSValue valueWithPointer:&ANEFACEBOOK(getProfile)],
                                @"logInWithPermissions":            [NSValue valueWithPointer:&ANEFACEBOOK(logInWithPermissions)],
                                @"logOut":                          [NSValue valueWithPointer:&ANEFACEBOOK(logOut)],
                                @"requestWithGraphPath":            [NSValue valueWithPointer:&ANEFACEBOOK(requestWithGraphPath)],
                                
                                // Settings
                                @"setDefaultShareDialogMode":       [NSValue valueWithPointer:&ANEFACEBOOK(setDefaultShareDialogMode)],
                                @"setLoginBehavior":                [NSValue valueWithPointer:&ANEFACEBOOK(setLoginBehavior)],
                                @"setDefaultAudience":              [NSValue valueWithPointer:&ANEFACEBOOK(setDefaultAudience)],
                                
                                // Sharing dialogs
                                @"canPresentShareDialog":           [NSValue valueWithPointer:&ANEFACEBOOK(canPresentShareDialog)],
                                @"shareLinkDialog":                 [NSValue valueWithPointer:&ANEFACEBOOK(shareLinkDialog)],
                                @"appInviteDialog":                 [NSValue valueWithPointer:&ANEFACEBOOK(appInviteDialog)],
                                @"gameRequestDialog":               [NSValue valueWithPointer:&ANEFACEBOOK(gameRequestDialog)],
                                @"shareOpenGraph":                  [NSValue valueWithPointer:&ANEFACEBOOK(shareOpenGraph)],
                                
                                // FB events
                                @"activateApp":                     [NSValue valueWithPointer:&ANEFACEBOOK(activateApp)],
                                @"logEvent":                        [NSValue valueWithPointer:&ANEFACEBOOK(logEvent)],
                                
                                // Debug
                                @"nativeLog":                       [NSValue valueWithPointer:&ANEFACEBOOK(nativeLog)],
                                @"setNativeLogEnabled":             [NSValue valueWithPointer:&ANEFACEBOOK(setNativeLogEnabled)],
                                };
    
    *numFunctionsToTest = (uint32_t)[functions count];
    
    FRENamedFunction *func = (FRENamedFunction *)malloc(sizeof(FRENamedFunction) * [functions count]);
    
    uint32_t i = 0;
    for (NSString* functionName in functions){
        NSValue *value = functions[functionName];
        
        func[i].name = (const uint8_t *)[functionName UTF8String];
        func[i].functionData = NULL;
        func[i].function = [value pointerValue];
        i++;
    }
    
    *functionsToSet = func;
    
    [[AirFacebook sharedInstance] setContext:ctx];
}

void AirFacebookContextFinalizer(FREContext ctx)
{
}
