# DocuSign Android SDK Changelog

## [v1.5.3.1] - DocuSign Android SDK 05-05-2022

### Fixed
* Bug fixes and Improvements

## [v1.5.5] - DocuSign Android SDK 04-18-2022

### Added
* Added envelopeIdStamping in DSEnvelope to control the display of envelope Id stamp in the completed document
* Made Integrator key optional during initialization of SDK

### Fixed
* Bug fixes and Improvements

## [v1.5.4] - DocuSign Android SDK 03-23-2022

### Fixed
* Bug fixes and Improvements


## [v1.5.3] - DocuSign Android SDK 01-31-2022

### Added
* Brand Id support in embedded signing for the envelopes created using DSEnvelope Builder and templates

### Fixed
* Bug fixes and Improvements

## [v1.5.1] - DocuSign Android SDK 01-18-2022

### Fixed
* Bug fixes and Improvements

## [v1.5.0] - DocuSign Android SDK 12-09-2021

### Added
* Captive Signing URL support
* Android SDK 12 support
* SDK CustomSettings API to suppress 'Decline to Sign' option in Online Signing
* SDK API to get IDs of downloaded envelopes and templates
* SDK Performance monitoring metrics
* Exiting Signing does not delete cached envelope

### Fixed
* Disabling checkbox tab programatically during Online signing
* Bug fixes and Improvements

## [v1.4.0] - DocuSign Android SDK 09-29-2021

### Added
* Offline Signing as separate SDK
* Offline Signing with Photo API
* Login based on Account Id
* Use existing signature during Online Signing
* Settings API to control displaying 'Signing complete' screen

### Fixed
* Bug fixes and Improvements

## [v1.3.0] - DocuSign Android SDK 07-08-2021

### Added
* eSign REST API access in SDK
* Captive Signing

### Fixed
* Bug fixes and Improvements

## [v1.2.0] - DocuSign Android SDK 05-19-2021

### Added
* Support for EMAIL_ADDRESS tag for Offline Signing using templates
* Dark mode support

### Fixed
* Bug fixes and Improvements

## [v1.1.10] - DocuSign Android SDK 04-28-2021

### Fixed
* Support email content in template

## [v1.1.9] - DocuSign Android SDK 03-17-2021

### Fixed
* Optimized login with access token api

## [v1.1.7] - DocuSign Android SDK 03-08-2021

### Added
* Provided API's via CustomSettings to enable or disable the download of AccountSettings and Consumer Disclosure while Login
* By default, sequential syncing is enabled, Provided an API to enable envelope syncing in parallel
* Disabled syncing when device is on 2G Network

## [v1.1.7] - DocuSign Android SDK 02-24-2021

### Fixed
* Fixed duplicate recipients entries in the Database
* Included recipients validation on the envelope defaults object

## [v1.1.6] - DocuSign Android SDK 02-02-2021

### Fixed
* Fixed text tab fixed width and font size issue

## [v1.1.5] - DocuSign Android SDK 01-11-2021

### Fixed
* Memory leak and performance issues
* Other bug fixes and enhancements

## [v1.1.4] - DocuSign Android SDK 12-07-2020

### Fixed
* Fixed bug related to downloading the combined document of a completed envelope

## [v1.1.3] - DocuSign Android SDK 12-01-2020

### Added
* Added support for app release builds using R8  

## [v1.1.2] - DocuSign Android SDK 11-18-2020

### Fixed
* Fixed border displayed around read only tabs during signing
* Bug fixes and Improvements

### Added
* Added api to control displaying host signing screen while using template for Online and Offline Signing
* Added api to retrieve envelope info after syncing
* Added Online Signing for Xamarin
* Added displaying Private message in Online Signing
* Added more Online Signing and Offline Signing telemetry events

## [v1.1.1] - DocuSign Android SDK 10-15-2020

### Fixed
* Tab values set using prefill envelope defaults for a template are not reflected in the document during Online Signing

## [v1.1.0] - DocuSign Android SDK 10-09-2020

### Added
* Added Online Signing feature
* Added useTemplateOnline() api in TemplateDelegate to use template and EnvelopeDefaults for Online Signing
* Added useTemplateOffline() api in TemplateDelegate to use template and EnvelopeDefaults for Offline Signing 
* Added signOnline() api in SigningDelegate to sign document Online
* Added signOffline() api in SigningDelegate to sign document Offline
* Added API to download the completed envelope

### Removed
* Removed useTemplate() api in TemplateDelegate
* Removed sign() api in SigningDelegate

### Fixed
* Bug fixes and improvements
* Removed RAM requirements


## [v1.0.4] - DocuSign Android SDK 09-17-2020

### Added
* Partial support for branding feature
* Attaching a pdf file to the template documents before offline signing via templates
* Many bug fixes and enhancements

## [v1.0.3] - DocuSign Android SDK 08-30-2020

### Fixed
* Syncing envelope crash with different Locale
* Editing read only tabs issue
* Consumer Disclosure issue for JWT Token login
* Crash when using online signing

## [v1.0.2] - DocuSign Android SDK 07-20-2020

### Fixed
* Removed external library dependencies from sdk artifact.
* Syncing envelope issue for large files during different network conditions

## [v1.0.0] - DocuSign Android SDK 07-01-2020

### Added
* Initial release of DocuSign Android SDK.


