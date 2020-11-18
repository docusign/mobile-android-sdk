#  TGK Capital Sample Application
This is a sample Kotlin application which demonstrates the integration of DocuSign Android SDK.

## Credentials Needed
Before getting started, an Integration Key and valid Service User credentials are needed. The SDK cannot be used without these.
### Integration Key
To use any DocuSign SDK or the REST API, an Integration Key is needed. Visit https://developers.docusign.com/ to obtain an Integration Key if one does not already exist. Note that an Integration Key is first provisioned on the DEMO environment, and then must be promoted to PROD when ready. 
### Email & Password
To use the DocuSign Android SDK, credentials are necessary. That user's credentials are what should be used in the Authentication section below.

## Sample Application Usage
1. Launch Sample Application.
2. In Login screen, select Settings from Title toolbar. Enter IntegratorKey, ClientSecret and Redirect Uri.
3. Select Login in Login screen. Provide Docusign user name and password associated with the account.
4. User is navigated to Home screen after succeessful login. It displays list of appointments.
### Template flow 
#### Online Signing 
1. Device or emulator should have proper network and is not in airplane mode.
2. In Home screen, select 'Tom Wood'. Select 'View Agreement'. Select 'Invest in Portfolio'.
3. List of Templates are displayed.
4. Select 'TGK Capital Portfolio A Agreement' template.
5. Starts Online Signing ceremony.
6. Finish signing.
#### Offline Signing 
1. Device or emulator should be in airplane mode and no network.
2. In Home screen, select 'Tom Wood'. Select 'View Agreement'. Select 'Invest in Portfolio'.
3. List of Templates are displayed.
4. Select 'TGK Capital Portfolio A Agreement' template.
5. Starts Offline Signing ceremony.
6. Finish signing.
7. In Home screen, select 'Pending Sync' tab.
8. Sync the signed document.
### Envelope flow 
#### Online Signing 
1. Device or emulator should have proper network and is not in airplane mode.
2. In Home screen, select 'Andrea Kuhn'. Select 'View Agreement'. Select 'Invest in Portfolio'.
3. Starts Online Signing ceremony.
4. Finish signing.
#### Offline Signing 
1. Device or emulator should be in airplane mode and no network.
2. In Home screen, select 'Andrea Kuhn'. Select 'View Agreement'. Select 'Invest in Portfolio'.
3. Starts Offline Signing ceremony.
4. Finish signing.
5. In Home screen, select 'Pending Sync' tab.
6. Sync the signed document.
