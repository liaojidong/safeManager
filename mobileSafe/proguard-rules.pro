# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Administrator\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontoptimize
-verbose
-dontwarn **HoneycombMR2
-dontwarn **CompatICS
-dontwarn **Honeycomb
-dontwarn **CompatIcs*
-dontwarn **CompatFroyo
-dontwarn **CompatGingerbread
-dontwarn **JellyBean
-dontwarn com.umeng.analytics.*
-dontwarn com.umeng.common.*
-dontwarn com.umeng.common.a.*
-dontwarn com.umeng.common.b.*
-dontwarn com.umeng.common.net.*
-dontwarn android.webkit.*
-ignorewarnings

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keep class com.umeng.analytics.** {*;}
-keep class com.umeng.common.** {*;}
-keep class com.umeng.common.a.** {*;}
-keep class com.umeng.common.b.** {*;}
-keep class com.umeng.common.net.** {*;}
-keep public class com.bodong.dpaysdk.** {*;}


#不混淆注解
-keepattributes *Annotation*
-keep class * extends java.lang.annotation.Annotation { *; }

-dontwarn  android.**
-dontwarn  pl.droidsonroids.gif.**
-dontwarn com.handmark.pulltorefresh.library.**
-dontwarn com.nhaarman.listviewanimations.**
-dontwarn android.support.v4.**
-dontwarn com.alipay.apmobilesecuritysdk.**
-dontwarn com.nhaarman.listviewanimations.**
-dontwarn com.alipay.apmobilesecuritysdk.**
-dontwarn com.squareup.picasso.OkHttpDownloader.**
-dontwarn com.squareup.picasso.**
#可能运行不了
-dontwarn com.alipay.mobile.framework.service.annotation**

-keep class android.support.v4.**{*;}
-keep interface android.support.v4.app.** { *; }
#内部类不混淆
-keepattributes Exceptions,InnerClasses

-keep class com.baidu.** { *; }
-keep class vi.com.gdi.bgl.android.**{*;}

-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.**
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment
-keep public class * extends android.support.v4.app.Fragment
-keep public class android.** { public protected private *; }
-keep public class android.support.v4.** { public protected private *; }
-keep public class com.eventbus.EventBus


#不混淆第三方jar包中的类
-keep class com.google.gson.** { *; }
-keep class com.alipay.** { *; }
-keep class com.ta.** { *; }
-keep class com.ut.** { *; }
-keep class com.lidroid.** { *; }
-keep class com.tencent.** { *; }
-keep class android.support.** { *; }
-keep class com.nhaarman.** { *; }
-keep class com.nostra13.** { *; }
-keep class com.sina.** { *; }
-keep class com.squareup.** { *; }
-keep class com.eventbus.** { *; }
-keep class com.handmark.** { *; }
-keep class com.kyleduo.** { *; }
-keep class com.squareup.picasso.** { *; }
-keep class com.unionpay.** { *; }
-keep class tv.danmaku.ijk.** { *; }
-keep class com.pili.** { *; }
-keep class io.agora.** { *; }
-keep class me.relex.** { *; }
-keep class com.qiniu.** { *; }
-keep class cz.** { *; }
-keep class uk.co.** { *; }
-keep class com.jeremyfeinstein.slidingmenu.** { *; }
-keep class se.emilsjolander.stickylistheaders.** { *; }
-keep class com.kyleduo.switchbutton.** { *; }
-keep class com.github.mikephil.charting.** { *; }
-keep class info.hoang8f.** { *; }
-keep class net.tsz.afinal.** { *; }
-keep class com.dd.** { *; }
-keep class com.andexert.** { *; }
-keep class com.readystatesoftware.systembartint.** { *; }
-keep class com.jaredrummler.android.processes.** { *; }
-keep class com.codetroopers.betterpickers.** { *; }
-keep class com.nineoldandroids.** { *; }
-keep class com.baoyz.swipemenulistview.** { *; }

#不混淆模型
-keep class com.dong.mobilesafe.bean.** { *; }
-keep class com.dong.mobilesafe.domain.** { *; }
-keep class android.** { *; }
-keep class org.** { *; }
-keep class javax.** { *; }


-keep class pl.droidsonroids.** { *; }
-keep class com.loovee.common.jni.** { *; }

#EventBus的混淆配置
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
    protected void onEvent*(**);
    private void onEvent*(**);
}

-keepattributes *Annotation*
-keepattributes Signature

#所有native的方法不能去混淆.
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

 #某些构造方法不能去混淆
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);

}

#枚举类不能去混淆.
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#aidl文件不能去混淆.
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclasseswithmembers class *{
    public *;
}



-keepclassmembers class * extends android.app.Activity {

   public void *(android.view.View);

}

-keepclassmembers class * extends android.support.v4.app.Activity {

   public void *(android.view.View);

}


-keepclasseswithmembers class * {
     public <init>(android.content.Context, android.util.AttributeSet);
 }

-keepclasseswithmembers class * {
     public <init>(android.content.Context, android.util.AttributeSet, int);
 }

-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
 }



-keepclassmembers public class * extends android.view.View {

   void set*(***);

   *** get*();

}



-keep public class * extends android.widget.Adapter {*;}

-keep public class * extends android.widget.CusorAdapter{*;}

-keepnames class * implements java.io.Serializable

-keepclassmembers class * implements java.io.Serializable {

*;

}

#不要混淆序列化
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#不要混淆R文件
-keep public class com.dong.mobilesafe.R$*{
 *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


-dontwarn com.cnmeach.dm.**
-keep class com.cnmeach.dm.** { *; }
-keep class com.cnmeach.dm.android.ui.interaction.** {*;}
-keepattributes *Annotation*



-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.auth.AlipaySDK{ public *;}
-keep class com.alipay.sdk.auth.APAuthInfo{ public *;}
-keep class com.alipay.mobilesecuritysdk.*
-keep class com.ut.*

-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}


-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#友盟混淆配置
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keep public class com.loovee.reliao.R$*{
public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#个推
-dontwarn com.igexin.**
-keep class com.igexin.**{*;}

#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
