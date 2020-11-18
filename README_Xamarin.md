# DocuSign Android SDK Integration for Xamarin apps
DocuSign Android SDK provides the following features:
* Templates 
* Envelope creation
* Offline Signing of documents
* Syncing signed documents with DocuSign

## Credentials Needed
Before getting started, an Integration Key and valid Service User credentials are needed. The SDK cannot be used without these.
### Integration Key
To use any DocuSign SDK or the REST API, an Integration Key is needed. Visit https://developers.docusign.com/ to obtain an Integration Key if one does not already exist. Note that an Integration Key is first provisioned on the DEMO environment, and then must be promoted to PROD when ready. 
### Email & Password
To use the DocuSign Android SDK, credentials are necessary. That user's credentials are what should be used in the Authentication section below.
### Nuget
Android SDK is published to nuget.org and is available at https://www.nuget.org/packages/Xamarin.Android.DocuSign

## Supported versions
Android Studio version should be 3.4 and above. Apps which integrate with DocuSign SDK requires AndroidX.
compileSdkVersion and targetSdkVersion should be 29 and above.
DocuSign SDK supports android versions 5.0 and above (API level 21).

## Setup 
### DocuSign SDK Package
Select the nuget.org source and add ‘Xamarin.Android.DocuSign’ package with latest version to the Xamarin application packages.
### Packages
Add the following Packages to the Xamarin application:
- Xamarin.Android.ReactiveX.RxAndroid
- Xamarin.Android.ReactiveX.RxJava
- Xamarin.Android.Support.Core.Utils
- Xamarin.Android.Support.CustomTabs
- Xamarin.Android.Support.Design
- Xamarin.AndroidX.AppCompat
- Xamarin.AndroidX.Browser
- Xamarin.AndroidX.ConstraintLayout
- Xamarin.AndroidX.Lifecyle.Extensions
- Xamarin.AndroidX.Lifecycle.ViewModel
- Xamarin.AndroidX.MultiDex
- Xamarin.AndroidX.Preference
- Xamarin.AndroidX.RecyclerView
- Xamarin.AndroidX.Room.Runtime
- Xamarin.AndroidX.Work.Runtime
- Xamarin.Essentials
- Xamarin.Google.Android.Material
- Xamarin.Kotlin.StdLib
- Square.Retrofit2
- Square.Retrofit2.ConverterGson
- GoogleGson
- Square.Retrofit2.AdapterRxJava2
- Square.Okhttp3 - 3.8.0
- Square.OKIO
- PDFTron.Android.RichEditor
- PDFTron.Android.PageCropper
- Xamarin.Android.JakeWharton.ThreeTenAbp
### Proguard
Proguard might be required when you create release builds with Proguard enabled.
```
# Add project specific ProGuard rules here.
#
# For more details, see
# http://developer.android.com/guide/developing/tools/proguard.html
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
# public *;
#}
# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable
# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
 
-keepattributes Signature, InnerClasses, EnclosingMethod
# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
@retrofit2.http.* <methods>;
}
# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**
# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit
# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*
# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>
# Optional third party libraries. You can safely ignore those warnings.
-dontwarn com.squareup.okhttp.**
-dontwarn com.squareup.picasso.**
-dontwarn com.edmodo.cropper.**
-dontwarn org.slf4j.impl.**
# RxJava needs these two lines for proper operation.
-keep class rx.internal.util.unsafe.** { *; }
-keep class com.google.**
-dontwarn com.google.**
-dontnote com.google.**
-keep class okhttp3.**
-dontwarn okhttp3.**
-dontnote okhttp3.**
-keep class retrofit2.**
-dontwarn retrofit2.**
-dontnote retrofit2.**
-keepnames class rx.android.schedulers.AndroidSchedulers
-keepnames class rx.Observable
-keep class rx.schedulers.Schedulers {
public static <methods>;
public static ** test();
}
-keep class rx.schedulers.ImmediateScheduler {
public <methods>;
}
-keep class rx.schedulers.TestScheduler {
public <methods>;
}
-keep class rx.subscriptions.Subscriptions {
*;
}
-keep class rx.exceptions.** {
public <methods>;
}
-keep class rx.subjects.** {
public <methods>;
}
-keepclassmembers class android.webkit.** { *; }
-keep class android.webkit.** { *; }
-keepclassmembers class android.webkit.** { *; }
-keep class android.webkit.** { *; }
#JavaScript rules and classes required to be kept
-keepclasseswithmembers class * {
@android.webkit.JavascriptInterface <methods>;
}
-keepclasseswithmembernames class * {
native <methods>;
}
-keepclassmembers class com.docusign.androidsdk.** { *; }
-keep class com.docusign.androidsdk.** { *; }
-keepclassmembers class jp.wasabeef.richeditor.** { *; }
-keep class jp.wasabeef.richeditor.** { *; }
-dontwarn jp.wasabeef.richeditor.**
-keep class androidx.paging.**
-dontwarn androidx.paging.**
```

