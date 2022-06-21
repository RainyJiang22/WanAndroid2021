
-optimizationpasses 5

-dontusemixedcaseclassnames

-dontskipnonpubliclibraryclasses

-dontskipnonpubliclibraryclassmembers

-dontpreverify

-verbose
-printmapping priguardMapping.txt

-optimizations !code/simplification/artithmetic,!field/*,!class/merging/*

-keep class com.base.wanandroid.db.**{ *; }
-keep class com.base.wanandroid.history.**{ *; }
################common###############

#keep coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}
-keep class kotlin.reflect.jvm.internal.**{ *; }


# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

#keep fastjson
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }
-keepattributes Signature
-keepattributes Annotation
-keepattributes InnerClasses



-keep class com.google.android.material.** {*;}
-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#annotation
-keep class android.support.annotation.** { *; }
-keep interface android.support.annotation.** { *; }



 #实体类不参与混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepnames class * implements java.io.Serializable
-keepattributes Signature
-keep class **.R$* { *; }
-ignorewarnings
-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclasseswithmembernames class * { # 保持native方法不被混淆
    native <methods>;
}

-keepclassmembers enum * {  # 使用enum类型时需要注意避免以下两个方法混淆，因为enum类的特殊性，以下两个方法会被反射调用，
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

################support###############
-keep class android.support.** { *; }
-keep interface android.support.** { *; }
-dontwarn android.support.**

-keep class com.google.android.material.** { *; }
-keep class androidx.** { *; }
-keep public class * extends androidx.**
-keep interface androidx.** { *; }
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**
################glide###############
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# 保留自定义控件(继承自View)不能被混淆
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(***);
    *** get* ();
}

################AgentWeb###############
-keep class com.just.agentweb.** { *; }

-dontwarn com.just.agentweb.**


# For using GSON @Expose annotation
-keepattributes *Annotation*
# Gson specific classes
-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.jinrishici.sdk.android.model.** { *; }

-keepclassmembers class * implements androidx.viewbinding.ViewBinding{
  public static <methods>;
}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class **.R$*{
public static final int *;
}