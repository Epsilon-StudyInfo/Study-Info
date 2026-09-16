# Default ProGuard rules.
# Disable minification by default in build.gradle.kts (release isMinifyEnabled = false).
# When enabling, add Firebase / Room / Moshi keep rules below.

-keep class com.studyinfo.app.data.database.entity.** { *; }
-keep class com.studyinfo.app.domain.model.** { *; }

# Room
-keep class * extends androidx.room.RoomDatabase { *; }

# Moshi
-keepclassmembers class * {
    @com.squareup.moshi.JsonClass *;
}
-keep @com.squareup.moshi.JsonClass class * { *; }

# Firebase
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**