## API
### Initialization
Initializes the DocuSign SDK.
```csharp
DocuSign.Init(
     this, // the Application Context
     "[YOUR INTEGRATOR KEY HERE]", // Same as Client Id
     "[YOUR SECRET KEY]",
     "[YOUR REDIRECT_URI]",
     DSMode.Debug  
);
DocuSign.Instance.Environment = DSEnvironment.DemoEnvironment; // For Demo environment. For production environment, use DSEnvironment.ProductionEnvironment

```

## Authentication
### Login
#### OAuth
Authenticates the DocuSign user using OAuth.
```csharp
// requestCode - This code will be returned in onActivityResult() of the calling activity
 
DSAuthenticationDelegate authenticationDelegate = DocuSign.Instance.AuthenticationDelegate;
authenticationDelegate.Login(requestCode, this, new LoginListener());
 
public class LoginListener : Java.Lang.Object,IDSAuthenticationListener
{
    public void onAuthenticationSuccess(DSUser user)
    {
        // TODO: handle successful authentication here
    }
 
    public void onAuthenticationError(DSAuthenticationException exception)
    {
        // TODO: handle authentication failure here
    }
}
```

#### AuthToken
Authenticates the DocuSign user with the provided authToken and optional refreshToken.
```csharp
/// accessToken - Access Token which authenticates the user
// refreshToken - If the access token can be refreshed, the refresh token. Optional
// expiresIn - The number of seconds from the time the access token was provisioned to when it will expire
 
DSAuthenticationDelegate authenticationDelegate = DocuSign.Instance.AuthenticationDelegate;
authenticationDelegate.Login(accessToken,
                refreshToken,
                expiresIn,
                this,
                new AccessTokenLoginListener());
 
public class AccessTokenLoginListener : Java.Lang.Object, IDSAuthenticationListener
{
    public void onAuthenticationSuccess(DSUser user)
    {
        // TODO: handle successful authentication here
    }
 
    public void onAuthenticationError(DSAuthenticationException exception)
    {
       // TODO: handle authentication failure here
    }
}
```

### Logout
Logout the authenticated DocuSign user.
```csharp
// Clears the DocuSign cached data
Boolean clearCachedData = true
 
DSAuthenticationDelegate authenticationDelegate = DocuSign.Instance.AuthenticationDelegate;
authenticationDelegate.Logout(this, clearCachedData, new LogoutListener());
public class LogoutListener : Java.Lang.Object, IDSLogoutListener
{
    public void onLogoutSuccess()
    {
        // TODO: handle successful logout here
    }
 
    public void onLogoutError(DSAuthenticationException exception)
    {
        // TODO: handle logout failure here
    }
}
```

