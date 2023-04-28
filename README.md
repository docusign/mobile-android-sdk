# DocuSign Android SDK Integration
DocuSign Android SDK provides the following features:
* Templates 
* Envelope creation
* Offline Signing and Online Signing of documents
* Syncing signed documents with DocuSign

## Credentials Needed
Before getting started, an Integration Key and valid Service User credentials are needed. The SDK cannot be used without these.
### Integration Key
To use any DocuSign SDK or the REST API, an Integration Key is needed. Visit https://developers.docusign.com/ to obtain an Integration Key if one does not already exist. Note that an Integration Key is first provisioned on the DEMO environment, and then must be promoted to PROD when ready. 
### Email & Password
To use the DocuSign Android SDK, credentials are necessary. That user's credentials are what should be used in the Authentication section below.
## Supported versions
Android Studio version should be 3.4 and above. Apps which integrate with DocuSign SDK requires AndroidX.
compileSdkVersion and targetSdkVersion should be 29 and above.
DocuSign SDK supports android versions 5.0 and above (API level 21).
## Setup 
1.  In your application's root build.gradle file:

    ```gradle
    allprojects {
        repositories {
            google()
            jcenter()
    
            maven {
                url  "https://docucdn-a.akamaihd.net/prod/docusignandroidsdk"
            }
        }
        }

        android {
            defaultConfig {
                multiDexEnabled = true
            }
        }
    ```

    ### DocuSign SDK Components 
    #### Online Signing   

    If you would like to use Online Signing, add the following dependencies in your app's build.gradle file:

    ```gradle
        
        dependencies {
            implementation 'com.docusign:androidsdk:1.8.2'
            implementation 'com.docusign:sdk-common:1.8.2'
        }
        
    ```

    #### Offline Signing  

    If you would like to use Offline Signing, add the following dependencies in your app's build.gradle file:

    ```gradle
        
        dependencies {
            implementation 'com.docusign:androidsdk:1.8.2'
            implementation 'com.docusign:sdk-common:1.8.2'
            implementation 'com.docusign:sdk-offline-signing:1.8.2'
        }
    ```

    #### eSign REST API

    If you would like to access eSign REST API from SDK, add the following dependencies in your app's build.gradle file:

    ```gradle
    dependencies {
        implementation 'com.docusign:androidsdk:1.8.2'
        implementation 'com.docusign:sdk-common:1.8.2'
        implementation 'com.docusign:sdk-esign-api:1.8.2'
    }
    ```

