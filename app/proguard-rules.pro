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
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}


-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }


-dontwarn android.net.http.AndroidHttpClient
-dontwarn com.google.android.gms.**

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }


# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
##---------------End: proguard configuration for Gson  ----------




# OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
-dontwarn com.google.android.gms.**
-dontwarn com.android.volley.toolbox.**


-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**
-keep class com.paysky.upg.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**



# Retrofit 2.X
## https://square.github.io/retrofit/ ##

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}


-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}


-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

-keep class android.support.v7.widget.RoundRectDrawable { *; }
-keep class com.daimajia.androidanimations.** { *; }
-keep interface com.daimajia.androidanimations.** { *; }

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-dontwarn com.daimajia.
-keep class com.daimajia.easing. { *; }
-keep interface com.daimajia.easing. { *; }
-keep public class com.daimajia.androidanimations. { *; }
-keep public class com.daimajia.androidanimations.library.specials.in.LandingAnimator. { *; }
-keep class com.daimajia.androidanimations.** { *; }
-keep interface com.daimajia.androidanimations.** { *; }
-keep class com.daimajia.* { *; }
-keep interface com.daimajia.* { *; }
-keep public class com.daimajia.* { *; }
-dontwarn okio.**

-keep class com.daimajia.** { *; }
           -dontwarn com.daimajia.**
           -keepnames class com.daimajia.**

           -dontwarn okhttp3.internal.platform.*




# Retrofit
-dontnote retrofit2.Platform # Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor # Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8 # Platform used when running on Java 8 VMs. Will not be used at runtime.

# OkHttp 3
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**



-keepattributes Signature, *Annotation*, EnclosingMethod
-keep class almeros.android.multitouch.gesturedetectors.** { *; }
-keep class com.mapbox.mapboxsdk.** { *; }
-keep interface com.mapbox.mapboxsdk.** { *; }
-keep class com.mapbox.services.android.telemetry.** { *; }
-keep class com.mapbox.services.commons.** { *;}
-keep class com.google.**
-keep class com.google.gson.** { *; }
-keep interface com.google.gson.** { *; }
-keep class q.rorbin.badgeview.** {*;}
-keep interface q.rorbin.badgeview.** {*;}
-keep class com.orhanobut.logger.** {*;}
-keep interface com.orhanobut.logger.** {*;}
-keep class me.leolin.shortcutbadger.**{*;}
-keep interface me.leolin.shortcutbadger.**{*;}
-keep class uk.co.chrisjenx.calligraphy.**{*;}
-keep interface uk.co.chrisjenx.calligraphy.**{*;}
-keep class com.zcw.togglebutton.**{*;}
-keep interface com.zcw.togglebutton.**{*;}
-keep class com.github.mikephil.chartin.**{*;}
-keep interface com.github.mikephil.chartin.**{*;}
-keep class com.borax12.materialdaterangepicker.**{*;}
-keep class com.borax12.materialdaterangepicker.**{*;}
-keep class com.airbnb.lottie.**{*;}
-keep class com.github.mikephil.charting.**{*;}
-keep class com.google.android.gms.**{*;}
-keep class com.tozny.crypto.android.AesCbcWithIntegrity$PrngFixes$* { *; }
-keep class io.card.payment.**{*;}



# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform



-ignorewarnings

# MeiZuFingerprint
-keep class com.fingerprints.service.** { *; }

# SmsungFingerprint
-keep class com.samsung.android.sdk.** { *; }


-keepclassmembers class * {
    @pub.devrel.easypermissions.AfterPermissionGranted <methods>;
}