## Envelope Creation
The following example shows how to build an Envelope with one document, two signer recipients and one CC recipient. Each signer recipient has one signature tab. It also includes some metadata. 
```csharp
try
{
    DSAuthenticationDelegate authenticationDelegate = DocuSign.Instance.AuthenticationDelegate;
    DSUser user = authenticationDelegate.GetLoggedInUser(ApplicationContext);
    URI fileURI = URI.Create(sampleDoc.Name); // PDF Document
 
    DSDocument.Builder dsDocumentBuilder = new DSDocument.Builder();
    dsDocumentBuilder.DocumentId = 1L;
    dsDocumentBuilder.Uri = "file:" + fileURI.ToString();
    dsDocumentBuilder.Name = "DOCUMENT NAME";
    DSDocument dSDocument = dsDocumentBuilder.Build();
 
    DSTab.Builder recipient1TabBuilder = new DSTab.Builder();
    recipient1TabBuilder.DocumentId = 1L;
    recipient1TabBuilder.RecipientId = 1L;
    recipient1TabBuilder.PageNumber = 1; // the page on which this tab should appear
    recipient1TabBuilder.XPosition = 370;  // the x-coordinate on page 1 where you want the driver's signature
    recipient1TabBuilder.YPosition = 620; // the y-coordinate on page 1 where you want the driver's signature
    recipient1TabBuilder.Type = DSTabType.Signature;
    DSTab recipient1Tab1 = recipient1TabBuilder.Build();
 
    DSTab.Builder recipient2TabBuilder = new DSTab.Builder();
    recipient2TabBuilder.DocumentId = 1L;
    recipient2TabBuilder.RecipientId = 2L;
    recipient2TabBuilder.PageNumber = 1; // the page on which this tab should appear
    recipient2TabBuilder.XPosition = 80;  // the x-coordinate on page 1 where you want the driver's signature
    recipient2TabBuilder.YPosition = 620; // the y-coordinate on page 1 where you want the driver's signature
    recipient2TabBuilder.Type = DSTabType.Signature;
    DSTab recipient2Tab1 = recipient2TabBuilder.Build();
 
    return new DSEnvelope.Builder()
            .EnvelopeName("ENVELOPE NAME")
            .Document(dSDocument)
            .Recipient(new DSEnvelopeRecipient.Builder()
                    .RecipientId(1)
                    .RoutingOrder(1)
                    // this should be the user returned in DSAuthenticationListener.
                    .HostName(user.Name)
                    .HostEmail(user.Email)
                    // this should be the Customer's name
                    .SignerName("Customer John")
                    // this should be the Customer's email address
                    .SignerEmail("customer.john@gmail.com")
                    .Type(DSRecipientType.InPersonSigner)
                    .Tab(recipient1Tab1)
                    .Build())
            .Recipient(new DSEnvelopeRecipient.Builder()
                    .RecipientId(2)
                    .RoutingOrder(2)
                    // this should be the user returned in DSAuthenticationListener.
                    .HostName(user.Name)
                    .HostEmail(user.Email)
                    // this should be the Technician's name
                    .SignerName("Technician Pat")
                    // this should be the Technician's email address
                    .SignerEmail("technician.pat@gmail.com")
                    .Type(DSRecipientType.InPersonSigner)
                    .Tab(recipient2Tab1)
                    .Build())
            // this recipient receives a copy
            .Recipient(new DSEnvelopeRecipient.Builder()
                    .RecipientId(3)
                    .RoutingOrder(3)
                    // if someone needs a signed copy, their name here
                    .SignerName("Jack Doe")
                    // if someone needs a signed copy, their email here
                    .SignerEmail("jack.doe@gmail.com")
                    .Type(DSRecipientType.CarbonCopy)
                    .Build())
            .TextCustomFields(
                    // this is for free-form metadata
                    GetTextCustomFields()
            )
            .Build();
}
catch (DSEnvelopeException exception)
{
    if (exception.Message != null)
    {
        Log.Error(TAG, exception.Message);
    }
    return null;
}
catch (DSAuthenticationException exception)
{
    if (exception.Message != null)
    {
        Log.Error(TAG, exception.Message);
    }
    return null;
}
catch (DocuSignNotInitializedException exception)
{
    if (exception.Message != null)
    {
        Log.Error(TAG, exception.Message);
    }
    return null;
}
 
private IList<DSTextCustomField> GetTextCustomFields()
{
 
    DSTextCustomField textCustomField1, textCustomField2;
    ArrayList textCustomFields = new ArrayList();
    try
    {
        DSTextCustomField.Builder textCustomField1Builder = new DSTextCustomField.Builder();
        textCustomField1Builder.FieldId = 123L;
        textCustomField1Builder.Name = "metadata1";
        textCustomField1Builder.Value = "some value";
        textCustomField1 = textCustomField1Builder.Build();
 
        DSTextCustomField.Builder textCustomField2Builder = new DSTextCustomField.Builder();
        textCustomField2Builder.FieldId = 234L;
        textCustomField2Builder.Name = "metadata2";
        textCustomField2Builder.Value = "some value 2";
        textCustomField2 = textCustomField2Builder.Build();
 
 
        textCustomFields.Add(textCustomField1);
        textCustomFields.Add(textCustomField2);
    }
    catch (DSEnvelopeException exception)
    {
        if (exception.Message != null)
        {
            Log.Error(TAG, exception.Message);
        }
    }
 
    List<DSTextCustomField> list = new List<DSTextCustomField>(textCustomFields.Size());
    for (int i = 0; i < textCustomFields.Size(); i++)
    {
        list.Add((DSTextCustomField)textCustomFields.Get(i));
    }
    return list;
}
```