2. If using CDN (as mentioned in the above steps) is not an option for downloading DocuSign Android SDK, then you can download SDK manually as separate library. The SDK is available at [release](https://github.com/docusign/mobile-android-sdk/tree/master/release).  

    In the app's build.gradle, add the following dependencies:
    ```gradle
    dependencies {
        implementation fileTree(dir: 'libs', include: ['*.*'])
    
        implementation 'io.reactivex.rxjava2:rxjava:2.2.9'
        implementation 'io.reactivex.rxjava2:rxandroid:2.1.0'
    
        implementation 'android.arch.lifecycle:viewmodel:1.1.1'
        implementation 'android.arch.lifecycle:extensions:1.1.1'
    
        implementation 'androidx.room:room-runtime:2.2.5'
        implementation 'androidx.room:room-rxjava2:2.2.5'
    
        implementation 'androidx.work:work-runtime:2.3.4'
        implementation 'androidx.work:work-rxjava2:2.3.4'
    
        implementation 'androidx.appcompat:appcompat:1.1.0'
        implementation 'androidx.recyclerview:recyclerview:1.1.0'
        implementation 'androidx.preference:preference-ktx:1.1.1'
        implementation 'com.android.support:design:28.0.0'
        implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
        implementation 'jp.wasabeef:richeditor-android:1.2.2'
        implementation 'com.edmodo:cropper:2.0.0'
        implementation 'androidx.multidex:multidex:2.0.1'
        implementation 'org.apache.commons:commons-lang3:3.4'
        implementation 'io.gsonfire:gson-fire:1.8.0'
        implementation 'io.swagger:swagger-annotations:1.5.18'
        implementation 'commons-codec:commons-codec:1.10'
        implementation 'com.squareup.okhttp3:okhttp:3.8.0'
        implementation 'com.squareup.okio:okio:2.2.2'
        implementation 'com.google.code.gson:gson:2.8.5'
        implementation 'org.slf4j:slf4j-api:1.7.22'
        implementation 'org.threeten:threetenbp:1.3.5'
    
        implementation 'com.squareup.retrofit2:retrofit:2.4.0'
        implementation 'com.squareup.retrofit2:converter-gson:2.4.0'
        implementation 'com.squareup.retrofit2:converter-scalars:2.4.0'
        implementation 'com.squareup.retrofit2:adapter-rxjava2:2.4.0'
    }
    ```
3. Make Application class extend MultiDexApplication (if it doesn't already)
4. Sync Gradle and/or build your application
5. If app release builds has minifyEnabled true, then go through the following:

   In the app, if Gradle Plugin version is >= 3.4.0, then the build system uses R8 for app size shrinking. Otherwise it uses ProGuard for app size shrinking.   
   
   If using ProGuard, make sure the Proguard version is >= 6.1.1. 
   If Proguard version is < 6.1.1, then you can include the Proguard as follows in your gradle build script:
   ```gradle
    build-script {
        dependencies {
          classpath 'com.android.tools.build:gradle:3.3.1'
          classpath 'net.sf.proguard:proguard-gradle:6.1.1'
        }
    }
   ```

   For ProGuard and R8, make sure the gradle build script as follows:

   ```gradle
    android {
        buildTypes {
	        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }   
    ```

   For ProGuard and R8, add the following ProGuard rules:

    ```
    -keepattributes Signature, InnerClasses, EnclosingMethod

    #Retrofit does reflection on method and parameter annotations.
    -keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

    #Retain service method parameters when optimizing.
    -keepclassmembers,allowshrinking,allowobfuscation interface * {
     @retrofit2.http.* <methods>;
    }

   #Ignore annotation used for build tooling.
   -dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

   #Ignore JSR 305 annotations for embedding nullability information.
   -dontwarn javax.annotation.**

   #Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
   -dontwarn kotlin.Unit

   #Top-level functions that can only be used by Kotlin.
   -dontwarn retrofit2.KotlinExtensions
   -dontwarn retrofit2.KotlinExtensions$*

   #With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
   #and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
   -if interface * { @retrofit2.http.* <methods>; }
   -keep,allowobfuscation interface <1>

   #Optional third party libraries. You can safely ignore those warnings.
   -dontwarn com.squareup.okhttp.**
   -dontwarn com.squareup.picasso.**
   -dontwarn com.edmodo.cropper.**
   -dontwarn org.slf4j.impl.**

   #RxJava needs these two lines for proper operation.
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

   #JavaScript rules and classes required to be kept
   -keepclasseswithmembers class * {
      @android.webkit.JavascriptInterface <methods>;
   }

   -keepclasseswithmembernames class * {
      native <methods>;
   }

   -keepclassmembers class com.docusign.androidsdk.** { *; }
   -keep class com.docusign.androidsdk.** { *; }

   -keepclassmembers class com.docusign.esign.** { *; }
   -keep class com.docusign.esign.** { *; }

   -dontwarn kotlinx.coroutines.**
   -keep class kotlinx.coroutines.** { *; }
    ```

## API Documentation
Refer to DocuSign SDK api documentation at https://docusign.github.io/mobile-android-sdk/


## Initialization
In your application's Application class:
```java
DocuSign.init(
    this, // the Application Context
    "[YOUR INTEGRATION KEY HERE]", // recommend not hard-coding this
    DSMode.DEBUG // this controls the logging (logcat) behavior
);
```

### Initialization for OAuth Login
In your application's Application class:
```java
DocuSign.init(
     this, // the Application Context
     "[YOUR INTEGRATION KEY HERE]", // Same as Client Id
     "[YOUR CLIENT SECRET KEY]",
     "[YOUR REDIRECT_URI]",
     DSMode.Debug  
);
DocuSign.getInstance().setEnvironment(DSEnvironment.DEMO_ENVIRONMENT); // For Demo environment. For production environment, use DSEnvironment.PRODUCTION_ENVIRONMENT
```

## Authentication
### Login
#### OAuth
Authenticates the DocuSign user using OAuth.
```java
// requestCode - This code will be returned in onActivityResult() of the calling activity
try {
    DSAuthenticationDelegate docusignAuthDelegate = DocuSign.getInstance().getAuthenticationDelegate();
    docusignAuthDelegate.login(requestCode, context,
        new DSAuthenticationListener() {
            @Override
            public void onSuccess(@NonNull DSUser user) {
                // TODO: handle successful authentication here
            }
 
            @Override
            public void onError(@NonNull DSAuthenticationException exception) {
                // TODO: handle authentication failure here
            }
        }
    );
} catch (DocuSignNotInitializedException exception) {
    // TODO: handle error. This means the SDK object was not properly initialized
}
```

#### AuthToken
Authenticates the DocuSign user with the provided authToken and optional refreshToken.
```java
// accessToken - Access Token which authenticates the user
// refreshToken - If the access token can be refreshed, the refresh token. Optional
// expiresIn - The number of seconds from the time the access token was provisioned to when it will expire
try {
    DSAuthenticationDelegate docusignAuthDelegate = DocuSign.getInstance().getAuthenticationDelegate();
    docusignAuthDelegate.login(accessToken, refreshToken, expiresIn, context,
        new DSAuthenticationListener() {
            @Override
            public void onSuccess(@NonNull DSUser user) {
                // TODO: handle successful authentication here
            }
 
            @Override
            public void onError(@NonNull DSAuthenticationException exception) {
                // TODO: handle authentication failure here
            }
        }
    );
} catch (DocuSignNotInitializedException exception) {
    // TODO: handle error. This means the SDK object was not properly initialized
}
```

### Logout
Logout the authenticated DocuSign user.
```java
// Clears the DocuSign cached data
Boolean clearCachedData = true
 
try {
    DSAuthenticationDelegate docusignAuthDelegate = DocuSign.getInstance().getAuthenticationDelegate();
    authenticationDelegate.logout(this, clearCachedData, new DSLogoutListener() {
     @Override
      public void onSuccess() {
         // TODO: handle successful logout here
      }
 
     @Override
     public void onError(@NonNull DSAuthenticationException e) {
        // TODO: handle logout failure here
     }
});
} catch (DocuSignNotInitializedException exception) {
    // TODO: handle error. This means the SDK object was not properly initialized
}
```

## Templates
### Get Templates
Retrieves the list of templates.
```java
DSTemplateDelegate templateDelegate = DocuSign.getInstance().getTemplateDelegate();
DSTemplatesFilter filter = DSTemplatesFilter(count, null, null, startPosition);
// count is no. of templates to be retrieved
// startPosition is the startPosition/index of the templates retrieval.
templateDelegate.getTemplates(filter, new DSTemplateListListener() {
    @Override
        public void onStart() {
            // TODO: Handle when the templates retrieval process is started
    }
     
    @Override
    public void onComplete(DSTemplates templates) {
            // TODO: Handle templates that are retrieved   
        }
     
    @Overridee
    public void onError(DSTemplateException exception) {
        // TODO: Handle error when there is an exception while retrieving templates
    }
);
```

### Retrieve Downloaded Templates
Retrieves the list of downloaded templates.
```java
DSTemplateDelegate templateDelegate = DocuSign.getInstance().getTemplateDelegate();
templateDelegate.retrieveDownloadedTemplates(new DSTemplateListListener() {
    @Override
        public void onStart() {
            // TODO: Handle when retrieval of downloaded templates process is started
    }
     
    @Override
    public void onComplete(DSTemplates templates) {
            // TODO: Handle downloaded templates that are retrieved
        }
     
    @Override
    public void onError(DSTemplateException exception) {
        // TODO: Handle error when there is an exception while retrieving downloaded templates
    }
});
```

### GetTemplate
Fetches the template.
```java
DSTemplateDelegate templateDelegate = DocuSign.getInstance().getTemplateDelegate();
templateDelegate.getTemplate(templateId, null, new DSTemplateListener(){
    @Override
    public void onComplete(DSTemplateDefinition template) {
        // TODO: Handle template that is retrieved
    }
 
    @Override
    public void onError(DSTemplateException exception) {
        // TODO: Handle error when there is an exception while retrieving the template
    }
});
```

### CacheTemplate
Caches the template.
```java
DSTemplateDelegate templateDelegate = DocuSign.getInstance().getTemplateDelegate();
templateDelegate.cacheTemplate(templateId, new DSCacheTemplateListener(){
    @Override
    public void onStart() {
        // TODO: Handle when caching of template proves is started
    }
 
    @Override
    public void onComplete(DSTemplate template) {
        // TODO: Handle template that is cached
    }
     
    @Override
    public void onError(DSTemplateException exception) {
        // TODO: Handle error when there is an exception while caching the template
    }  
});
```

### RetrieveCachedTemplate
Retrieves the cached template.
```java
DSTemplateDelegate templateDelegate = DocuSign.getInstance().getTemplateDelegate();
templateDelegate.retrieveCachedTemplate(templateId, new DSGetCachedTemplateListener(){
    @Override
    public void onComplete(DSTemplateDefinition template) {
        // TODO: Handle cached template
    }
 
    @Override
    public void onError(DSTemplateException exception) {
        // TODO: Handle error when there is an exception while retrieving cached template
    }
});
```

### RemoveCachedTemplate
Removes the cached template.
```java
DSTemplateDelegate templateDelegate = DocuSign.getInstance().getTemplateDelegate();
// DSTemplateDefinition template. Refer to javadoc for more info about DSTemplateDefintion.
templateDelegate.removeCachedTemplate(template, new DSRemoveTemplateListener(){
    @Override
    public void onTemplateRemoved(boolean isRemoved) {
        // TODO: Handle when the template has been removed.
    }
});
```

### UseTemplate
#### UseTemplate Offline
Use the template and completes offline signing.
```java
DSTemplateDelegate templateDelegate = DocuSign.getInstance().getTemplateDelegate();
templateDelegate.useTemplateOffline(
                context,
                templateId,
                envelopeDefaults, // This can be used to pre-fill the template values such as recipients, emails, tabs etc. Refer to javadoc for more info about DSEnvelopeDefaults.
                pdfFileUri, // PDF file to append to the beginning or end of resulting envelope
                insertPdfAtPosition,  // Whether to insert the PDF at the beginning or end
                new DSOfflineUseTemplateListener() {
                    @Override
                    public void onComplete(@NotNull String envelopeId) {
                        // TODO: Handle when the template has been successfully signed.
                    }
                     
                    @Override
                    public void onError(@NotNull DSTemplateException exception) {
                        // TODO: Handle error when there is an exception while using the template or during signing
                    }
 
                    @Override
                    public void onCancel(@NotNull String templateId, String envelopeId) {
                        // TODO: Handle when the signing ceremony is cancelled
                    }
                });
```

#### UseTemplate Online
Use the template and completes online signing.
```java
DSTemplateDelegate templateDelegate = DocuSign.getInstance().getTemplateDelegate();
templateDelegate.useTemplateOnline(
        context,
        templateId,
        envelopeDefaults, // This can be used to pre-fill the template values such as recipients, emails, tabs etc. Refer to javadoc for more info about DSEnvelopeDefaults.
        new DSOnlineUseTemplateListener() {
            @Override
            public void onComplete(@NotNull String envelopeId, boolean onlySent) {
                // TODO: Handle when the online signing ceremony for all signers has been successful
            }

            @Override
            public void onStart(String envelopeId) {
                // TODO: Handle when the online signing is started
            }

            @Override
            public void onRecipientSigningSuccess(String envelopeId, String recipientId) {
                // TODO: Handle when the online signing success for a signer is successful
            }

            @Override
            public void onRecipientSigningError(String envelopeId, String recipientId, DSTemplateException exception) {
                // TODO: Handle when the error occurred during online signing for a signer
            }
                
            @Override
            public void onCancel(String envelopeId, String recipientId) {
                // TODO: Handle when the online signing ceremony is cancelled
            }

            @Override
            public void onError(String envelopeId, DSTemplateException exception) {
                // TODO: Handle when the error occurred during online signing ceremony
            }
        });
```

### UpdateTemplate
Updates the cached template.
```java
DSTemplateDelegate templateDelegate = DocuSign.getInstance().getTemplateDelegate();
templateDelegate.updatedCachedTemplate(templateId, new DSUpdateCachedTemplateListener(){
    @Override
    public void onComplete(DSTemplate template, Boolean updateAvailable) {
        // TODO: Handle when template is updated
    }
     
    @Override
    public void onError(DSTemplateException exception) {
        // TODO: Handle error when there is an exception while updating the template
    }
});
```

### Templates usage scenario
1. Get the list of templates with getTemplates
2. Get a specific template with getTemplate
3. Decide to save it locally with cacheTemplate
4. Can list all cached/downloaded templates with retrieveDownloadedTemplates
5. Can access a specific cached template with retrieveCachedTemplate
6. Can clear the template from the cache with removeCachedTemplate
7. Can update a cached template with updatedCachedTemplate

## Envelope Creation
The following example shows how to build an Envelope with one document, two signer recipients and one CC recipient. Each signer recipient has one signature tab. It also includes some metadata. 
```java
try {
            DSUser user = DocuSign.getInstance().getAuthenticationDelegate().getLoggedInUser(context);
            DSEnvelope  envelope = new DSEnvelope.Builder()
                    .envelopeName("[ENVELOPE NAME HERE]")
                    .document(new DSDocument.Builder()
                            .documentId("1")
                            .uri("file://path_to_your_pdf_file_here")
                            .name("[DOCUMENT NAME HERE]")
                            .build())
                    .recipient(new DSEnvelopeRecipient.Builder()
                            .recipientId("1")
                            .routingOrder(1)
                            .hostName(user.getName()) // this should be the user name returned in AuthenticationListener.onSuccess
                            .hostEmail(user.getEmail()) // this should be the user email returned in AuthenticationListener.onSuccess
                            .signerName("John Doe")
                            .signerEmail("john.doe@abc.com")
                            .type(DSRecipientType.IN_PERSON_SIGNER)
                            .tab(new DSTab.Builder()
                                    .documentId("1")
                                    .recipientId("1")
                                    .pageNumber(1) // the page on which this tab should appear
                                    .xPosition(123) // the x-coordinate on page 1 where you want the signer's signature
                                    .yPosition(123) // the y-coordinate on page 1 where you want the signer's signature
                                    .type(DSTabType.SIGNATURE)
                                    .build())
                            .build())
                    .recipient(new DSEnvelopeRecipient.Builder()
                            .recipientId("2")
                            .routingOrder(1)
                            .hostName(user.getName()) // this should be the user name returned in AuthenticationListener.onSuccess
                            .hostEmail(user.getEmail()) // this should be the user email returned in AuthenticationListener.onSuccess
                            .signerName("Jane Doe")
                            .signerEmail("jane.doe@abc.com")
                            .type(DSRecipientType.IN_PERSON_SIGNER)
                            .tab(new DSTab.Builder()
                                    .documentId("1")
                                    .recipientId("2")
                                    .pageNumber(1) // the page on which this tab should appear
                                    .xPosition(456) // the x-coordinate on page 1 where you want the signer's signature
                                    .yPosition(456) // the y-coordinate on page 1 where you want the signer's signature
                                    .type(DSTabType.SIGNATURE)
                                    .build())
                            .build())
                    .recipient(new DSEnvelopeRecipient.Builder() // this recipient receives a copy
                            .recipientId("3")
                            .routingOrder(2)
                            .signerName("Jack Doe") // if someone needs a signed copy, their name here
                            .signerEmail("jack.doe@abc.com") // if someone needs a signed copy, their valid email here
                            .type(DSRecipientType.CARBON_COPY)
                            .build())
                    .textCustomField(
                            new DSTextCustomField.Builder() // this is for free-form metadata
                                    .fieldId(123)
                                    .name("metadata1")
                                    .value("some value")
                                    .build()
                    )
                    .build();
        } catch (DSEnvelopeException | DSAuthenticationException exception) {
            // TODO: handle errors with envelope creation. exception.getMessage() will indicate what went wrong
        }
}
```

## Sending/Storing an Envelope
The following example assumes the envelope object from the above Envelope Creation section.
```java
try {
    DSEnvelopeDelegate docusignEnvelopeDelegate = DocuSign.getInstance().getEnvelopeDelegate();
    docusignEnvelopeDelegate.composeAndSendEnvelope(envelope, new ComposeAndSendEnvelopeListener() {
        @Override
        public void onSuccess(@NonNull String envelopeId) {
            // At this point, the Envelope will be successfully created in local db store
            // TODO: this is where Signing will kickoff
        }
 
        @Override
        public void onError(@NonNull DSEnvelopeException exception) {
            // TODO: handle failed envelope creation. exception.getMessage() will indicate what went wrong
        }
    });
} catch (DocuSignNotInitializedException exception) {
    // TODO: handle error. This means the SDK object was not properly initialized
}
```

## Deleting an Envelope from local storage
The following example assumes you know the envelopeId you want to delete.
```java
try {
    DSEnvelopeDelegate docusignEnvelopeDelegate = DocuSign.getInstance().getEnvelopeDelegate();
    docusignEnvelopeDelegate.deleteCachedEnvelope(envelopeId, new DSDeleteCachedEnvelopeListener() {
        @Override
        public void onSuccess(@NonNull String envelopeId) {
            // TODO: handle successful envelope deletion
        }
 
        @Override
        public void onError(@NonNull DSEnvelopeException exception) {
            // TODO: handle error with envelope deletion. exception.getMessage() will indicate what went wrong
        }
    });
} catch (DocuSignNotInitializedException exception) {
    // TODO: handle error. This means the SDK object was not properly initialized
}
```

## Signing an Envelope
### Signing offline
Signing an envelope offline

```java
DSSigningDelegate signingDelegate = DocuSign.getInstance().getSigningDelegate();
signingDelegate.signOffline(
                context,
                envelopeId,  // envelopeId of the envelope which is created locally
                new DSOfflineSigningListener() {
                    @Override
                    public void onSuccess(@NotNull String envelopeId) {
                        // TODO: Handle when envelope is successfully signed offline
                    }
                     
                    @Override
                    public void onError(@NotNull DSSigningException exception) {
                        // TODO: Handle when error occurred during offline signing ceremony.
                    }
 
                    @Override
                    public void onCancel(@NotNull String envelopeId) {
                        // TODO: Handle when offline signing ceremony is cancelled
                    }
                });
```

### Signing online
Signing an envelope online

```java
DSSigningDelegate signingDelegate = DocuSign.getInstance().getSigningDelegate();
signingDelegate.signOnline(
                context,
                serverEnvelopeId,      // Envelope Id of the envelope created in DocuSign portal
                new DSOnlineSigningListener() {
                    @Override
                    public void onStart(@NotNull String envelopeId) {
                        // TODO: Handle when Online Signing started
                    }
 
                    @Override
                    public void onSuccess(@NotNull String envelopeId) {
                        // TODO: Handle when Online Signing ceremony is successful
                    }
                     
                    @Override
                    public void onError(String envelopeId, @NotNull DSSigningException exception) {
                        // TODO: Handle when error occurred during Online Signing ceremony
                    }
 
                    @Override
                    public void onCancel(@NotNull String envelopeId, @NotNull String recipientId) {
                        // TODO: Handle when Online Signing ceremony is cancelled
                    }
 
                    @Override
                    public void onRecipientSigningError(@NotNull String envelopeId, @NotNull String recipientId, @NotNull DSSigningException exception) {
                        // TODO: Handle when error occurred during Online Signing ceremony for a signer
                    }
 
                    @Override
                    public void onRecipientSigningSuccess(@NotNull String envelopeId, @NotNull String recipientId) {
                        // TODO: Handle when Online Signing is successful for a signer
                    }
                });
```



## Syncing an envelope
The following example assumes you know the envelopeId you want to sync.
```java
try {
    DSEnvelopeDelegate envelopeDelegate = DocuSign.getInstance().getEnvelopeDelegate();
    envelopeDelegate.syncEnvelope(envelopeId, new DSSyncEnvelopeListener() {
        @Override
        public void onSuccess(@NonNull String localEnvelopeId, String serverEnvelopeId) {
            // At this point, envelope with localEnvelopeId is synced successfully and a new envelope Id is returned from
            // server which can be accessed using serverEnvelopeId
            // TODO: handle successful envelope syncing
        }
 
        @Override
        public void onError(@NonNull DSSyncException exception, @NonNull String localEnvelopeId, Integer syncRetryCount) {
            // At this point, envelope with localEnvelopeId failed to sync. DSSyncException errorCode and errorMsg gives
            // the reason for sync failure. syncRetryCount tells the number of times envelope sync failed
            // TODO: handle error with envelope syncing. exception.getMessage() will indicate what went wrong
        }
    }, true); // passing true, deletes the envelope in Database after syncing it to the cloud.
              // Setting it to false, will retain the envelope in db with all the necessary information. An explicit clean up is required on your end to keep the db clean.
} catch (DocuSignNotInitializedException exception) {
    // TODO: handle error. This means the SDK object was not properly initialized
}
```

## Syncing all envelopes
The following example will sync all Sync Pending envelopes.
```java
try {
    DSEnvelopeDelegate envelopeDelegate = DocuSign.getInstance().getEnvelopeDelegate();
    envelopeDelegate.syncAllEnvelopes(new DSSyncAllEnvelopesListener() {
        @Override
        public void onStart() {
            // TODO: handle on start of syncing of all envelopes
        }
        @Override
        public void onEnvelopeSyncSuccess(@NonNull String localEnvelopeId, String serverEnvelopeId) {
            // At this point, envelope with localEnvelopeId is synced successfully and
            // server returned the synced envelope Id: serverEnvelopeId
            // TODO: handle successful envelope sync of envelope with Id localenvelopeId
        }
 
        @Override
        public void onEnvelopeSyncError(@NonNull DSSyncException exception, @NonNull String localEnvelopeId, Integer syncRetryCount) {
            // At this point, envelope with localEnvelopeId failed to sync. DSSyncException errorCode and errorMsg gives
            // the reason for sync failure. syncRetryCount tells the number of times envelope sync failed
            // TODO: handle error with envelope syncing of envelope with Id localenvelopeId
        }
        @Override
        public void onComplete(@Nullable List<String> failedEnvelopeIdList) {
            // failedEnvelopeIdList will have the list of all sync failed envelope Ids. It will be null
            // if all envelopes are synced successfully
            // TODO: handle successful sync of the envelopes
        }
 
        @Override
        public void onError(DSException exception) {
                // TODO: handle error with envelopes syncing
        }
    }, true); // passing true, deletes the envelopes in Database after synching it to the cloud.
             // Setting it to false, will retain the envelopes in db with all the necessary information. An explicit clean up is required on your end to keep the db clean.
} catch (DocuSignNotInitializedException exception) {
    // TODO: handle error. This means the SDK object was not properly initialized
}
```

## Transition from demo environment to production environment
If you’re using the demo environment, check that you set the environment after creating the DocuSign instance as follows:
```java
    DocuSign.getInstance().setEnvironment(DSEnvironment.DEMO_ENVIRONMENT);
```

If you’re using the production environment, check that you set the environment after creating the DocuSign instance as follows:
```java
    DocuSign.getInstance().setEnvironment(DSEnvironment.PRODUCTION_ENVIRONMENT);
```

## ESign REST API invocation from SDK
The following example shows how to retrieve user signature using eSign REST API from DocuSign Android SDK:
### Java
```java
private void getUserSignatureInfo() {
            DSESignApiDelegate eSignApiDelegate = DocuSign.getInstance().getESignApiDelegate();
            final UsersApi usersApi = eSignApiDelegate.createApiService(UsersApi.class);
            if (usersApi != null) {
                DSAuthenticationDelegate authDelegate = DocuSign.getInstance().getAuthenticationDelegate();
                final DSUser user = authDelegate.getLoggedInUser(getApplicationContext());

                eSignApiDelegate.invoke(new DSESignApiListener() {

                    @Override
                    public <T> void onSuccess(T response) {
                        if (response instanceof UserSignaturesInformation) {
                            UserSignature userSignature = ((UserSignaturesInformation) response).getUserSignatures().get(0);
                            Log.d(TAG, "Signature Id: " + userSignature.getSignatureId());
                        }
                    }

                    @Override
                    public void onError(@NotNull DSRestException exception) {
                        // TODO: Handle error
                    }
                }, new Function0<Call<UserSignaturesInformation>>(){

                    @Override
                    public Call<UserSignaturesInformation> invoke() {
                        return usersApi.userSignaturesGetUserSignatures(user.getAccountId(), user.getUserId(), "signature");
                    }
                });
            }
}
```

### Kotlin
``` kotlin
private fun getUserSignatureInfo() {
  val eSignApiDelegate = DocuSign.getInstance().getESignApiDelegate()
  val usersApi = eSignApiDelegate.createApiService(UsersApi::class.java)

  val authDelegate = DocuSign.getInstance().getAuthenticationDelegate()
  val user = authDelegate.getLoggedInUser(context)
  eSignApiDelegate.invoke(object : DSESignApiListener {

                            override fun <T> onSuccess(response: T?) {
                                if (response is UserSignaturesInformation) {
                                    val userSignature = (response as UserSignaturesInformation).getUserSignatures().get(0)
                                    Log.d(TAG, "Signature Id: " + userSignature.getSignatureId());
                                }
                            }

                            override fun onError(exception: DSRestException) {
                                // TODO: Handle error
                            }
                            }) {
                                usersApi.userSignaturesGetUserSignatures(user.getAccountId(), user.getUserId(), "signature")
                            }
}
```

## Captive Signing
To perform captive signing using signing URL, you can invoke the following API:
```java
    DSSigningDelegate signingDelegate = DocuSign.getInstance().getSigningDelegate();
        signingDelegate.launchCaptiveSigning(context,
                signingURL,
                envelopeId,
                recipientId,
                new DSCaptiveSigningListener() {

                    @Override
                    public void onSuccess(@NonNull String envelopeId) {
                        // TODO: Handle when captive signing ceremony is succefully completed.
                    }

                    @Override
                    public void onStart(@NonNull String envelopeId) {
                        // TODO: Handle when captive signing ceremony is about to start
                    }

                    @Override
                    public void onRecipientSigningSuccess(@NonNull String envelopeId, @NonNull String recipientId) {
                        // TODO: Handle when recipient finished signing
                    }

                    @Override
                    public void onRecipientSigningError(@NonNull String envelopeId, @NonNull String recipientId, @NonNull DSSigningException exception) {
                        // TODO: Handle when error occured during recipient signing
                    }

                    @Override
                    public void onError(String envelopeId, DSSigningException exception) {
                        // TODO: Handle when error occured during captive signing ceremony
                    }

                    @Override
                    public void onCancel(@NonNull String envelopeId, @NonNull String recipientId) {
                        // TODO: Handle when captive signing ceremony is cancelld.
                    }
                });
```

## Auto place tags
You can create local envelope using Auto place tags based on the anchor string instead of x, y positions using the following API:

```java
    try {
            DSUser user = DocuSign.getInstance().getAuthenticationDelegate().getLoggedInUser(context);
            DSEnvelope  envelope = new DSEnvelope.Builder()
                    .envelopeName("[ENVELOPE NAME HERE]")
                    .document(new DSDocument.Builder()
                            .documentId("1")
                            .uri("file://path_to_your_pdf_file_here")
                            .name("[DOCUMENT NAME HERE]")
                            .build())
                    .recipient(new DSEnvelopeRecipient.Builder()
                            .recipientId("1")
                            .routingOrder(1)
                            .hostName(user.getName()) // this should be the user name returned in AuthenticationListener.onSuccess
                            .hostEmail(user.getEmail()) // this should be the user email returned in AuthenticationListener.onSuccess
                            .signerName("John Doe")
                            .signerEmail("john.doe@abc.com")
                            .type(DSRecipientType.IN_PERSON_SIGNER)
                            .tab(new DSTab.Builder()
                                    .documentId("1")
                                    .recipientId("1")
                                    .pageNumber(1) // the page on which this tab should appear
                                    .anchorString("Signature") // Auto places signature tags in the document based on the specified anchor string.
                                    .type(DSTabType.SIGNATURE)
                                    .build())
                            .build())
                    .recipient(new DSEnvelopeRecipient.Builder()
                            .recipientId("2")
                            .routingOrder(1)
                            .hostName(user.getName()) // this should be the user name returned in AuthenticationListener.onSuccess
                            .hostEmail(user.getEmail()) // this should be the user email returned in AuthenticationListener.onSuccess
                            .signerName("John Doe")
                            .signerEmail("john.doe@abc.com")
                            .type(DSRecipientType.IN_PERSON_SIGNER)
                            .tab(new DSTab.Builder()
                                    .documentId("1")
                                    .recipientId("2")
                                    .pageNumber(1) // the page on which this tab should appear
                                    .anchorString("Signature") // Auto places signature tags in the document based on the specified anchor string.
                                    .type(DSTabType.SIGNATURE)
                                    .build())
                            .build())
                    .recipient(new DSEnvelopeRecipient.Builder() // this recipient receives a copy
                            .recipientId("3")
                            .routingOrder(2)
                            .signerName("Jack Doe") // if someone needs a signed copy, their name here
                            .signerEmail("jack.doe@abc.com") // if someone needs a signed copy, their valid email here
                            .type(DSRecipientType.CARBON_COPY)
                            .build())
                    .textCustomField(
                            new DSTextCustomField.Builder() // this is for free-form metadata
                                    .fieldId(123)
                                    .name("metadata1")
                                    .value("some value")
                                    .build()
                    )
                    .build();
        } catch (DSEnvelopeException | DSAuthenticationException exception) {
            // TODO: handle errors with envelope creation. exception.getMessage() will indicate what went wrong
        }
```

## SDK Documentation
* [SDK API Docs](./docs)
* [SDK eSign API Docs](./docs)

Support
===========
* [FAQ for DocuSign Android SDK](https://www.docusign.com/blog/developers/docusign-android-sdk-faq) 
* Reach out via developer community on Stack Overflow, search the [DocuSignAPI](https://stackoverflow.com/questions/tagged/docusignapi) tag.
* Open an [issue](https://github.com/docusign/mobile-android-sdk/issues).

License
=======

The DocuSign Mobile Android SDK is licensed under the following [License](LICENSE.md).
