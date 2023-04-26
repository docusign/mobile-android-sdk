package com.reactnativesampleapp;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.docusign.androidsdk.DocuSign;
import com.docusign.androidsdk.delegates.DSAuthenticationDelegate;
import com.docusign.androidsdk.delegates.DSCustomSettingsDelegate;
import com.docusign.androidsdk.delegates.DSTemplateDelegate;
import com.docusign.androidsdk.dsmodels.DSUser;
import com.docusign.androidsdk.exceptions.DSAuthenticationException;
import com.docusign.androidsdk.exceptions.DSException;
import com.docusign.androidsdk.exceptions.DSSigningException;
import com.docusign.androidsdk.exceptions.DSTemplateException;
import com.docusign.androidsdk.exceptions.DocuSignNotInitializedException;
import com.docusign.androidsdk.listeners.DSAuthenticationListener;
import com.docusign.androidsdk.listeners.DSOnlineUseTemplateListener;
import com.docusign.androidsdk.util.DSMode;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class DocuSignSDKBridge extends ReactContextBaseJavaModule {

    DocuSign docuSignInstance = null;
    private ReactApplicationContext context = null;

    public DocuSignSDKBridge(ReactApplicationContext reactContext) {
        super(reactContext);
        context = reactContext;
    }

    @NonNull
    @Override
    public String getName() {
        return "DocuSignSDKBridge";
    }

    @ReactMethod
    public void init() throws DSException {
        docuSignInstance = DocuSign.init(context,
                Constants.DOCUSIGN_API_INTEGRATOR_KEY,
                Constants.DOCUSIGN_OAUTH_CLIENT_SECRET,
                Constants.DOCUSIGN_REDIRECT_URI,
                DSMode.DEBUG);
    }

    @ReactMethod
    public void login() throws DocuSignNotInitializedException {
        DSAuthenticationDelegate authenticationDelegate = DocuSign.getInstance().getAuthenticationDelegate();
        authenticationDelegate.login(100,
                context.getCurrentActivity(),
                new DSAuthenticationListener() {
                    @Override
                    public void onSuccess(@NonNull DSUser dsUser) {
                        Toast.makeText(context,
                                "Logged in successfully",
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(@NonNull DSAuthenticationException e) {
                        Toast.makeText(context,
                                "Failed to login: ${exception.message}",
                                Toast.LENGTH_LONG).show();
                    }

        });
    }

    @ReactMethod
    public void signOnline() throws DocuSignNotInitializedException, DSSigningException {

        DSCustomSettingsDelegate customSettingsDelegate = DocuSign.getInstance().getCustomSettingsDelegate();
        customSettingsDelegate.enableFileAccess(context.getCurrentActivity(), true);
        customSettingsDelegate.enableThirdPartyContentAccess(context.getCurrentActivity(), true);
        DSTemplateDelegate templateDelegate = DocuSign.getInstance().getTemplateDelegate();

        templateDelegate.useTemplateOnline(
                context.getCurrentActivity(),
                Constants.TEMPLATE_ID,
                null,
                new DSOnlineUseTemplateListener() {
                    @Override
                    public void onStart(@NonNull String s) {

                    }

                    @Override
                    public void onRecipientSigningSuccess(@NonNull String s, @NonNull String s1) {

                    }

                    @Override
                    public void onRecipientSigningError(@NonNull String s, @NonNull String s1, @NonNull DSTemplateException e) {

                    }

                    @Override
                    public void onError(@Nullable String s, @NonNull DSTemplateException e) {
                        Toast.makeText(context,
                                "Failed to sign online: ${exception.message}",
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete(@NonNull String s, boolean b) {
                        Toast.makeText(context,
                                "Signing successfully completed",
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancel(@NonNull String s, @NonNull String s1) {
                        Toast.makeText(context,
                                "Signing cancelled",
                                Toast.LENGTH_LONG).show();
                    }

        });
    }
}