## Sending/Storing an Envelope
The following example assumes the envelope object from the above Envelope Creation section.
```csharp
try
{
    DocuSign.Instance.EnvelopeDelegate.ComposeAndSendEnvelope(envelope,
            new ComposeAndSendEnvelopeListener());
}
catch (DocuSignNotInitializedException exception)
{
    // TODO: handle error. This means the SDK object was not properly initialized
}
 
public class ComposeAndSendEnvelopeListener : Java.Lang.Object, IDSComposeAndSendEnvelopeListener
{
 
    public void onComposeAndSendEnvelopeSuccess(string envelopeId)
    {
        // At this point, the Envelope will be successfully created in local db store
        // TODO: this is where Signing will kickoff
    }
 
    public void onComposeAndSendEnvelopeError(DSEnvelopeException exception)
    {
        // TODO: handle failed envelope creation. exception.Message will indicate what went wrong
    }
}
```

## Deleting an Envelope from local storage
The following example assumes you know the envelopeId you want to delete.
```csharp
try
{
    DocuSign.Instance.EnvelopeDelegate.DeleteCachedEnvelope(envelopeId,
            new DeleteCachedEnvelopeListener());
}
catch (DocuSignNotInitializedException exception)
{
    // TODO: handle error. This means the SDK object was not properly initialized
}
 
public class DeleteCachedEnvelopeListener : Java.Lang.Object, IDSDeleteCachedEnvelopeListener
{
    public void onDeleteCachedEnvelopeSuccess(string envelopeId)
    {
        // TODO: handle successful envelope deletion
    }
 
    public void onDeleteCachedEnvelopeError(DSEnvelopeException exception)
    {
        // TODO: handle error with envelope deletion. exception.Message will indicate what went wrong
    }
 
}
```

## Signing an Envelope

### Offline Signing
The following example assumes that you know the envelopeId you want to sign in offline mode.
```csharp
try
{
    DocuSign.Instance.SigningDelegate.SignOffline(context,
        envelopeId, new OfflineSigningListener());
}
catch (DocuSignNotInitializedException exception)
{
    // TODO: handle error. This means the SDK object was not properly initialized
}
 
 
public class OfflineSigningListener : Java.Lang.Object, IDSOfflineSigningListener
{
    public void onOfflineSigningSuccess(string envelopeId)
    {
        // TODO: handle successful envelope signing
    }
 
    public void onOfflineSigningCancel(string envelopeId)
    {
        // TODO: handle when envelope signing is cancelled.
    }
 
    public void onOfflineSigningError(DSSigningException exception)
    {
        // TODO: handle error occurred during signing ceremony. exception.Message will indicate what went wrong
    }
}
```

### Online Signing
#### Online Signing with locally cached envelope
The following example assumes you know the envelopeId of cached envelope that you want to sign online
```csharp
try
{
    DocuSign.Instance.SigningDelegate.CreateEnvelopeAndLaunchOnlineSigning(context,
            localEnvelopeId, new OnlineSigningListener());
}
catch (DocuSignNotInitializedException exception)
{
    // TODO: handle error. This means the SDK object was not properly initialized
}
 
 
public class OnlineSigningListener : Java.Lang.Object, IDSOnlineSigningListener
{
    public void onOnlineRecipientSigningError(string envelopeId, string recipientId, DSSigningException exception)
    {
        // TODO: handle signing error for a recipient
    }

    public void onOnlineRecipientSigningSuccess(string envelopeId, string recipientId)
    {
       // TODO: handle signing success for a recipient
    }

    public void onOnlineSigningCancel(string envelopeId, string recipientId)
    {
        // TODO: handle when signing is cancelled for a recipient
    }

    public void onOnlineSigningError(string envelopeId, DSSigningException exception)
    {
       // TODO: handle when error during signing 
    }

    public void onOnlineSigningStart(string envelopeId)
    {
        // TODO: handle when signing is started
    }

    public void onOnlineSigningSuccess(string envelopeId)
    {
        // TODO: handle when signing is completed successfully
    }
}
```

