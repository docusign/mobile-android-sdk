# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

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

-dontwarn kotlinx.coroutines.**
-keep class kotlinx.coroutines.** { *; }
