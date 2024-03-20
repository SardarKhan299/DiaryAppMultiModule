// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
//    dependencies{
//        classpath("com.google.gms:google-services:4.4.1")
//    }
    val compose_version by extra("1.4.6")
}
plugins {
    id ("com.android.application") version "8.1.1" apply false
    id ("com.android.library") version "8.1.1" apply false
    id ("org.jetbrains.kotlin.android") version "1.8.20" apply false
    id ("io.realm.kotlin") version "1.11.0" apply false
    id ("com.google.dagger.hilt.android") version "2.48" apply false
    id ("com.google.devtools.ksp") version "1.8.20-1.0.11" apply false
}