#### Online Signing with envelope created in DocuSign portal
The following example assumes you know the envelopeId of envelope created in DocuSign portal that you want to sign online
```csharp
try
{
    DocuSign.Instance.SigningDelegate.signOnline(context,
            serverEnvelopeId, new OnlineSigningListener());
}
catch (DocuSignNotInitializedException exception)
{
    // TODO: handle error. This means the SDK object was not properly initialized
}
 
 
public class OnlineSigningListener : Java.Lang.Object, IDSOnlineSigningListener
{
    public void onOnlineRecipientSigningError(string envelopeId, string recipientId, DSSigningException exception)
    {
        // TODO: handle signing error for a recipient
    }

    public void onOnlineRecipientSigningSuccess(string envelopeId, string recipientId)
    {
       // TODO: handle signing success for a recipient
    }

    public void onOnlineSigningCancel(string envelopeId, string recipientId)
    {
        // TODO: handle when signing is cancelled for a recipient
    }

    public void onOnlineSigningError(string envelopeId, DSSigningException exception)
    {
       // TODO: handle when error during signing 
    }

    public void onOnlineSigningStart(string envelopeId)
    {
        // TODO: handle when signing is started
    }

    public void onOnlineSigningSuccess(string envelopeId)
    {
        // TODO: handle when signing is completed successfully
    }
}
```



## Syncing an envelope
The following example assumes you know the envelopeId you want to sync.
```csharp
try
{
    DocuSign.Instance.EnvelopeDelegate.SyncEnvelope(envelopeId,
            new SyncEnvelopeListener(), true);  // passing true, deletes the envelope in Database after syncing it to the cloud.
                                                // Setting it to false, will retain the envelope in db with all the necessary information. An explicit clean up is required on your end to keep the db clean.
}
catch (DocuSignNotInitializedException exception)
{
    // TODO: handle error. This means the SDK object was not properly initialized
}
 
public class SyncEnvelopeListener : Java.Lang.Object, IDSSyncEnvelopeListener
{
    public void onSyncEnvelopeSuccess(string localEnvelopeId, string serverEnvelopeId)
    {
        // At this point, envelope with localEnvelopeId is synced successfully and a new envelope Id is returned from
        // server which can be accessed using serverEnvelopeId
        // TODO: handle successful envelope syncing
    }
 
    public void onSyncEnvelopeError(DSSyncException exception, string localEnvelopeId,
                                    Java.Lang.Integer syncRetryCount)
    {
       // At this point, envelope with localEnvelopeId failed to sync. DSSyncException errorCode and errorMsg gives
       // the reason for sync failure. syncRetryCount tells the number of times envelope sync failed
       // TODO: handle error with envelope syncing. exception.getMessage() will indicate what went wrong
    }
}
```

## Syncing all envelopes
The following example will sync all Sync Pending envelopes.
```csharp
try
{
    DocuSign.Instance.EnvelopeDelegate.SyncAllEnvelope(new SyncAllEnvelopeListener(), true);
}
catch (DocuSignNotInitializedException exception)
{
    // TODO: handle error. This means the SDK object was not properly initialized
}
 
public class SyncAllEnvelopeListener : Java.Lang.Object, IDSSyncAllEnvelopesListener
{
    public void onSyncAllEnvelopesStart()
    {
        // TODO: handle on start of syncing of all envelopes
    }
 
    public void onSyncAllEnvelopesEnvelopeSyncSuccess(string localEnvelopeId, string serverEnvelopeId)
    {
        // At this point, envelope with localEnvelopeId is synced successfully and
        // server returned the synced envelope Id: serverEnvelopeId
        // TODO: handle successful envelope sync of envelope with Id localenvelopeId
    }
 
    public void onSyncAllEnvelopesEnvelopeSyncError(DSSyncException exception, string localEnvelopeId, Java.Lang.Integer syncRetryCount)
    {
        // At this point, envelope with localEnvelopeId failed to sync. DSSyncException errorCode and errorMsg gives
        // the reason for sync failure. syncRetryCount tells the number of times envelope sync failed
        // TODO: handle error with envelope syncing of envelope with Id localenvelopeId
    }
 
    public void onSyncAllEnvelopesComplete(IList<string> failedEnvelopeIdList)
    {
        // failedEnvelopeIdList will have the list of all sync failed envelope Ids. It will be null
        // if all envelopes are synced successfully
        // TODO: handle successful sync of the envelopes
    }
 
    public void onSyncAllEnvelopesError(DSException exception)
    {
        // TODO: handle error with envelopes syncing
    }
}
```

Support
===========

* Reach out via developer community on Stack Overflow, search the [DocuSignAPI](http://stackoverflow.com/questions/tagged/docusignapi) tag.
* Open an [issue](https://github.com/docusign/mobile-android-sdk/issues).

License
=======

The DocuSign Mobile Android SDK is licensed under the following [License](LICENSE.md).
